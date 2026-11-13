package pe.erp.sofas.ui.views.constructor;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.junit.jupiter.api.Test;
import pe.erp.sofas.core.Session;
import pe.erp.sofas.core.Toast;
import pe.erp.sofas.service.ConfiguradorService;
import pe.erp.sofas.service.GestorCarritoService;
import pe.erp.sofas.service.PersistenciaConfiguracionService;
import pe.erp.sofas.service.TelaService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@EquipoPLMD_MGML_AMCS_DRJB
class ConstructorModularControllerTest {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testConstructorCreation() {
        ConfiguradorService configuradorMock = mock(ConfiguradorService.class);
        TelaService telasMock = mock(TelaService.class);
        Toast toastMock = mock(Toast.class);
        GestorCarritoService gestorMock = mock(GestorCarritoService.class);
        PersistenciaConfiguracionService persistenciaMock = mock(PersistenciaConfiguracionService.class);
        Session sessionMock = mock(Session.class);

        ConstructorModularController controller = new ConstructorModularController(
            configuradorMock, telasMock, toastMock, gestorMock, persistenciaMock, sessionMock
        );
        assertNotNull(controller);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testConstructorWithMocks() {
        ConfiguradorService configuradorMock = mock(ConfiguradorService.class);
        TelaService telasMock = mock(TelaService.class);
        Toast toastMock = mock(Toast.class);
        GestorCarritoService gestorMock = mock(GestorCarritoService.class);
        PersistenciaConfiguracionService persistenciaMock = mock(PersistenciaConfiguracionService.class);
        Session sessionMock = mock(Session.class);

        ConstructorModularController controller = new ConstructorModularController(
            configuradorMock, telasMock, toastMock, gestorMock, persistenciaMock, sessionMock
        );

        assertNotNull(controller);
        verify(configuradorMock, never()).defecto(any());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testConstructorNotNull() {
        ConfiguradorService configuradorMock = mock(ConfiguradorService.class);
        TelaService telasMock = mock(TelaService.class);
        Toast toastMock = mock(Toast.class);
        GestorCarritoService gestorMock = mock(GestorCarritoService.class);
        PersistenciaConfiguracionService persistenciaMock = mock(PersistenciaConfiguracionService.class);
        Session sessionMock = mock(Session.class);

        ConstructorModularController controller = new ConstructorModularController(
            configuradorMock, telasMock, toastMock, gestorMock, persistenciaMock, sessionMock
        );

        assertTrue(controller != null);
    }
}
