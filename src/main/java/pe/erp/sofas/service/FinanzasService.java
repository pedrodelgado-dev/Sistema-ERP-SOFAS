package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/** Ingresos, egresos, cuentas por cobrar/pagar y rentabilidad por modelo y por tela. */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FinanzasService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Mes(@EquipoPLMD_MGML_AMCS_DRJB
      int anio, @EquipoPLMD_MGML_AMCS_DRJB
        int mes, @EquipoPLMD_MGML_AMCS_DRJB
          double ventas, @EquipoPLMD_MGML_AMCS_DRJB
            double ingresos, @EquipoPLMD_MGML_AMCS_DRJB
              double egresos) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public String etiqueta() {
            return AppClock.mesCorto(mes) + (mes == 1 ? " " + String.valueOf(anio).substring(2) : "");
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Kpis(@EquipoPLMD_MGML_AMCS_DRJB
      double ingresos, @EquipoPLMD_MGML_AMCS_DRJB
        double egresos, @EquipoPLMD_MGML_AMCS_DRJB
          double utilidad, @EquipoPLMD_MGML_AMCS_DRJB
            double porCobrar, @EquipoPLMD_MGML_AMCS_DRJB
              double porPagar) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Rentabilidad(@EquipoPLMD_MGML_AMCS_DRJB
      String nombre, @EquipoPLMD_MGML_AMCS_DRJB
        String detalle, @EquipoPLMD_MGML_AMCS_DRJB
          int unidades, @EquipoPLMD_MGML_AMCS_DRJB
            double ingresos, @EquipoPLMD_MGML_AMCS_DRJB
              double costo) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public double margen() {
            return ingresos - costo;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public double margenPct() {
            return ingresos == 0 ? 0 : margen() * 100 / ingresos;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ResumenMensualRepository resumenRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final VentaRepository ventaRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PagoRepository pagoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final OrdenCompraRepository compraRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final GastoRepository gastoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PedidoRepository pedidoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ConsumoRealRepository consumoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProductoService productoService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final CompraService compraService;

    /** Ultimos 12 meses: 11 cierres historicos + el mes en curso calculado. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Mes> ultimos12() {
        List<Mes> l = new ArrayList<>();
        LocalDate hoy = AppClock.hoy();
        Map<String, ResumenMensual> hist = new HashMap<>();
        resumenRepository.findAll().forEach(r -> hist.put(r.getAnio() + "-" + r.getMes(), r));
        for (int i = 11; i >= 1; i--) {
            LocalDate d = hoy.minusMonths(i);
            ResumenMensual r = hist.get(d.getYear() + "-" + d.getMonthValue());
            double v = r == null ? 0 : r.getVentas().doubleValue();
            l.add(new Mes(d.getYear(), d.getMonthValue(), v, v * 1.18, r == null ? 0 : r.getEgresos().doubleValue()));
        }
        l.add(new Mes(hoy.getYear(), hoy.getMonthValue(), ventasMes(), ingresosMes(), egresosMes()));
        return l;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double ventasMes() {
        LocalDate inicio = AppClock.hoy().withDayOfMonth(1);
        return ventaRepository.findAll().stream().filter(v -> !v.getFecha().toLocalDate().isBefore(inicio))
                .mapToDouble(v -> v.getTotal().doubleValue()).sum();
    }

    /** Ventas de tienda + pagos de pedidos y retapizados del mes. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public double ingresosMes() {
        LocalDate inicio = AppClock.hoy().withDayOfMonth(1);
        double pagos = pagoRepository.findAll().stream().filter(p -> p.getFecha() != null && !p.getFecha().isBefore(inicio))
                .mapToDouble(p -> p.getMonto().doubleValue()).sum();
        return ventasMes() + pagos;
    }

    /** Compras recibidas + gastos del mes. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public double egresosMes() {
        LocalDate inicio = AppClock.hoy().withDayOfMonth(1);
        double compras = compraRepository.findAll().stream().filter(o -> o.getEstado() == EstadoCompra.RECIBIDA
                && o.getFechaRecepcion() != null && !o.getFechaRecepcion().isBefore(inicio)).mapToDouble(o -> o.getTotal().doubleValue()).sum();
        double gastos = gastoRepository.findAll().stream().filter(g -> !g.getFecha().isBefore(inicio)).mapToDouble(g -> g.getMonto().doubleValue()).sum();
        return compras + gastos;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Pedido> porCobrar() {
        return pedidoRepository.findAllByOrderByCodigoDesc().stream().filter(p -> !p.getEstado().cerrado() && p.getSaldo().signum() > 0).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<OrdenCompra> porPagar() {
        return compraService.porPagar();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Gasto> gastosMes() {
        LocalDate inicio = AppClock.hoy().withDayOfMonth(1);
        return gastoRepository.findAll().stream().filter(g -> !g.getFecha().isBefore(inicio))
                .sorted(Comparator.comparing(Gasto::getFecha).reversed()).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Kpis kpis() {
        double ing = ingresosMes(), egr = egresosMes();
        double cxc = porCobrar().stream().mapToDouble(p -> p.getSaldo().doubleValue()).sum();
        double cxp = porPagar().stream().mapToDouble(o -> o.getTotal().doubleValue()).sum();
        return new Kpis(ing, egr, ing - egr, cxc, cxp);
    }

    /** Por modelo: ventas de tienda + pedidos no cancelados; costo = costo estandar de la ficha (o costo del modelo). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Rentabilidad> porModelo() {
        Map<Long, int[]> unidades = new LinkedHashMap<>();
        Map<Long, double[]> ingresos = new HashMap<>();
        Map<Long, Producto> productos = new HashMap<>();
        for (Venta v : ventaRepository.findAll()) {
            for (ItemVenta i : v.getItems()) {
                Producto p = i.getProducto();
                productos.put(p.getId(), p);
                unidades.computeIfAbsent(p.getId(), k -> new int[1])[0] += i.getCantidad();
                ingresos.computeIfAbsent(p.getId(), k -> new double[1])[0] += i.getSubtotal().doubleValue();
            }
        }
        for (Pedido p : pedidoRepository.findAll()) {
            if (p.getEstado() == EstadoPedido.CANCELADO || p.getProducto() == null) continue;
            productos.put(p.getProducto().getId(), p.getProducto());
            unidades.computeIfAbsent(p.getProducto().getId(), k -> new int[1])[0] += p.getCantidad();
            ingresos.computeIfAbsent(p.getProducto().getId(), k -> new double[1])[0] += p.getPrecio().doubleValue();
        }
        List<Rentabilidad> r = new ArrayList<>();
        for (var e : unidades.entrySet()) {
            Producto p = productos.get(e.getKey());
            var ce = productoService.costoEstandar(p);
            double costoU = ce != null ? ce.costo() : p.getCosto() == null ? 0 : p.getCosto().doubleValue();
            r.add(new Rentabilidad(p.getNombre(), p.getCodigo(), e.getValue()[0], ingresos.get(e.getKey())[0], costoU * e.getValue()[0]));
        }
        r.sort(Comparator.comparingDouble(Rentabilidad::margen).reversed());
        return r;
    }

    /** Por tipo de tela: pedidos con esa tela (ingreso y recargo) y metros consumidos en produccion. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Rentabilidad> porTela() {
        List<Rentabilidad> r = new ArrayList<>();
        for (TipoTela t : TipoTela.values()) {
            List<Pedido> ped = pedidoRepository.findAll().stream().filter(p -> p.getTela() != null && p.getTela().getTipo() == t
                    && p.getEstado() != EstadoPedido.CANCELADO).toList();
            double metros = consumoRepository.findAll().stream().filter(c -> c.getMaterial().getCodigo().equals(t.materialCodigo()))
                    .mapToDouble(ConsumoReal::getRealConsumido).sum();
            double ingresoTela = ped.stream().mapToDouble(p -> p.getPrecio().doubleValue() * 0.22).sum() + metros * t.costoMetro() * 1.55;
            double costo = metros * t.costoMetro() + ped.stream().mapToDouble(p -> p.getCantidad() * 10.5 * t.costoMetro()).sum();
            r.add(new Rentabilidad(t.nombre(), "S/ " + (int) t.costoMetro() + "/m · recargo " + (int) t.recargoPct() + " %",
                    ped.size(), ingresoTela, costo));
        }
        r.sort(Comparator.comparingDouble(Rentabilidad::margen).reversed());
        return r;
    }
}
