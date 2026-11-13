package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/** Barra de progreso fina con color propio (etapas, stock, margen, % pagado). */
@EquipoPLMD_MGML_AMCS_DRJB
public class ProgressBarMini extends StackPane {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final Region relleno = new Region();
    @EquipoPLMD_MGML_AMCS_DRJB
    private double valor;

    @EquipoPLMD_MGML_AMCS_DRJB
    public ProgressBarMini(double valor, String color, double alto) {
        getStyleClass().add("progress-track");
        relleno.getStyleClass().add("progress-fill");
        setMinHeight(alto);
        setPrefHeight(alto);
        setMaxHeight(alto);
        relleno.setMaxHeight(alto);
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        getChildren().add(relleno);
        setStyle("-fx-background-radius: " + alto + ";");
        setColor(color);
        setValor(valor);
        widthProperty().addListener((o, a, n) -> ajustar());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void setColor(String color) {
        relleno.setStyle("-fx-background-color: " + color + "; -fx-background-radius: " + getPrefHeight() + ";");
    }

    /** Valor entre 0 y 1. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public void setValor(double v) {
        this.valor = Math.max(0, Math.min(1, v));
        ajustar();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private void ajustar() {
        double w = getWidth() * valor;
        relleno.setMinWidth(w);
        relleno.setPrefWidth(w);
        relleno.setMaxWidth(w);
    }
}
