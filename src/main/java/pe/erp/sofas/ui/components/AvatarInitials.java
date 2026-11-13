package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/** Avatar circular con iniciales; el color lo define una variante de estilo o un color explicito. */
@EquipoPLMD_MGML_AMCS_DRJB
public class AvatarInitials extends StackPane {

    @EquipoPLMD_MGML_AMCS_DRJB
    public AvatarInitials(String nombre, double size, String variante) {
        Label l = new Label(iniciales(nombre));
        l.getStyleClass().add("avatar-text");
        l.setStyle("-fx-font-size: " + Math.round(size * 0.38) + "px;");
        getChildren().add(l);
        getStyleClass().addAll("avatar", "avatar-" + variante);
        setMinSize(size, size);
        setPrefSize(size, size);
        setMaxSize(size, size);
    }

    /** Avatar con color de fondo dado (p. ej. color del area del trabajador). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static AvatarInitials conColor(String nombre, double size, String color) {
        AvatarInitials a = new AvatarInitials(nombre, size, "custom");
        a.setStyle("-fx-background-color: " + color + ";");
        return a;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String iniciales(String nombre) {
        if (nombre == null || nombre.isBlank()) return "?";
        String[] p = nombre.replaceAll("[^\\p{L} ]", " ").trim().split("\\s+");
        if (p.length == 1) return p[0].substring(0, Math.min(2, p[0].length())).toUpperCase();
        return ("" + p[0].charAt(0) + p[1].charAt(0)).toUpperCase();
    }
}
