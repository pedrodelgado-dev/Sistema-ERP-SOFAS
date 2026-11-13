package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.CarritoRepository;
import pe.erp.sofas.repository.ConfiguracionGuardadaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
public class GestorCarritoService {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final CarritoRepository carritoRepo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ConfiguracionGuardadaRepository configRepo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final CalculoPrecioService precioService;

    @EquipoPLMD_MGML_AMCS_DRJB
    public Carrito obtenerOCrearCarrito(Usuario usuario) {
        Optional<Carrito> existente = carritoRepo.findByUsuario(usuario);
        if (existente.isPresent()) {
            return existente.get();
        }

        Carrito nuevo = Carrito.builder()
            .usuario(usuario)
            .build();
        return carritoRepo.save(nuevo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Carrito agregarAlCarrito(Usuario usuario, Long configuracionId, Integer cantidad) {
        Carrito carrito = obtenerOCrearCarrito(usuario);

        ConfiguracionGuardada config = configRepo.findByIdAndUsuario(configuracionId, usuario)
            .orElseThrow(() -> new IllegalArgumentException("Configuración no encontrada"));

        // Verificar si ya existe en carrito
        Optional<CarritoItem> existente = carrito.getItems().stream()
            .filter(item -> item.getConfiguracion().getId().equals(configuracionId))
            .findFirst();

        if (existente.isPresent()) {
            existente.get().setCantidad(existente.get().getCantidad() + cantidad);
        } else {
            carrito.agregarItem(config, cantidad);
        }

        return carritoRepo.save(carrito);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Carrito removerDelCarrito(Usuario usuario, Long itemId) {
        Carrito carrito = obtenerOCrearCarrito(usuario);
        carrito.removerItem(itemId);
        return carritoRepo.save(carrito);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Carrito actualizarCantidad(Usuario usuario, Long itemId, Integer nuevaCantidad) {
        Carrito carrito = obtenerOCrearCarrito(usuario);

        if (nuevaCantidad <= 0) {
            carrito.removerItem(itemId);
        } else {
            carrito.actualizarCantidad(itemId, nuevaCantidad);
        }

        return carritoRepo.save(carrito);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Carrito vaciarCarrito(Usuario usuario) {
        Carrito carrito = obtenerOCrearCarrito(usuario);
        carrito.vaciar();
        return carritoRepo.save(carrito);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<CarritoItem> listarItems(Usuario usuario) {
        Carrito carrito = obtenerOCrearCarrito(usuario);
        return carrito.getItems();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public BigDecimal calcularTotal(Usuario usuario) {
        Carrito carrito = obtenerOCrearCarrito(usuario);
        BigDecimal total = BigDecimal.ZERO;

        for (CarritoItem item : carrito.getItems()) {
            ConfigSofa config = item.getConfiguracion().toConfigSofa();
            BigDecimal precioUnitario = precioService.calcularPrecio(config);
            total = total.add(precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        return total.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public DetalleCarrito obtenerDetalleCarrito(Usuario usuario) {
        Carrito carrito = obtenerOCrearCarrito(usuario);
        Integer cantidadItems = carrito.obtenerCantidadItems();
        Integer cantidadTotal = carrito.obtenerCantidadTotal();
        BigDecimal precioTotal = calcularTotal(usuario);

        return new DetalleCarrito(
            cantidadItems,
            cantidadTotal,
            precioTotal,
            carrito.getItems()
        );
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void eliminarCarrito(Usuario usuario) {
        carritoRepo.deleteByUsuario(usuario);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record DetalleCarrito(
        @EquipoPLMD_MGML_AMCS_DRJB
        Integer cantidadItems,
        @EquipoPLMD_MGML_AMCS_DRJB
        Integer cantidadTotal,
        @EquipoPLMD_MGML_AMCS_DRJB
        BigDecimal precioTotal,
        @EquipoPLMD_MGML_AMCS_DRJB
        List<CarritoItem> items
    ) {}
}
