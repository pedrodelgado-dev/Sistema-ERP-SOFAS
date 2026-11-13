package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/** Graficos nativos de JavaFX configurados con el estilo del ERP (los colores vienen del CSS). */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Charts {

    @EquipoPLMD_MGML_AMCS_DRJB
    private Charts() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Serie(@EquipoPLMD_MGML_AMCS_DRJB
      String nombre, @EquipoPLMD_MGML_AMCS_DRJB
        List<String> categorias, @EquipoPLMD_MGML_AMCS_DRJB
          List<Double> valores) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static NumberAxis ejeY(Function<Double, String> formato) {
        NumberAxis y = new NumberAxis();
        y.setForceZeroInRange(true);
        y.setMinorTickVisible(false);
        y.setTickLabelFormatter(new StringConverter<>() {
            @EquipoPLMD_MGML_AMCS_DRJB
            @Override
            public String toString(Number n) {
                return formato.apply(n.doubleValue());
            }

            @EquipoPLMD_MGML_AMCS_DRJB
            @Override
            public Number fromString(String s) {
                return 0;
            }
        });
        return y;
    }

    /**
     * Barras verticales con tooltip; {@code alClic} recibe categoria y valor (la barra queda resaltada).
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static BarChart<String, Number> barras(List<Serie> series, Function<Double, String> formato,
                                                  BiConsumer<String, Double> alClic) {
        CategoryAxis x = new CategoryAxis();
        x.setTickMarkVisible(false);
        BarChart<String, Number> chart = new BarChart<>(x, ejeY(formato));
        chart.setAnimated(false);
        chart.setLegendVisible(series.size() > 1);
        chart.setVerticalGridLinesVisible(false);
        chart.setAlternativeRowFillVisible(false);
        chart.setBarGap(3);
        chart.setCategoryGap(series.size() > 1 ? 14 : 18);
        for (Serie s : series) {
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName(s.nombre());
            for (int i = 0; i < s.categorias().size(); i++) {
                serie.getData().add(new XYChart.Data<>(s.categorias().get(i), s.valores().get(i)));
            }
            chart.getData().add(serie);
        }
        for (XYChart.Series<String, Number> serie : chart.getData()) {
            for (XYChart.Data<String, Number> d : serie.getData()) {
                Node n = d.getNode();
                if (n == null) continue;
                Tooltip t = new Tooltip(serie.getName() + " · " + d.getXValue() + "\n" + formato.apply(d.getYValue().doubleValue()));
                t.setShowDelay(Duration.millis(80));
                Tooltip.install(n, t);
                n.setOnMouseEntered(e -> n.getStyleClass().add("bar-hover"));
                n.setOnMouseExited(e -> n.getStyleClass().remove("bar-hover"));
                if (alClic != null) {
                    n.setStyle("-fx-cursor: hand;");
                    n.setOnMouseClicked(e -> {
                        chart.getData().forEach(sr -> sr.getData().forEach(dd -> {
                            if (dd.getNode() != null) dd.getNode().getStyleClass().remove("bar-selected");
                        }));
                        n.getStyleClass().add("bar-selected");
                        alClic.accept(d.getXValue(), d.getYValue().doubleValue());
                    });
                }
            }
        }
        return chart;
    }

    /** Lineas (o area si {@code area} es true) con simbolos y tooltip por punto. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static XYChart<String, Number> lineas(List<Serie> series, Function<Double, String> formato, boolean area) {
        CategoryAxis x = new CategoryAxis();
        x.setTickMarkVisible(false);
        XYChart<String, Number> chart = area ? new AreaChart<>(x, ejeY(formato)) : new LineChart<>(x, ejeY(formato));
        chart.setAnimated(false);
        chart.setLegendVisible(series.size() > 1);
        chart.setVerticalGridLinesVisible(false);
        chart.setAlternativeRowFillVisible(false);
        if (chart instanceof LineChart<String, Number> lc) lc.setCreateSymbols(true);
        if (chart instanceof AreaChart<String, Number> ac) ac.setCreateSymbols(true);
        for (Serie s : series) {
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName(s.nombre());
            for (int i = 0; i < s.categorias().size(); i++) {
                serie.getData().add(new XYChart.Data<>(s.categorias().get(i), s.valores().get(i)));
            }
            chart.getData().add(serie);
        }
        for (XYChart.Series<String, Number> serie : chart.getData()) {
            for (XYChart.Data<String, Number> d : serie.getData()) {
                if (d.getNode() == null) continue;
                Tooltip t = new Tooltip(serie.getName() + " · " + d.getXValue() + "\n" + formato.apply(d.getYValue().doubleValue()));
                t.setShowDelay(Duration.millis(60));
                Tooltip.install(d.getNode(), t);
            }
        }
        return chart;
    }

    /** Barra horizontal etiquetada (estado por etapa, rentabilidad por categoria...). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static HBox barraHorizontal(String etiqueta, double valor, double maximo, String color, String texto) {
        var lbl = UI.label(etiqueta, "hbar-label");
        lbl.setMinWidth(130);
        lbl.setPrefWidth(130);
        ProgressBarMini barra = new ProgressBarMini(maximo <= 0 ? 0 : valor / maximo, color, 10);
        HBox.setHgrow(barra, Priority.ALWAYS);
        barra.setMaxWidth(Double.MAX_VALUE);
        var val = UI.label(texto, "hbar-value");
        val.setMinWidth(Region.USE_PREF_SIZE);
        val.setPrefWidth(56);
        val.setAlignment(Pos.CENTER_RIGHT);
        HBox h = new HBox(12, lbl, barra, val);
        h.setAlignment(Pos.CENTER_LEFT);
        return h;
    }

    /** Barras apiladas horizontales de dos tramos (completadas + en curso). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static VBox apilada(String etiqueta, String detalle, double a, double b, double maximo, String colorA, String colorB) {
        HBox barra = new HBox();
        barra.setMinHeight(14);
        barra.setPrefHeight(14);
        barra.getStyleClass().add("progress-track");
        barra.setStyle("-fx-background-radius: 7;");
        Region ra = new Region();
        ra.setStyle("-fx-background-color: " + colorA + "; -fx-background-radius: 7 0 0 7;");
        Region rb = new Region();
        rb.setStyle("-fx-background-color: " + colorB + "; -fx-background-radius: 0 7 7 0;");
        barra.getChildren().addAll(ra, rb);
        barra.widthProperty().addListener((o, x, w) -> {
            double total = Math.max(maximo, 1);
            ra.setPrefWidth(w.doubleValue() * a / total);
            ra.setMinWidth(w.doubleValue() * a / total);
            rb.setPrefWidth(w.doubleValue() * b / total);
            rb.setMinWidth(w.doubleValue() * b / total);
        });
        HBox cab = UI.hbox(8, UI.label(etiqueta, "cell-title"), UI.spacer(), UI.label(detalle, "cell-sub"));
        return new VBox(6, cab, barra);
    }
}
