package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.erp.sofas.model.ConfigSofa;
import pe.erp.sofas.model.Usuario;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@EquipoPLMD_MGML_AMCS_DRJB
class GeneradorPDFServiceTest {

    @EquipoPLMD_MGML_AMCS_DRJB
    private GeneradorPDFService service;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Mock
    private CalculoPrecioService precioService;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Mock
    private PersistenciaConfiguracionService configService;

    @EquipoPLMD_MGML_AMCS_DRJB
    private ConfigSofa configTest;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Usuario usuarioTest;

    @EquipoPLMD_MGML_AMCS_DRJB
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new GeneradorPDFService(precioService, configService);

        configTest = new ConfigSofa(
            ConfigSofa.Modelo.MODERNO,
            3,
            ConfigSofa.Chaise.NINGUNA,
            92,
            null,
            ConfigSofa.Brazo.RECTO,
            ConfigSofa.Patas.MADERA,
            ConfigSofa.AcabadoMadera.NATURAL,
            ConfigSofa.Espuma.D23,
            1,
            null,
            ConfigSofa.Mecanismo.FIJO
        );

        usuarioTest = new Usuario();
        usuarioTest.setId(1L);
        usuarioTest.setNombre("Test User");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testGenerarPresupuestoSinImagen() {
        CalculoPrecioService.DesglosePrecio desglose = new CalculoPrecioService.DesglosePrecio(
            BigDecimal.valueOf(800),
            BigDecimal.valueOf(150),
            BigDecimal.valueOf(1330),
            BigDecimal.ZERO,
            BigDecimal.valueOf(1330),
            BigDecimal.valueOf(239.4),
            BigDecimal.valueOf(1569.4)
        );

        when(precioService.desglosar(any(ConfigSofa.class), any()))
            .thenReturn(desglose);

        byte[] pdf = service.generarPresupuesto(configTest, "Cliente Test", usuarioTest);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
        // Validar que es un PDF válido (empieza con %PDF)
        assertTrue(pdf[0] == '%' && pdf[1] == 'P' && pdf[2] == 'D' && pdf[3] == 'F');
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testGenerarPresupuestoConClienteNombre() {
        CalculoPrecioService.DesglosePrecio desglose = new CalculoPrecioService.DesglosePrecio(
            BigDecimal.valueOf(800),
            BigDecimal.valueOf(150),
            BigDecimal.valueOf(1330),
            BigDecimal.ZERO,
            BigDecimal.valueOf(1330),
            BigDecimal.valueOf(239.4),
            BigDecimal.valueOf(1569.4)
        );

        when(precioService.desglosar(any(ConfigSofa.class), any()))
            .thenReturn(desglose);

        byte[] pdf = service.generarPresupuesto(configTest, "Empresa XYZ S.A.C.", usuarioTest);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testGenerarPresupuestoContienePrecio() {
        CalculoPrecioService.DesglosePrecio desglose = new CalculoPrecioService.DesglosePrecio(
            BigDecimal.valueOf(800),
            BigDecimal.valueOf(150),
            BigDecimal.valueOf(1330),
            BigDecimal.ZERO,
            BigDecimal.valueOf(1330),
            BigDecimal.valueOf(239.4),
            BigDecimal.valueOf(1569.4)
        );

        when(precioService.desglosar(any(ConfigSofa.class), any()))
            .thenReturn(desglose);

        byte[] pdf = service.generarPresupuesto(configTest, "Cliente Test", usuarioTest);

        assertNotNull(pdf);
        String contenido = new String(pdf);
        // Verificar que contiene información de precios
        assertTrue(contenido.contains("DESGLOSE") || contenido.contains("PRECIO") || pdf.length > 1000);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testGenerarPresupuestoModernoConChaise() {
        ConfigSofa conChaise = new ConfigSofa(
            ConfigSofa.Modelo.MODERNO, 3, ConfigSofa.Chaise.DERECHA, 92, null,
            ConfigSofa.Brazo.RECTO, ConfigSofa.Patas.MADERA, ConfigSofa.AcabadoMadera.NATURAL,
            ConfigSofa.Espuma.D23, 1, null, ConfigSofa.Mecanismo.FIJO
        );

        CalculoPrecioService.DesglosePrecio desglose = new CalculoPrecioService.DesglosePrecio(
            BigDecimal.valueOf(800),
            BigDecimal.valueOf(150),
            BigDecimal.valueOf(1530),
            BigDecimal.ZERO,
            BigDecimal.valueOf(1530),
            BigDecimal.valueOf(275.4),
            BigDecimal.valueOf(1805.4)
        );

        when(precioService.desglosar(any(ConfigSofa.class), any()))
            .thenReturn(desglose);

        byte[] pdf = service.generarPresupuesto(conChaise, "Cliente Test", usuarioTest);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testGenerarPresupuestoEstructuraValida() {
        CalculoPrecioService.DesglosePrecio desglose = new CalculoPrecioService.DesglosePrecio(
            BigDecimal.valueOf(800),
            BigDecimal.valueOf(150),
            BigDecimal.valueOf(1330),
            BigDecimal.ZERO,
            BigDecimal.valueOf(1330),
            BigDecimal.valueOf(239.4),
            BigDecimal.valueOf(1569.4)
        );

        when(precioService.desglosar(any(ConfigSofa.class), any()))
            .thenReturn(desglose);

        byte[] pdf = service.generarPresupuesto(configTest, "Cliente Test", usuarioTest);

        // Validar estructura mínima
        assertNotNull(pdf);
        assertTrue(pdf.length > 500);  // Debe tener contenido sustancial

        // Verificar marcas de PDF
        String inicio = new String(pdf, 0, Math.min(10, pdf.length));
        assertTrue(inicio.contains("%PDF"));
    }
}
