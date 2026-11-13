package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/** Control segmentado (Hoy | Semana | Mes, Kanban | Tabla | Calendario...). */
@EquipoPLMD_MGML_AMCS_DRJB
public class SegmentedControl extends HBox {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ToggleGroup grupo = new ToggleGroup();
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ObjectProperty<String> valor = new SimpleObjectProperty<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    public SegmentedControl(String... opciones) {
        getStyleClass().add("segmented");
        setMaxWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
        for (String op : opciones) {
            agregar(op, null);
        }
        grupo.selectedToggleProperty().addListener((o, a, n) -> {
            if (n == null) {
                grupo.selectToggle(a);
                return;
            }
            valor.set((String) n.getUserData());
        });
        if (!grupo.getToggles().isEmpty()) grupo.selectToggle(grupo.getToggles().get(0));
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
    }

    /** Agrega un segmento con icono opcional. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public SegmentedControl agregar(String texto, String icono) {
        ToggleButton t = new ToggleButton(texto);
        t.setUserData(texto);
        t.setToggleGroup(grupo);
        t.getStyleClass().add("segment");
        t.setMnemonicParsing(false);
        t.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
        if (icono != null) t.setGraphic(Ico.of(icono, 16));
        getChildren().add(t);
        return this;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static SegmentedControl conIconos(String[] textos, String[] iconos) {
        SegmentedControl s = new SegmentedControl();
        for (int i = 0; i < textos.length; i++) s.agregar(textos[i], iconos[i]);
        s.grupo.selectToggle(s.grupo.getToggles().get(0));
        return s;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ObjectProperty<String> valorProperty() {
        return valor;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getValor() {
        return valor.get();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void setValor(String v) {
        grupo.getToggles().stream().filter(t -> v.equals(t.getUserData())).findFirst().ifPresent(grupo::selectToggle);
    }
}
