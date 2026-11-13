package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/** Esqueleto de carga que imita una fila de KPI, un grafico y una tabla. */
@EquipoPLMD_MGML_AMCS_DRJB
public class Skeleton extends VBox {

    @EquipoPLMD_MGML_AMCS_DRJB
    public Skeleton() {
        super(16);
        setPadding(new Insets(4, 0, 0, 0));
        HBox kpis = new HBox(16);
        for (int i = 0; i < 4; i++) kpis.getChildren().add(bloque(-1, 104));
        HBox medio = new HBox(16, bloque(-1, 260), bloque(360, 260));
        VBox filas = new VBox(10);
        for (int i = 0; i < 5; i++) filas.getChildren().add(bloque(-1, 22));
        filas.getStyleClass().add("skeleton-card");
        filas.setPadding(new Insets(18));
        getChildren().addAll(kpis, medio, filas);

        FadeTransition pulso = new FadeTransition(Duration.millis(700), this);
        pulso.setFromValue(1);
        pulso.setToValue(0.55);
        pulso.setAutoReverse(true);
        pulso.setCycleCount(Timeline.INDEFINITE);
        pulso.play();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static Region bloque(double ancho, double alto) {
        Region r = new Region();
        r.getStyleClass().add("skeleton-block");
        r.setMinHeight(alto);
        r.setPrefHeight(alto);
        if (ancho > 0) {
            r.setMinWidth(ancho);
            r.setPrefWidth(ancho);
        } else {
            HBox.setHgrow(r, Priority.ALWAYS);
            r.setMaxWidth(Double.MAX_VALUE);
        }
        return r;
    }
}
