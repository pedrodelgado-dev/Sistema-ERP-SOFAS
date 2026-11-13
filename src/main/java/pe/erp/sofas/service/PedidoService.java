package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.core.Money;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.*;
import pe.erp.sofas.service.calculo.Formula;
import pe.erp.sofas.service.calculo.Medidas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Pedidos a medida: Registrado → Cotizado → Confirmado (adelanto ≥ 50 %) → En produccion (con OP)
 * → Listo para entrega → Entregado; o Cancelado.
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final BigDecimal MIN_ADELANTO = new BigDecimal("0.50");

    @EquipoPLMD_MGML_AMCS_DRJB
    public record NuevoPedido(@EquipoPLMD_MGML_AMCS_DRJB
      Long clienteId, @EquipoPLMD_MGML_AMCS_DRJB
        Long productoId, @EquipoPLMD_MGML_AMCS_DRJB
          Pedido.Tipo tipo, @EquipoPLMD_MGML_AMCS_DRJB
            int cantidad, @EquipoPLMD_MGML_AMCS_DRJB
              Integer largo,
                              @EquipoPLMD_MGML_AMCS_DRJB
                              Integer fondo, @EquipoPLMD_MGML_AMCS_DRJB
                                Integer alto, @EquipoPLMD_MGML_AMCS_DRJB
                                  Long telaId, @EquipoPLMD_MGML_AMCS_DRJB
                                    LocalDate fechaEntrega, @EquipoPLMD_MGML_AMCS_DRJB
                                      BigDecimal precio,
                              @EquipoPLMD_MGML_AMCS_DRJB
                              BigDecimal adelanto, @EquipoPLMD_MGML_AMCS_DRJB
                                String metodo, @EquipoPLMD_MGML_AMCS_DRJB
                                  String observaciones, @EquipoPLMD_MGML_AMCS_DRJB
                                    String configuracion,
                              @EquipoPLMD_MGML_AMCS_DRJB
                              String especificacion) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final PedidoRepository repository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ClienteRepository clienteRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProductoRepository productoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final TelaRepository telaRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PagoRepository pagoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final FichaService fichaService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProduccionService produccionService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ObjectProvider<ConfiguradorService> configurador;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Pedido> listar() {
        return repository.findAllByOrderByCodigoDesc();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Pedido pedido(Long id) {
        return repository.findById(id).orElseThrow(() -> new NegocioException("El pedido ya no existe."));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Optional<Pedido> porCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String siguienteCodigo() {
        return Codigos.siguiente(repository.findAll(), Pedido::getCodigo, "PE-", 4);
    }

    /** Pedidos a medida (personalizados) aun no entregados ni cancelados. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public long pendientesAMedida() {
        return listar().stream().filter(p -> p.getTipo() == Pedido.Tipo.PERSONALIZADO && !p.getEstado().cerrado()).count();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public long abiertos() {
        return listar().stream().filter(p -> !p.getEstado().cerrado()).count();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Pago> pagos(Pedido p) {
        return pagoRepository.findAllByOrderByFechaDesc().stream().filter(x -> p.getCodigo().equals(x.getReferencia())).toList();
    }

    /** Precio sugerido: precio del modelo escalado por el largo pedido y con el recargo de la tela. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public BigDecimal precioSugerido(Producto p, Integer largo, Tela tela, int cantidad) {
        if (p == null) return BigDecimal.ZERO;
        double base = p.getPrecio().doubleValue();
        if (largo != null && largo > 0 && p.getLargo() != null && p.getLargo() > 0 && !largo.equals(p.getLargo())) {
            base = base * Math.pow(largo / (double) p.getLargo(), 0.85);
        }
        if (tela != null) base = base * (1 + tela.getTipo().recargoPct() / 100.0);
        return BigDecimal.valueOf(Formula.redondearDecena(base) * Math.max(1, cantidad)).setScale(2, RoundingMode.HALF_UP);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Pedido crear(NuevoPedido d) {
        if (d.clienteId() == null) throw new NegocioException("Elige el cliente.");
        if (d.productoId() == null) throw new NegocioException("Elige el modelo de sofá.");
        if (d.cantidad() <= 0) throw new NegocioException("La cantidad debe ser mayor a cero.");
        if (d.precio() == null || d.precio().signum() <= 0) throw new NegocioException("Ingresa el precio del pedido.");
        if (d.fechaEntrega() != null && d.fechaEntrega().isBefore(AppClock.hoy())) throw new NegocioException("La entrega no puede ser en el pasado.");
        BigDecimal adelanto = d.adelanto() == null ? BigDecimal.ZERO : d.adelanto();
        if (adelanto.signum() < 0 || adelanto.compareTo(d.precio()) > 0) throw new NegocioException("El adelanto no puede superar el precio.");
        Cliente c = clienteRepository.findById(d.clienteId()).orElseThrow();
        Pedido p = Pedido.builder().codigo(siguienteCodigo()).cliente(c)
                .producto(productoRepository.findById(d.productoId()).orElseThrow())
                .tipo(d.tipo() == null ? Pedido.Tipo.ESTANDAR : d.tipo()).cantidad(d.cantidad())
                .fecha(AppClock.hoy()).fechaEntrega(d.fechaEntrega()).precio(d.precio().setScale(2, RoundingMode.HALF_UP))
                .adelanto(BigDecimal.ZERO).estado(EstadoPedido.REGISTRADO)
                .largo(d.largo()).fondo(d.fondo()).alto(d.alto())
                .tela(d.telaId() == null ? null : telaRepository.findById(d.telaId()).orElse(null))
                .observaciones(d.observaciones()).configuracion(d.configuracion()).especificacion(d.especificacion())
                .build();
        if (p.getTela() != null) p.setMaterial(p.getTela().getTipo().nombre());
        p = repository.save(p);
        if (adelanto.signum() > 0) {
            p = registrarAdelanto(p.getId(), adelanto, d.metodo() == null ? "Efectivo" : d.metodo());
        }
        return p;
    }

    /** Registra un pago a cuenta. Si el pedido estaba cotizado y llega al 50 %, queda confirmado. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Pedido registrarAdelanto(Long id, BigDecimal monto, String metodo) {
        Pedido p = pedido(id);
        if (p.getEstado() == EstadoPedido.CANCELADO) throw new NegocioException("El pedido está cancelado.");
        if (monto == null || monto.signum() <= 0) throw new NegocioException("Ingresa un monto válido.");
        if (monto.compareTo(p.getSaldo()) > 0) throw new NegocioException("El monto supera el saldo de " + Money.fmt(p.getSaldo()) + ".");
        p.setAdelanto(p.getAdelanto().add(monto));
        pagoRepository.save(Pago.builder().cliente(p.getCliente()).referencia(p.getCodigo()).fecha(AppClock.hoy())
                .monto(monto).metodo(metodo).concepto(p.getAdelanto().compareTo(p.getPrecio()) >= 0 ? "Cancelación" : "Adelanto").build());
        if ((p.getEstado() == EstadoPedido.COTIZADO || p.getEstado() == EstadoPedido.REGISTRADO) && cubreMinimo(p)) {
            p.setEstado(EstadoPedido.CONFIRMADO);
        }
        return repository.save(p);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static boolean cubreMinimo(Pedido p) {
        return p.getAdelanto().compareTo(p.getPrecio().multiply(MIN_ADELANTO)) >= 0;
    }

    /** Avanza al siguiente estado del flujo validando cada paso. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Pedido avanzar(Long id) {
        Pedido p = pedido(id);
        return avanzarA(id, p.getEstado().siguiente());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Pedido avanzarA(Long id, EstadoPedido destino) {
        Pedido p = pedido(id);
        if (p.getEstado().cerrado()) throw new NegocioException("El pedido ya está " + p.getEstado().nombre().toLowerCase() + ".");
        switch (destino) {
            case CONFIRMADO -> {
                if (!cubreMinimo(p)) {
                    throw new NegocioException("Para confirmar se necesita un adelanto mínimo del 50 % ("
                            + Money.fmt(p.getPrecio().multiply(MIN_ADELANTO)) + ").");
                }
            }
            case EN_PRODUCCION -> {
                if (p.getOpCodigo() == null) throw new NegocioException("Genera la orden de producción para pasar a producción.");
            }
            case LISTO -> {
                if (p.getOpCodigo() != null) {
                    var op = produccionService.porCodigo(p.getOpCodigo()).orElse(null);
                    if (op != null && op.getEtapa() != Etapa.LISTO) {
                        throw new NegocioException("La orden " + op.getCodigo() + " aún está en " + op.getEtapa().nombre() + ".");
                    }
                }
            }
            case ENTREGADO -> {
                if (p.getSaldo().signum() > 0) throw new NegocioException("Registra el saldo de " + Money.fmt(p.getSaldo()) + " antes de entregar.");
            }
            default -> {
            }
        }
        p.setEstado(destino);
        return repository.save(p);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Pedido vincularOP(Long id, String opCodigo) {
        Pedido p = pedido(id);
        p.setOpCodigo(opCodigo);
        if (p.getEstado() == EstadoPedido.CONFIRMADO) p.setEstado(EstadoPedido.EN_PRODUCCION);
        return repository.save(p);
    }

    /** Genera la OP con la ficha aprobada del modelo y pasa el pedido a "En produccion". */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public OrdenProduccion generarOP(Long id, Long responsableId) {
        Pedido p = pedido(id);
        if (p.getOpCodigo() != null) throw new NegocioException("El pedido ya tiene la orden " + p.getOpCodigo() + ".");
        if (p.getEstado().cerrado()) throw new NegocioException("El pedido está cerrado.");
        if (!cubreMinimo(p)) throw new NegocioException("Registra un adelanto mínimo del 50 % antes de producir.");
        if (p.getEstado() == EstadoPedido.REGISTRADO || p.getEstado() == EstadoPedido.COTIZADO) p.setEstado(EstadoPedido.CONFIRMADO);
        repository.save(p);
        OrdenProduccion op;
        ConfigSofa cfg = ConfigSofa.leer(p.getConfiguracion());
        if (cfg != null) {
            ConfiguradorService conf = configurador.getObject();
            op = produccionService.crear(conf.nuevaOP(cfg, conf.cotizar(cfg), p.getCliente().getId(), responsableId,
                    p.getFechaEntrega(), p.getCodigo()));
        } else {
            FichaTecnica f = fichaService.aprobadaDe(p.getProducto());
            Medidas medidas = null;
            if (f != null && p.getLargo() != null && p.getLargo() > 0 && !Boolean.TRUE.equals(f.getCompuesta())
                    && !p.getLargo().equals(f.getLargo())) {
                medidas = new Medidas(p.getLargo(), p.getFondo() == null ? f.getFondo() : p.getFondo(),
                        p.getAlto() == null ? f.getAlto() : p.getAlto(), f.getCuerpos());
            }
            op = produccionService.crear(new ProduccionService.NuevaOP(p.getProducto().getId(), null, p.getCantidad(),
                    p.getCliente().getId(), responsableId, Prioridad.MEDIA, AppClock.hoy(), p.getFechaEntrega(),
                    p.getTela() == null ? null : p.getTela().getId(), medidas, Map.of(), List.of(), p.getCodigo(), null,
                    p.getEspecificacion(), Etapa.PENDIENTE, null, null));
        }
        vincularOP(id, op.getCodigo());
        return op;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Pedido cancelar(Long id) {
        Pedido p = pedido(id);
        if (p.getEstado() == EstadoPedido.ENTREGADO) throw new NegocioException("No se puede cancelar un pedido entregado.");
        if (p.getOpCodigo() != null) {
            produccionService.porCodigo(p.getOpCodigo()).filter(OrdenProduccion::isActiva)
                    .ifPresent(op -> produccionService.anular(op.getId()));
            p = pedido(id);
        }
        p.setEstado(EstadoPedido.CANCELADO);
        return repository.save(p);
    }

    /** Mensaje de WhatsApp (simulado). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public String mensajeWhatsapp(Pedido p) {
        return "Hola " + p.getCliente().getNombre().split(" ")[0] + ", te saluda SISTEMA ERP SOFAS Sofás. "
                + "Tu pedido " + p.getCodigo() + " (" + p.getProductoNombre() + (p.getTela() == null ? "" : " en " + p.getTela().getNombre())
                + ") está en estado: " + p.getEstado().nombre() + ". Precio " + Money.fmt(p.getPrecio()) + ", a cuenta "
                + Money.fmt(p.getAdelanto()) + ", saldo " + Money.fmt(p.getSaldo())
                + (p.getFechaEntrega() == null ? "" : ". Entrega estimada: " + AppClock.fmt(p.getFechaEntrega())) + ". ¡Gracias!";
    }
}
