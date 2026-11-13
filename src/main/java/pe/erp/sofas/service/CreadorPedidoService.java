package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.CarritoRepository;
import pe.erp.sofas.repository.PedidoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
public class CreadorPedidoService {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final CarritoRepository carritoRepo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PedidoRepository pedidoRepo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final CalculoPrecioService precioService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final GestorCarritoService gestorCarrito;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Pedido> crearPedidosDesdeCarrito(Usuario usuario, Cliente cliente, boolean vaciarCarrito) {
        Carrito carrito = gestorCarrito.obtenerOCrearCarrito(usuario);

        if (carrito.getItems().isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        List<Pedido> pedidosCreados = new ArrayList<>();

        for (CarritoItem item : carrito.getItems()) {
            Pedido pedido = crearPedidoDesdeItem(item, cliente);
            Pedido pedidoGuardado = pedidoRepo.save(pedido);
            pedidosCreados.add(pedidoGuardado);
        }

        if (vaciarCarrito) {
            gestorCarrito.vaciarCarrito(usuario);
        }

        return pedidosCreados;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private Pedido crearPedidoDesdeItem(CarritoItem item, Cliente cliente) {
        ConfiguracionGuardada config = item.getConfiguracion();
        ConfigSofa sofaConfig = config.toConfigSofa();

        // Calcular precio
        BigDecimal precioUnitario = precioService.calcularPrecio(sofaConfig);
        BigDecimal precioTotal = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));

        // Generar código único
        String codigo = generarCodigoPedido();

        // Generar especificación legible
        String especificacion = generarEspecificacion(sofaConfig);

        // Generar configuración serializada
        String configSerialized = generarConfigSerialized(sofaConfig);

        Pedido pedido = Pedido.builder()
            .codigo(codigo)
            .cliente(cliente)
            .tipo(Pedido.Tipo.PERSONALIZADO)
            .cantidad(item.getCantidad())
            .fecha(LocalDate.now())
            .precio(precioTotal)
            .estado(EstadoPedido.CONFIRMADO)
            .largo(sofaConfig.largo())
            .fondo(sofaConfig.profundidad())
            .alto(sofaConfig.alto())
            .material(sofaConfig.espuma().nombre())
            .especificacion(especificacion)
            .configuracion(configSerialized)
            .observaciones(item.getNotas())
            .build();

        return pedido;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private String generarCodigoPedido() {
        // Formato: PED-YYYYMM-XXXXX (ej: PED-202610-A1B2C)
        LocalDate hoy = LocalDate.now();
        String fechaParte = String.format("%04d%02d", hoy.getYear(), hoy.getMonthValue());
        String aleatorioParte = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return "PED-" + fechaParte + "-" + aleatorioParte;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private String generarEspecificacion(ConfigSofa config) {
        return String.format(
            "%s | %d cuerpos | %s | %s | %s | %s",
            config.modelo().nombre(),
            config.cuerpos(),
            config.espuma().nombre(),
            config.brazo().nombre(),
            config.patas().nombre(),
            config.mecanismo().nombre()
        );
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private String generarConfigSerialized(ConfigSofa config) {
        return String.format(
            "modelo=%s;cuerpos=%d;chaise=%s;profundidad=%d;brazo=%s;patas=%s;" +
            "acabado=%s;espuma=%s;mecanismo=%s",
            config.modelo().name(),
            config.cuerpos(),
            config.chaise().name(),
            config.profundidad(),
            config.brazo().name(),
            config.patas().name(),
            config.acabado().name(),
            config.espuma().name(),
            config.mecanismo().name()
        );
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Pedido actualizarPedido(Long pedidoId, Cliente cliente, LocalDate fechaEntrega, String observaciones) {
        Pedido pedido = pedidoRepo.findById(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        if (cliente != null) {
            pedido.setCliente(cliente);
        }
        if (fechaEntrega != null) {
            pedido.setFechaEntrega(fechaEntrega);
        }
        if (observaciones != null) {
            pedido.setObservaciones(observaciones);
        }

        return pedidoRepo.save(pedido);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Pedido registrarAdelanto(Long pedidoId, BigDecimal monto) {
        Pedido pedido = pedidoRepo.findById(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        BigDecimal nuevoAdelanto = pedido.getAdelanto().add(monto);

        if (nuevoAdelanto.compareTo(pedido.getPrecio()) > 0) {
            throw new IllegalArgumentException("El adelanto no puede exceder el precio total");
        }

        pedido.setAdelanto(nuevoAdelanto);
        return pedidoRepo.save(pedido);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Pedido cambiarEstado(Long pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepo.findById(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        pedido.setEstado(nuevoEstado);
        return pedidoRepo.save(pedido);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record ResumenPedidos(
        @EquipoPLMD_MGML_AMCS_DRJB
        List<Pedido> pedidos,
        @EquipoPLMD_MGML_AMCS_DRJB
        Integer totalPedidos,
        @EquipoPLMD_MGML_AMCS_DRJB
        BigDecimal montoTotal,
        @EquipoPLMD_MGML_AMCS_DRJB
        Integer cantidadTotal
    ) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public static ResumenPedidos desde(List<Pedido> pedidos) {
            Integer totalPedidos = pedidos.size();
            BigDecimal montoTotal = pedidos.stream()
                .map(Pedido::getPrecio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            Integer cantidadTotal = pedidos.stream()
                .mapToInt(Pedido::getCantidad)
                .sum();

            return new ResumenPedidos(pedidos, totalPedidos, montoTotal, cantidadTotal);
        }
    }
}
