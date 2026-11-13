package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/** Formulario en rejilla: etiquetas arriba de cada control, secciones numeradas y mensaje de error. */
@EquipoPLMD_MGML_AMCS_DRJB
public class FormGrid extends VBox {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final GridPane grid = new GridPane();
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Label error = UI.label("", "form-error");
    @EquipoPLMD_MGML_AMCS_DRJB
    private final int columnas;
    @EquipoPLMD_MGML_AMCS_DRJB
    private int col;
    @EquipoPLMD_MGML_AMCS_DRJB
    private int fila;

    @EquipoPLMD_MGML_AMCS_DRJB
    public FormGrid(int columnas) {
        super(14);
        this.columnas = columnas;
        grid.setHgap(14);
        grid.setVgap(14);
        for (int i = 0; i < columnas; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / columnas);
            cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);
        }
        error.setGraphic(Ico.of("ERROR", 16));
        error.setWrapText(true);
        error.setMaxWidth(Double.MAX_VALUE);
        error.setVisible(false);
        error.setManaged(false);
        getChildren().addAll(error, grid);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public FormGrid campo(String etiqueta, Node control) {
        return campo(etiqueta, control, 1);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public FormGrid campo(String etiqueta, Node control, int span) {
        return nodo(UI.field(etiqueta, control), span);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public FormGrid nodo(Node n, int span) {
        if (col + span > columnas) {
            col = 0;
            fila++;
        }
        grid.add(n, col, fila, span, 1);
        if (n instanceof Region r) r.setMaxWidth(Double.MAX_VALUE);
        col += span;
        if (col >= columnas) {
            col = 0;
            fila++;
        }
        return this;
    }

    /** Cabecera de seccion a todo el ancho: "1  Cliente". */
    @EquipoPLMD_MGML_AMCS_DRJB
    public FormGrid seccion(String numero, String titulo) {
        if (col != 0) {
            col = 0;
            fila++;
        }
        Label num = UI.label(numero, "form-section-num");
        var h = UI.hbox(10, num, UI.label(titulo, "form-section-title"));
        if (fila > 0) h.setStyle("-fx-padding: 8 0 0 0;");
        grid.add(h, 0, fila++, columnas, 1);
        return this;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void error(String mensaje) {
        error.setText(mensaje);
        error.setVisible(true);
        error.setManaged(true);
        UI.entrar(error);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void limpiarError() {
        error.setVisible(false);
        error.setManaged(false);
    }
}
