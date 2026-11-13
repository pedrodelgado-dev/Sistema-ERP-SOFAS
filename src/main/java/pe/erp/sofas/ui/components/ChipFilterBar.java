package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;

import java.util.List;

/** Fila de chips de filtro con contador; siempre hay uno seleccionado. */
@EquipoPLMD_MGML_AMCS_DRJB
public class ChipFilterBar extends FlowPane {

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Chip(@EquipoPLMD_MGML_AMCS_DRJB
      String clave, @EquipoPLMD_MGML_AMCS_DRJB
        String texto, @EquipoPLMD_MGML_AMCS_DRJB
          long conteo) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ToggleGroup grupo = new ToggleGroup();
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ObjectProperty<String> seleccion = new SimpleObjectProperty<>();
    @EquipoPLMD_MGML_AMCS_DRJB
    private boolean reconstruyendo;

    @EquipoPLMD_MGML_AMCS_DRJB
    public ChipFilterBar() {
        super(8, 8);
        getStyleClass().add("chip-bar");
        setPrefWrapLength(1150);
        setMinWidth(0);
        setMinHeight(USE_PREF_SIZE);
        grupo.selectedToggleProperty().addListener((o, a, n) -> {
            if (reconstruyendo) return;
            if (n == null) {
                if (a != null) grupo.selectToggle(a);
                return;
            }
            seleccion.set((String) n.getUserData());
        });
    }

    /** Reemplaza los chips conservando la seleccion actual si sigue existiendo. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public void setChips(List<Chip> chips) {
        String actual = seleccion.get();
        reconstruyendo = true;
        getChildren().clear();
        grupo.getToggles().clear();
        ToggleButton aSeleccionar = null;
        for (Chip c : chips) {
            ToggleButton t = new ToggleButton(c.texto());
            t.setUserData(c.clave());
            t.setToggleGroup(grupo);
            t.getStyleClass().add("chip");
            t.setMnemonicParsing(false);
            if (c.conteo() >= 0) {
                Label n = UI.label(String.valueOf(c.conteo()), "chip-count");
                t.setGraphic(n);
                t.setContentDisplay(javafx.scene.control.ContentDisplay.RIGHT);
            }
            getChildren().add(t);
            if (aSeleccionar == null || c.clave().equals(actual)) aSeleccionar = t;
        }
        if (aSeleccionar != null) grupo.selectToggle(aSeleccionar);
        reconstruyendo = false;
        // Solo notifica si la clave seleccionada cambio (evita recursion cuando se llama desde un render).
        if (aSeleccionar != null) seleccion.set((String) aSeleccionar.getUserData());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ObjectProperty<String> seleccionProperty() {
        return seleccion;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getSeleccion() {
        return seleccion.get();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void seleccionar(String clave) {
        grupo.getToggles().stream().filter(t -> clave.equals(t.getUserData())).findFirst().ifPresent(grupo::selectToggle);
    }
}
