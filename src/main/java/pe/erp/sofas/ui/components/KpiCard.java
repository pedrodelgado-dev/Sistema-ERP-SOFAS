package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Tarjeta de indicador: icono en chip de color, etiqueta, valor grande y linea secundaria.
 * Variantes de color: blue, wood, green, amber, red, navy, teal, violet.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class KpiCard extends VBox {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final Label valor;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Label sub;

    @EquipoPLMD_MGML_AMCS_DRJB
    public KpiCard(String icono, String variante, String etiqueta, String valor, String sub) {
        super(10);
        getStyleClass().addAll("card", "kpi-card");
        StackPane chip = new StackPane(Ico.of(icono, 19));
        chip.getStyleClass().addAll("kpi-icon", "kpi-" + variante);
        Label lbl = UI.wrap(etiqueta, "kpi-label");
        HBox top = new HBox(10, lbl, UI.spacer(), chip);
        top.setAlignment(Pos.TOP_LEFT);
        this.valor = UI.label(valor, "kpi-value");
        this.sub = UI.label(sub == null ? "" : sub, "kpi-sub");
        getChildren().addAll(top, this.valor, this.sub);
        HBox.setHgrow(this, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMinWidth(150);
    }

    /** Sub-linea con tendencia (verde si sube, roja si baja). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public KpiCard tendencia(boolean positiva) {
        sub.getStyleClass().add(positiva ? "trend-up" : "trend-down");
        sub.setGraphic(Ico.of(positiva ? "TRENDING_UP" : "TRENDING_DOWN", 15));
        return this;
    }

    /** Borde superior de color (tarjetas de Finanzas). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public KpiCard bordeSuperior(String color) {
        setStyle("-fx-border-color: " + color + " transparent transparent transparent; -fx-border-width: 3 0 0 0;");
        return this;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public KpiCard alClic(Runnable accion) {
        UI.onClick(this, e -> accion.run());
        getStyleClass().add("kpi-clickable");
        return this;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void setValor(String v) {
        valor.setText(v);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void setSub(String s) {
        sub.setText(s);
    }
}
