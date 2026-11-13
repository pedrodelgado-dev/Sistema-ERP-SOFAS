package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.core.Money;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.ClienteRepository;
import pe.erp.sofas.repository.ProductoRepository;
import pe.erp.sofas.repository.VentaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** Punto de venta: descuenta stock, numera B001 / F001 y valida factura, efectivo y pago mixto. */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VentaService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Linea(@EquipoPLMD_MGML_AMCS_DRJB
      Long productoId, @EquipoPLMD_MGML_AMCS_DRJB
        int cantidad, @EquipoPLMD_MGML_AMCS_DRJB
          BigDecimal precioUnitario) {
    }

    /**
     * Datos del cobro. En pago mixto {@code metodo2} y los montos deben sumar el total; en efectivo
     * {@code recibido} debe cubrir el total.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    public record Cobro(@EquipoPLMD_MGML_AMCS_DRJB
      Long clienteId, @EquipoPLMD_MGML_AMCS_DRJB
        Venta.Comprobante comprobante, @EquipoPLMD_MGML_AMCS_DRJB
          List<Linea> lineas, @EquipoPLMD_MGML_AMCS_DRJB
            BigDecimal descuento,
                        @EquipoPLMD_MGML_AMCS_DRJB
                        String metodo, @EquipoPLMD_MGML_AMCS_DRJB
                          BigDecimal recibido, @EquipoPLMD_MGML_AMCS_DRJB
                            String metodo2, @EquipoPLMD_MGML_AMCS_DRJB
                              BigDecimal monto1, @EquipoPLMD_MGML_AMCS_DRJB
                                BigDecimal monto2) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final VentaRepository repository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProductoRepository productoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ClienteRepository clienteRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final UsuarioActual usuario;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Venta> historial() {
        return repository.findAllByOrderByFechaDesc();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Venta> delMes(LocalDate referencia) {
        LocalDate inicio = referencia.withDayOfMonth(1);
        LocalDate fin = inicio.plusMonths(1);
        return historial().stream().filter(v -> !v.getFecha().toLocalDate().isBefore(inicio) && v.getFecha().toLocalDate().isBefore(fin)).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public BigDecimal totalMes() {
        return delMes(AppClock.hoy()).stream().map(Venta::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public int sofasMes() {
        return delMes(AppClock.hoy()).stream().mapToInt(Venta::getUnidades).sum();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public int siguienteNumero(Venta.Comprobante c) {
        return repository.findBySerie(c.serie()).stream().mapToInt(Venta::getNumero).max().orElse(0) + 1;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Venta cobrar(Cobro c) {
        if (c.lineas() == null || c.lineas().isEmpty()) throw new NegocioException("El carrito está vacío.");
        Cliente cliente = c.clienteId() == null ? null : clienteRepository.findById(c.clienteId()).orElseThrow();
        if (c.comprobante() == Venta.Comprobante.FACTURA && (cliente == null || !cliente.tieneRuc())) {
            throw new NegocioException("La factura exige un cliente con RUC.");
        }
        Venta venta = Venta.builder().comprobante(c.comprobante()).serie(c.comprobante().serie())
                .numero(siguienteNumero(c.comprobante())).fecha(LocalDateTime.of(AppClock.hoy(), AppClock.HORA))
                .cliente(cliente).vendedor(usuario.nombre()).build();
        BigDecimal bruto = BigDecimal.ZERO;
        for (Linea l : c.lineas()) {
            Producto p = productoRepository.findById(l.productoId()).orElseThrow();
            if (l.cantidad() <= 0) throw new NegocioException("Cantidad inválida para " + p.getNombre() + ".");
            if (p.getStock() < l.cantidad()) {
                throw new NegocioException("Stock insuficiente de " + p.getNombre() + ": quedan " + p.getStock() + ".");
            }
            p.setStock(p.getStock() - l.cantidad());
            productoRepository.save(p);
            BigDecimal precio = l.precioUnitario() == null ? p.getPrecio() : l.precioUnitario();
            venta.agregar(ItemVenta.builder().producto(p).cantidad(l.cantidad()).precioUnitario(precio).build());
            bruto = bruto.add(precio.multiply(BigDecimal.valueOf(l.cantidad())));
        }
        BigDecimal descuento = c.descuento() == null ? BigDecimal.ZERO : c.descuento().setScale(2, RoundingMode.HALF_UP);
        if (descuento.signum() < 0 || descuento.compareTo(bruto) >= 0) throw new NegocioException("El descuento no es válido.");
        BigDecimal total = bruto.subtract(descuento);
        venta.setDescuento(descuento);
        venta.setTotal(total);
        String metodo = c.metodo() == null ? "Efectivo" : c.metodo();
        if ("Mixto".equals(metodo)) {
            BigDecimal a = c.monto1() == null ? BigDecimal.ZERO : c.monto1();
            BigDecimal b = c.monto2() == null ? BigDecimal.ZERO : c.monto2();
            if (a.add(b).compareTo(total) != 0) {
                throw new NegocioException("En pago mixto los montos deben sumar " + Money.fmt(total) + ".");
            }
            metodo = "Mixto (Efectivo " + Money.fmt(a) + " + " + (c.metodo2() == null ? "Yape" : c.metodo2()) + " " + Money.fmt(b) + ")";
        }
        if ("Efectivo".equals(metodo)) {
            BigDecimal recibido = c.recibido() == null ? total : c.recibido();
            if (recibido.compareTo(total) < 0) throw new NegocioException("El monto recibido no cubre el total.");
            venta.setRecibido(recibido);
            venta.setVuelto(recibido.subtract(total));
        }
        venta.setMetodoPago(metodo);
        return repository.save(venta);
    }
}
