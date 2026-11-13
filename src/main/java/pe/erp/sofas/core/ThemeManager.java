package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Tema claro/oscuro en caliente: cambia la hoja de tokens de la escena (theme-light.css ↔ theme-dark.css)
 * y la hoja base de AtlantaFX. Tambien aplica la densidad de las tablas (Compacta/Normal/Amplia).
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Component
@Lazy
public class ThemeManager {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final List<String> DENSIDADES = List.of("Compacta", "Normal", "Amplia");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final BooleanProperty oscuro = new SimpleBooleanProperty(Prefs.bool(Prefs.TEMA_OSCURO, false));
    @EquipoPLMD_MGML_AMCS_DRJB
    private String densidad = Prefs.str(Prefs.DENSIDAD, "Normal");
    @EquipoPLMD_MGML_AMCS_DRJB
    private Scene scene;

    @EquipoPLMD_MGML_AMCS_DRJB
    public void aplicar(Scene scene) {
        this.scene = scene;
        refrescar();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public BooleanProperty oscuroProperty() {
        return oscuro;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isOscuro() {
        return oscuro.get();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void alternar() {
        setOscuro(!isOscuro());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void setOscuro(boolean valor) {
        oscuro.set(valor);
        Prefs.setBool(Prefs.TEMA_OSCURO, valor);
        refrescar();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getDensidad() {
        return densidad;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void setDensidad(String densidad) {
        this.densidad = densidad;
        Prefs.setStr(Prefs.DENSIDAD, densidad);
        refrescar();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private void refrescar() {
        Application.setUserAgentStylesheet(isOscuro()
                ? new PrimerDark().getUserAgentStylesheet()
                : new PrimerLight().getUserAgentStylesheet());
        if (scene == null) return;
        scene.getStylesheets().setAll(
                css("/css/base.css"),
                css(isOscuro() ? "/css/theme-dark.css" : "/css/theme-light.css"));
        if (scene.getRoot() != null) {
            var clases = scene.getRoot().getStyleClass();
            clases.removeAll("density-compacta", "density-normal", "density-amplia", "dark", "light");
            clases.add("density-" + densidad.toLowerCase());
            clases.add(isOscuro() ? "dark" : "light");
        }
    }

    /** Vuelve a aplicar clases a un nuevo nodo raiz (p. ej. al pasar del login al shell). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public void reaplicar() {
        refrescar();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private String css(String ruta) {
        return Objects.requireNonNull(getClass().getResource(ruta), ruta).toExternalForm();
    }
}
