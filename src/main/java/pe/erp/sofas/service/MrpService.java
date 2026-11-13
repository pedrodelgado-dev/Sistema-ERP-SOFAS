package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.MaterialRepository;
import pe.erp.sofas.repository.OrdenCompraRepository;
import pe.erp.sofas.repository.PedidoRepository;
import pe.erp.sofas.service.calculo.Explosion;
import pe.erp.sofas.service.calculo.Medidas;
import pe.erp.sofas.service.calculo.Opciones;
import pe.erp.sofas.service.calculo.UnidadCompra;

import java.time.LocalDate;
import java.util.*;

/**
 * Requerimiento de materiales: requerido = OP no terminadas + pedidos confirmados sin OP;
 * transito = OC Solicitadas y Aprobadas; faltante = max(0, requerido − stock − transito).
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MrpService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Fila(@EquipoPLMD_MGML_AMCS_DRJB
      Material material, @EquipoPLMD_MGML_AMCS_DRJB
        double requerido, @EquipoPLMD_MGML_AMCS_DRJB
          double transito, @EquipoPLMD_MGML_AMCS_DRJB
            double faltante, @EquipoPLMD_MGML_AMCS_DRJB
              LocalDate necesitaEl,
                       @EquipoPLMD_MGML_AMCS_DRJB
                       Proveedor proveedor, @EquipoPLMD_MGML_AMCS_DRJB
                         Set<String> origenes) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public double stock() {
            return material.getStock();
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public double reservado() {
            return material.getReservado();
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public double disponible() {
            return material.getDisponible();
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public double valorFaltante() {
            return faltante * material.getCosto();
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Filtro(@EquipoPLMD_MGML_AMCS_DRJB
      LocalDate desde, @EquipoPLMD_MGML_AMCS_DRJB
        LocalDate hasta, @EquipoPLMD_MGML_AMCS_DRJB
          String categoria, @EquipoPLMD_MGML_AMCS_DRJB
            Long proveedorId, @EquipoPLMD_MGML_AMCS_DRJB
              boolean soloFaltantes) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public static final Filtro TODO = new Filtro(null, null, null, null, false);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Resultado(@EquipoPLMD_MGML_AMCS_DRJB
      List<Fila> filas, @EquipoPLMD_MGML_AMCS_DRJB
        int ops, @EquipoPLMD_MGML_AMCS_DRJB
          int pedidos) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public long conFaltante() {
            return filas.stream().filter(f -> f.faltante() > 1e-6).count();
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public double valorFaltante() {
            return filas.stream().mapToDouble(Fila::valorFaltante).sum();
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProduccionService produccionService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final FichaService fichaService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PedidoRepository pedidoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final OrdenCompraRepository compraRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final MaterialRepository materialRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final CompraService compraService;

    @EquipoPLMD_MGML_AMCS_DRJB
    public Resultado calcular(Filtro filtro) {
        if (filtro == null) filtro = Filtro.TODO;
        Map<Long, double[]> req = new LinkedHashMap<>();
        Map<Long, LocalDate> fechas = new HashMap<>();
        Map<Long, Set<String>> origenes = new HashMap<>();
        int ops = 0, pedidos = 0;
        for (OrdenProduccion op : produccionService.activas()) {
            if (!enRango(op.getFechaEntrega(), filtro)) continue;
            Explosion.Resultado r = produccionService.requerimiento(op);
            if (r == null) continue;
            ops++;
            LocalDate necesita = op.getEtapa() == Etapa.PENDIENTE ? fecha(op.getFechaInicio()) : AppClock.hoy();
            acumular(r, op.getCodigo(), necesita, req, fechas, origenes);
        }
        for (Pedido p : pedidoRepository.findAllByOrderByCodigoDesc()) {
            if (p.getEstado() != EstadoPedido.CONFIRMADO || p.getOpCodigo() != null || p.getProducto() == null) continue;
            if (!enRango(p.getFechaEntrega(), filtro)) continue;
            FichaTecnica f = fichaService.aprobadaDe(p.getProducto());
            if (f == null) continue;
            pedidos++;
            Explosion.Resultado r = fichaService.explotar(f, p.getCantidad(), opcionesPedido(p, f));
            LocalDate necesita = p.getFechaEntrega() == null ? AppClock.hoy().plusDays(7) : p.getFechaEntrega().minusDays(10);
            acumular(r, p.getCodigo(), necesita.isBefore(AppClock.hoy()) ? AppClock.hoy() : necesita, req, fechas, origenes);
        }
        Map<Long, Double> transito = transito();
        List<Fila> filas = new ArrayList<>();
        for (var e : req.entrySet()) {
            Material m = materialRepository.findById(e.getKey()).orElseThrow();
            if (filtro.categoria() != null && !filtro.categoria().equals(m.getCategoria())) continue;
            if (filtro.proveedorId() != null && (m.getProveedor() == null || !m.getProveedor().getId().equals(filtro.proveedorId()))) continue;
            double requerido = m.isEntera() ? Math.ceil(e.getValue()[0] - 1e-9) : e.getValue()[0];
            double tr = transito.getOrDefault(m.getId(), 0d);
            double faltante = Math.max(0, requerido - m.getStock() - tr);
            if (filtro.soloFaltantes() && faltante <= 1e-6) continue;
            filas.add(new Fila(m, requerido, tr, faltante, fechas.get(m.getId()), m.getProveedor(), origenes.get(m.getId())));
        }
        filas.sort(Comparator.comparingDouble(Fila::valorFaltante).reversed().thenComparing(f -> f.material().getCodigo()));
        return new Resultado(filas, ops, pedidos);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Opciones opcionesPedido(Pedido p, FichaTecnica f) {
        Map<String, String> sust = new HashMap<>();
        if (p.getTela() != null) {
            for (LineaBOM l : f.getLineas()) if (l.getMaterial().isTela()) sust.put(l.getMaterial().getCodigo(), p.getTela().getTipo().materialCodigo());
            if (Boolean.TRUE.equals(f.getCompuesta())) {
                for (TipoTela t : TipoTela.values()) sust.put(t.materialCodigo(), p.getTela().getTipo().materialCodigo());
            }
        }
        Medidas m = null;
        if (p.getLargo() != null && p.getLargo() > 0 && !Boolean.TRUE.equals(f.getCompuesta())) {
            m = new Medidas(p.getLargo(), p.getFondo() == null ? f.getFondo() : p.getFondo(),
                    p.getAlto() == null ? f.getAlto() : p.getAlto(), f.getCuerpos());
        }
        return new Opciones(m, sust, List.of(), false, 0, 0);
    }

    /** Cantidades en OC Solicitadas y Aprobadas por material. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public Map<Long, Double> transito() {
        Map<Long, Double> t = new HashMap<>();
        for (OrdenCompra oc : compraRepository.findAll()) {
            if (!oc.getEstado().enTransito()) continue;
            for (ItemCompra i : oc.getItems()) t.merge(i.getMaterial().getId(), i.getCantidad(), Double::sum);
        }
        return t;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public long comprasEnTransito() {
        return compraRepository.findAll().stream().filter(o -> o.getEstado().enTransito()).count();
    }

    /** Una OC en Borrador por proveedor, redondeando a la unidad de compra. Devuelve los codigos creados. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public List<String> generarOrdenes(List<Fila> filas, String origen) {
        Map<Proveedor, List<CompraService.LineaCompra>> porProveedor = new LinkedHashMap<>();
        for (Fila f : filas) {
            if (f.faltante() <= 1e-6 || f.proveedor() == null) continue;
            double cant = UnidadCompra.redondear(f.material().getUnidad(), f.faltante());
            porProveedor.computeIfAbsent(f.proveedor(), k -> new ArrayList<>())
                    .add(new CompraService.LineaCompra(f.material().getId(), null, cant,
                            java.math.BigDecimal.valueOf(f.material().getCosto())));
        }
        List<String> codigos = new ArrayList<>();
        porProveedor.forEach((p, lineas) -> codigos.add(compraService.crear(p.getId(), lineas, false,
                "Generada desde el requerimiento de materiales.", origen, true).getCodigo()));
        return codigos;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private void acumular(Explosion.Resultado r, String origen, LocalDate necesita, Map<Long, double[]> req,
                          Map<Long, LocalDate> fechas, Map<Long, Set<String>> origenes) {
        for (Explosion.PorMaterial pm : r.porMaterial()) {
            Long id = pm.material().getId();
            req.computeIfAbsent(id, k -> new double[1])[0] += pm.cantidad();
            fechas.merge(id, necesita, (a, b) -> a.isBefore(b) ? a : b);
            origenes.computeIfAbsent(id, k -> new LinkedHashSet<>()).add(origen);
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static boolean enRango(LocalDate f, Filtro filtro) {
        if (f == null) return true;
        if (filtro.desde() != null && f.isBefore(filtro.desde())) return false;
        return filtro.hasta() == null || !f.isAfter(filtro.hasta());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static LocalDate fecha(LocalDate f) {
        return f == null || f.isBefore(AppClock.hoy()) ? AppClock.hoy() : f;
    }
}
