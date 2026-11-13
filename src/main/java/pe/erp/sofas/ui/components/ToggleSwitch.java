package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Interruptor on/off (basado en el ToggleSwitch de AtlantaFX, estilizado con los tokens del tema). */
@EquipoPLMD_MGML_AMCS_DRJB
public class ToggleSwitch extends atlantafx.base.controls.ToggleSwitch {

    @EquipoPLMD_MGML_AMCS_DRJB
    public ToggleSwitch(boolean seleccionado) {
        setSelected(seleccionado);
        getStyleClass().add("is-switch");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ToggleSwitch(String texto, boolean seleccionado) {
        super(texto);
        setSelected(seleccionado);
        getStyleClass().add("is-switch");
    }
}
