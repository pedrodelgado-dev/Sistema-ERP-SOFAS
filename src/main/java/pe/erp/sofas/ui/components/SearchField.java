package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/** Buscador con icono de lupa y boton para limpiar. */
@EquipoPLMD_MGML_AMCS_DRJB
public class SearchField extends HBox {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final TextField campo = new TextField();

    @EquipoPLMD_MGML_AMCS_DRJB
    public SearchField(String prompt) {
        this(prompt, "SEARCH");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public SearchField(String prompt, String icono) {
        super(8);
        getStyleClass().add("search-field");
        campo.setPromptText(prompt);
        campo.getStyleClass().add("search-input");
        HBox.setHgrow(campo, Priority.ALWAYS);
        Button limpiar = UI.iconButton("CLOSE", "Limpiar búsqueda", "btn-ghost", "btn-xs");
        limpiar.visibleProperty().bind(campo.textProperty().isNotEmpty());
        limpiar.managedProperty().bind(limpiar.visibleProperty());
        limpiar.setOnAction(e -> {
            campo.clear();
            campo.requestFocus();
        });
        limpiar.setFocusTraversable(false);
        getChildren().addAll(Ico.of(icono, 18), campo, limpiar);
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        setOnMouseClicked(e -> campo.requestFocus());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public StringProperty textProperty() {
        return campo.textProperty();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getText() {
        return campo.getText() == null ? "" : campo.getText().trim();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void setText(String t) {
        campo.setText(t);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public TextField campo() {
        return campo;
    }
}
