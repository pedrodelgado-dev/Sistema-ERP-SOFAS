package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Validacion en linea de formularios: cada regla devuelve el mensaje de error (o null si es valido),
 * se evalua al editar y el boton de guardar queda deshabilitado mientras haya errores.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class Validacion {

    @EquipoPLMD_MGML_AMCS_DRJB
    private record Regla(@EquipoPLMD_MGML_AMCS_DRJB
      Node control, @EquipoPLMD_MGML_AMCS_DRJB
        Label error, @EquipoPLMD_MGML_AMCS_DRJB
          Supplier<String> mensaje) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final List<Regla> reglas = new ArrayList<>();
    @EquipoPLMD_MGML_AMCS_DRJB
    private final BooleanProperty valido = new SimpleBooleanProperty(true);
    @EquipoPLMD_MGML_AMCS_DRJB
    private boolean tocado;

    /** Campo con etiqueta arriba, control y mensaje de error debajo. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public VBox campo(String etiqueta, Node control, Supplier<String> regla) {
        Label error = UI.label("", "field-error");
        error.setVisible(false);
        error.setManaged(false);
        error.setWrapText(true);
        VBox v = new VBox(5, UI.label(etiqueta, "form-label"), control, error);
        if (control instanceof Region r) r.setMaxWidth(Double.MAX_VALUE);
        javafx.scene.layout.HBox.setHgrow(v, Priority.ALWAYS);
        if (regla != null) agregar(control, error, regla);
        return v;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Validacion regla(Node control, Supplier<String> regla) {
        agregar(control, null, regla);
        return this;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private void agregar(Node control, Label error, Supplier<String> regla) {
        reglas.add(new Regla(control, error, regla));
        Observable o = observable(control);
        if (o != null) o.addListener(x -> {
            tocado = true;
            evaluar();
        });
        evaluar();
    }

    /** Re-evalua cuando cambia otro valor del que depende una regla. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public void escuchar(Observable... dependencias) {
        for (Observable o : dependencias) o.addListener(x -> evaluar());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static Observable observable(Node c) {
        if (c instanceof TextInputControl t) return t.textProperty();
        if (c instanceof ComboBoxBase<?> cb) return cb.valueProperty();
        if (c instanceof ChoiceBox<?> ch) return ch.valueProperty();
        if (c instanceof CheckBox ck) return ck.selectedProperty();
        if (c instanceof Spinner<?> sp) return sp.valueProperty();
        return null;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean evaluar() {
        boolean ok = true;
        for (Regla r : reglas) {
            String msg;
            try {
                msg = r.mensaje().get();
            } catch (RuntimeException e) {
                msg = "Valor inválido.";
            }
            boolean error = msg != null;
            ok &= !error;
            boolean mostrar = error && (tocado || r.control().getProperties().containsKey("tocado"));
            r.control().getStyleClass().remove("field-invalid");
            if (mostrar) r.control().getStyleClass().add("field-invalid");
            if (r.error() != null) {
                r.error().setText(mostrar ? msg : "");
                r.error().setVisible(mostrar);
                r.error().setManaged(mostrar);
            }
        }
        valido.set(ok);
        return ok;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public BooleanProperty validoProperty() {
        return valido;
    }

    /** Deshabilita el boton mientras el formulario no sea valido. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public void enlazar(Button guardar) {
        guardar.disableProperty().bind(valido.not());
    }

    // ---------------------------------------------------------------- reglas comunes

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String requerido(TextInputControl t, String nombre) {
        return t.getText() == null || t.getText().isBlank() ? "Ingresa " + nombre + "." : null;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String requerido(ComboBoxBase<?> c, String nombre) {
        return c.getValue() == null ? "Elige " + nombre + "." : null;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String numero(TextInputControl t, double min, String nombre) {
        try {
            double v = Double.parseDouble(t.getText().replace(",", "").replace("S/", "").trim());
            return v < min ? nombre + " debe ser " + (min == 0 ? "cero o más" : "mayor o igual a " + pe.erp.sofas.core.Money.numero(min)) + "." : null;
        } catch (NumberFormatException | NullPointerException e) {
            return "Ingresa " + nombre.toLowerCase() + " válido.";
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static double valor(TextInputControl t) {
        try {
            return Double.parseDouble(t.getText().replace(",", "").replace("S/", "").trim());
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }
    }
}
