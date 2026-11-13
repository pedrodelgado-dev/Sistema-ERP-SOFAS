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
import java.util.List;

@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Resumen(@EquipoPLMD_MGML_AMCS_DRJB
      BigDecimal totalComprado, @EquipoPLMD_MGML_AMCS_DRJB
        BigDecimal saldo, @EquipoPLMD_MGML_AMCS_DRJB
          long compras, @EquipoPLMD_MGML_AMCS_DRJB
            long pedidos) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ClienteRepository repository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final VentaRepository ventaRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PedidoRepository pedidoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PagoRepository pagoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final RetapizadoRepository retapizadoRepository;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Cliente> listar() {
        return repository.findAllByOrderByCodigoAsc();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Cliente cliente(Long id) {
        return repository.findById(id).orElseThrow(() -> new NegocioException("El cliente ya no existe."));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Venta> ventas(Cliente c) {
        return ventaRepository.findAllByOrderByFechaDesc().stream().filter(v -> v.getCliente() != null && v.getCliente().getId().equals(c.getId())).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Pedido> pedidos(Cliente c) {
        return pedidoRepository.findAllByOrderByCodigoDesc().stream().filter(p -> p.getCliente().getId().equals(c.getId())).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Pago> pagos(Cliente c) {
        return pagoRepository.findByClienteIdOrderByFechaDesc(c.getId());
    }

    /** Total comprado = ventas + pedidos entregados; saldo = saldos de pedidos abiertos. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public Resumen resumen(Cliente c) {
        List<Venta> v = ventas(c);
        List<Pedido> p = pedidos(c);
        BigDecimal ventas = v.stream().map(Venta::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal pedidos = p.stream().filter(x -> x.getEstado() != EstadoPedido.CANCELADO).map(Pedido::getPrecio).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal retap = retapizadoRepository.findAll().stream().filter(r -> r.getCliente().getId().equals(c.getId())
                && r.getEstado() == EstadoRetapizado.ENTREGADO && r.getTotal() != null).map(Retapizado::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal saldo = p.stream().filter(x -> !x.getEstado().cerrado()).map(Pedido::getSaldo).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Resumen(ventas.add(pedidos).add(retap), saldo, v.size(), p.size());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String siguienteCodigo() {
        return Codigos.siguiente(repository.findAll(), Cliente::getCodigo, "C-", 4);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Cliente guardar(Cliente c) {
        validar(c);
        boolean duplicado = listar().stream().anyMatch(x -> x.getDocumento() != null && x.getDocumento().equals(c.getDocumento())
                && !x.getId().equals(c.getId()));
        if (duplicado) throw new NegocioException("Ya existe un cliente con el documento " + c.getDocumento() + ".");
        if (c.getId() == null) {
            c.setCodigo(siguienteCodigo());
            c.setFechaRegistro(AppClock.hoy());
        }
        return repository.save(c);
    }

    /** DNI de 8 digitos para personas; RUC de 11 para empresas. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static void validar(Cliente c) {
        if (c.getNombre() == null || c.getNombre().isBlank()) throw new NegocioException("El nombre es obligatorio.");
        String doc = c.getDocumento() == null ? "" : c.getDocumento().trim();
        if (c.getTipo() == Cliente.Tipo.PERSONA && !doc.matches("\\d{8}")) throw new NegocioException("El DNI debe tener 8 dígitos.");
        if (c.getTipo() == Cliente.Tipo.EMPRESA && !doc.matches("\\d{11}")) throw new NegocioException("El RUC debe tener 11 dígitos.");
        if (c.getTelefono() != null && !c.getTelefono().isBlank() && !c.getTelefono().replace(" ", "").matches("\\+?\\d{6,12}")) {
            throw new NegocioException("Revisa el teléfono.");
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public void eliminar(Long id) {
        Cliente c = cliente(id);
        if (!ventas(c).isEmpty() || !pedidos(c).isEmpty()) throw new NegocioException("El cliente tiene ventas o pedidos; no se puede eliminar.");
        repository.delete(c);
    }
}
