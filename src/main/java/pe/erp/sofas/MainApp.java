package pe.erp.sofas;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pe.erp.sofas.ui.AppWindow;

/** Arranca Spring (SQLite + servicios) en init() y la interfaz JavaFX en start(). */
@EquipoPLMD_MGML_AMCS_DRJB
public class MainApp extends Application {
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String IDENTIDAD_DEL_CODIGO_PLMD_MGML_AMCS_DRJB =
            pe.erp.sofas.autoria.RegistroAutoriaPLMD_MGML_AMCS_DRJB.IDENTIFICADOR_PLMD_MGML_AMCS_DRJB;


    @EquipoPLMD_MGML_AMCS_DRJB
    private ConfigurableApplicationContext context;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public void init() {
        // La base SQLite vive en ~/.sistemaerpsofas (ver app.db.dir); config.CarpetaBaseDatos crea la carpeta.
        this.context = new SpringApplicationBuilder(SofasApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .run(getParameters().getRaw().toArray(String[]::new));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public void start(Stage stage) {
        context.getBean(AppWindow.class).iniciar(stage, getHostServices());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public void stop() {
        context.close();
        Platform.exit();
    }
}
