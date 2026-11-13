package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.*;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/** Ordenes de compra: Borrador → Solicitada → Aprobada → Recibida (o Cancelada). */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompraService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public record LineaCompra(@EquipoPLMD_MGML_AMCS_DRJB
      Long materialId, @EquipoPLMD_MGML_AMCS_DRJB
        Long telaId, @EquipoPLMD_MGML_AMCS_DRJB
          double cantidad, @EquipoPLMD_MGML_AMCS_DRJB
            BigDecimal precioUnitario) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final OrdenCompraRepository repository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProveedorRepository proveedorRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final MaterialRepository materialRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final TelaRepository telaRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final InventarioService inventarioService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final TelaService telaService;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<OrdenCompra> listar() {
        return repository.findAllByOrderByCodigoDesc();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public OrdenCompra orden(Long id) {
        return repository.findById(id).orElseThrow(() -> new NegocioException("La orden de compra ya no existe."));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String siguienteCodigo() {
        return Codigos.siguiente(repository.findAll(), OrdenCompra::getCodigo, "OC-", 4);
    }

    /** Ordenes aprobadas o recibidas aun no pagadas. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public List<OrdenCompra> porPagar() {
        return listar().stream()
                .filter(o -> (o.getEstado() == EstadoCompra.APROBADA || o.getEstado() == EstadoCompra.RECIBIDA) && o.getFechaPago() == null)
                .toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public OrdenCompra crear(Long proveedorId, List<LineaCompra> lineas, boolean enviarSolicitud, String observaciones,
                             String origen, boolean nueva) {
        if (proveedorId == null) throw new NegocioException("Selecciona un proveedor.");
        Proveedor proveedor = proveedorRepository.findById(proveedorId).orElseThrow();
        if (!proveedor.getActivo()) throw new NegocioException("El proveedor está inactivo.");
        List<LineaCompra> validas = lineas.stream().filter(l -> l.materialId() != null && l.cantidad() > 0).toList();
        if (validas.isEmpty()) throw new NegocioException("Agrega al menos un material con cantidad.");
        OrdenCompra orden = OrdenCompra.builder()
                .codigo(siguienteCodigo()).proveedor(proveedor).fecha(AppClock.hoy()).fechaEntrega(AppClock.hoy().plusDays(5))
                .estado(enviarSolicitud ? EstadoCompra.SOLICITADA : EstadoCompra.BORRADOR)
                .origen(origen == null ? "Manual" : origen).nueva(nueva)
                .observaciones(observaciones).build();
        for (LineaCompra l : validas) {
            if (l.precioUnitario() == null || l.precioUnitario().signum() < 0) throw new NegocioException("Revisa los precios unitarios.");
            Material m = materialRepository.findById(l.materialId()).orElseThrow();
            Tela tela = l.telaId() == null ? null : telaRepository.findById(l.telaId()).orElse(null);
            if (tela == null && m.isTela()) tela = colorParaReponer(m);
            orden.agregar(ItemCompra.builder().material(m).tela(tela).cantidad(l.cantidad()).precioUnitario(l.precioUnitario()).build());
        }
        return repository.save(orden);
    }

    /** Color de la tela con menor cobertura (stock / minimo) para reponer. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private Tela colorParaReponer(Material m) {
        TipoTela tipo = TipoTela.porMaterial(m.getCodigo());
        return telaService.porTipo(tipo).stream().filter(t -> !t.getDescontinuada())
                .min(Comparator.comparingDouble(t -> t.getStock() / Math.max(1, t.getMinimo()))).orElse(null);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public OrdenCompra solicitar(Long id) {
        return transicion(id, EstadoCompra.BORRADOR, EstadoCompra.SOLICITADA);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public OrdenCompra aprobar(Long id) {
        return transicion(id, EstadoCompra.SOLICITADA, EstadoCompra.APROBADA);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public void marcarVista(Long id) {
        OrdenCompra o = orden(id);
        if (Boolean.TRUE.equals(o.getNueva())) {
            o.setNueva(false);
            repository.save(o);
        }
    }

    /** Recepcion: suma al stock (en las telas, al color del muestrario) y registra "Entrada" en el kardex. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public OrdenCompra recibir(Long id) {
        OrdenCompra orden = orden(id);
        if (orden.getEstado() != EstadoCompra.APROBADA) throw new NegocioException("Solo se pueden recibir órdenes aprobadas.");
        for (ItemCompra item : orden.getItems()) {
            Material m = materialRepository.findById(item.getMaterial().getId()).orElseThrow();
            if (m.isTela() && item.getTela() != null) {
                telaService.entradaRollo(item.getTela().getId(), item.getCantidad(), orden.getCodigo());
            } else {
                inventarioService.registrar(m, TipoMovimiento.ENTRADA, item.getCantidad(), orden.getCodigo(),
                        "Recepción " + orden.getProveedor().getRazonSocial());
            }
        }
        orden.setEstado(EstadoCompra.RECIBIDA);
        orden.setFechaRecepcion(AppClock.hoy());
        orden.setNueva(false);
        Proveedor p = orden.getProveedor();
        p.setTotalComprado(p.getTotalComprado().add(orden.getTotal()));
        proveedorRepository.save(p);
        return repository.save(orden);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public OrdenCompra cancelar(Long id) {
        OrdenCompra orden = orden(id);
        if (orden.getEstado() == EstadoCompra.RECIBIDA) throw new NegocioException("No se puede cancelar una orden ya recibida.");
        orden.setEstado(EstadoCompra.CANCELADA);
        orden.setNueva(false);
        return repository.save(orden);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public OrdenCompra registrarPago(Long id) {
        OrdenCompra orden = orden(id);
        orden.setFechaPago(AppClock.hoy());
        return repository.save(orden);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private OrdenCompra transicion(Long id, EstadoCompra desde, EstadoCompra hacia) {
        OrdenCompra orden = orden(id);
        if (orden.getEstado() != desde) throw new NegocioException("La orden está en estado " + orden.getEstado() + ".");
        orden.setEstado(hacia);
        orden.setNueva(false);
        return repository.save(orden);
    }
}
