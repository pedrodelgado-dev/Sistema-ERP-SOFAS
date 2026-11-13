package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.CarritoRepository;
import pe.erp.sofas.repository.ConfiguracionGuardadaRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@EquipoPLMD_MGML_AMCS_DRJB
class GestorCarritoServiceTest {

    @EquipoPLMD_MGML_AMCS_DRJB
    private GestorCarritoService service;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Mock
    private CarritoRepository carritoRepo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Mock
    private ConfiguracionGuardadaRepository configRepo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Mock
    private CalculoPrecioService precioService;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Usuario usuarioTest;
    @EquipoPLMD_MGML_AMCS_DRJB
    private ConfiguracionGuardada configTest;

    @EquipoPLMD_MGML_AMCS_DRJB
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new GestorCarritoService(carritoRepo, configRepo, precioService);

        usuarioTest = new Usuario();
        usuarioTest.setId(1L);
        usuarioTest.setNombre("Test User");

        configTest = ConfiguracionGuardada.builder()
            .id(1L)
            .usuario(usuarioTest)
            .nombre("Mi Moderno")
            .modelo(ConfigSofa.Modelo.MODERNO)
            .cuerpos(3)
            .build();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testObtenerOCrearCarritoNuevo() {
        when(carritoRepo.findByUsuario(usuarioTest)).thenReturn(Optional.empty());
        when(carritoRepo.save(any(Carrito.class))).thenAnswer(inv -> {
            Carrito c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        Carrito carrito = service.obtenerOCrearCarrito(usuarioTest);

        assertNotNull(carrito);
        assertEquals(usuarioTest, carrito.getUsuario());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testAgregarAlCarrito() {
        Carrito carrito = Carrito.builder().usuario(usuarioTest).build();

        when(carritoRepo.findByUsuario(usuarioTest)).thenReturn(Optional.of(carrito));
        when(configRepo.findByIdAndUsuario(1L, usuarioTest)).thenReturn(Optional.of(configTest));
        when(carritoRepo.save(any(Carrito.class))).thenReturn(carrito);

        service.agregarAlCarrito(usuarioTest, 1L, 2);

        assertEquals(1, carrito.getItems().size());
        assertEquals(2, carrito.getItems().get(0).getCantidad());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testRemoverDelCarrito() {
        CarritoItem item = CarritoItem.builder().id(1L).configuracion(configTest).cantidad(2).build();
        List<CarritoItem> items = new ArrayList<>();
        items.add(item);
        Carrito carrito = Carrito.builder().usuario(usuarioTest).items(items).build();

        when(carritoRepo.findByUsuario(usuarioTest)).thenReturn(Optional.of(carrito));
        when(carritoRepo.save(any(Carrito.class))).thenReturn(carrito);

        service.removerDelCarrito(usuarioTest, 1L);

        assertTrue(carrito.getItems().isEmpty());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testActualizarCantidad() {
        CarritoItem item = CarritoItem.builder().id(1L).configuracion(configTest).cantidad(2).build();
        List<CarritoItem> items = new ArrayList<>();
        items.add(item);
        Carrito carrito = Carrito.builder().usuario(usuarioTest).items(items).build();

        when(carritoRepo.findByUsuario(usuarioTest)).thenReturn(Optional.of(carrito));
        when(carritoRepo.save(any(Carrito.class))).thenReturn(carrito);

        service.actualizarCantidad(usuarioTest, 1L, 5);

        assertEquals(5, carrito.getItems().get(0).getCantidad());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testCalcularTotal() {
        CarritoItem item = CarritoItem.builder().id(1L).configuracion(configTest).cantidad(2).build();
        List<CarritoItem> items = new ArrayList<>();
        items.add(item);
        Carrito carrito = Carrito.builder().usuario(usuarioTest).items(items).build();

        when(carritoRepo.findByUsuario(usuarioTest)).thenReturn(Optional.of(carrito));
        when(precioService.calcularPrecio(any(ConfigSofa.class))).thenReturn(BigDecimal.valueOf(1500));

        BigDecimal total = service.calcularTotal(usuarioTest);

        assertEquals(BigDecimal.valueOf(3000).setScale(2, java.math.RoundingMode.HALF_UP), total);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testObtenerDetalleCarrito() {
        CarritoItem item = CarritoItem.builder().id(1L).configuracion(configTest).cantidad(2).build();
        List<CarritoItem> items = new ArrayList<>();
        items.add(item);
        Carrito carrito = Carrito.builder().usuario(usuarioTest).items(items).build();

        when(carritoRepo.findByUsuario(usuarioTest)).thenReturn(Optional.of(carrito));
        when(precioService.calcularPrecio(any(ConfigSofa.class))).thenReturn(BigDecimal.valueOf(1500));

        GestorCarritoService.DetalleCarrito detalle = service.obtenerDetalleCarrito(usuarioTest);

        assertEquals(1, detalle.cantidadItems());
        assertEquals(2, detalle.cantidadTotal());
    }
}
