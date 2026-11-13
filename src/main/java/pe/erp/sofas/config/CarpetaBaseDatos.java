package pe.erp.sofas.config;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;

/** Crea la carpeta de la base SQLite ({@code app.db.dir}) y la de imagenes antes de abrir la conexion. */
@EquipoPLMD_MGML_AMCS_DRJB
public class CarpetaBaseDatos implements EnvironmentPostProcessor {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication app) {
        String dir = env.getProperty("app.db.dir");
        if (dir != null && !dir.isBlank()) {
            new File(dir).mkdirs();
            new File(dir, "images").mkdirs();
        }
    }
}
