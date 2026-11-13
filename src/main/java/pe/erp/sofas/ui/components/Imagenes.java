package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.image.Image;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/** Carga imagenes por ruta ("classpath:/images/..." o archivo local) con cache. */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Imagenes {

    @EquipoPLMD_MGML_AMCS_DRJB
    private static final Map<String, Image> CACHE = new HashMap<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    private Imagenes() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Image cargar(String ruta) {
        if (ruta == null || ruta.isBlank()) return null;
        return CACHE.computeIfAbsent(ruta + "@" + new File(ruta).lastModified(), k -> {
            if (ruta.startsWith("classpath:")) {
                URL url = Imagenes.class.getResource(ruta.substring("classpath:".length()));
                return url == null ? null : new Image(url.toExternalForm(), 1400, 0, true, true, false);
            }
            File f = new File(ruta);
            return f.exists() ? new Image(f.toURI().toString(), 1400, 0, true, true, false) : null;
        });
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static void olvidar(String ruta) {
        CACHE.keySet().removeIf(k -> k.startsWith(ruta + "@"));
    }
}
