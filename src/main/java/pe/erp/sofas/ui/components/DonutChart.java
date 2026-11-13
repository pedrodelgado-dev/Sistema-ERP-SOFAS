package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;

import java.util.ArrayList;
import java.util.List;

/** Dona con leyenda; al pasar el mouse por un segmento el centro muestra su nombre y porcentaje. */
@EquipoPLMD_MGML_AMCS_DRJB
public class DonutChart extends HBox {

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Segmento(@EquipoPLMD_MGML_AMCS_DRJB
      String nombre, @EquipoPLMD_MGML_AMCS_DRJB
        double valor, @EquipoPLMD_MGML_AMCS_DRJB
          String color) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final Label centroValor = UI.label("", "donut-value");
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Label centroTexto = UI.label("", "donut-caption");

    @EquipoPLMD_MGML_AMCS_DRJB
    public DonutChart(List<Segmento> segmentos, double diametro, String tituloCentro, String sufijo) {
        this(segmentos, diametro, tituloCentro, sufijo, false);
    }

    /** Con {@code vertical} la leyenda va debajo de la dona (columnas angostas). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public DonutChart(List<Segmento> segmentos, double diametro, String tituloCentro, String sufijo, boolean vertical) {
        super(22);
        setAlignment(Pos.CENTER_LEFT);
        double total = segmentos.stream().mapToDouble(Segmento::valor).sum();
        double grosor = diametro * 0.16;
        double r = (diametro - grosor) / 2;
        Pane lienzo = new Pane();
        lienzo.setMinSize(diametro, diametro);
        lienzo.setPrefSize(diametro, diametro);
        lienzo.setMaxSize(diametro, diametro);
        Circle fondo = new Circle(diametro / 2, diametro / 2, r);
        fondo.getStyleClass().add("donut-track");
        fondo.setStrokeWidth(grosor);
        lienzo.getChildren().add(fondo);

        Runnable restablecer = () -> {
            centroValor.setText(tituloCentro);
            centroTexto.setText(sufijo);
        };
        restablecer.run();

        List<HBox> leyendas = new ArrayList<>();
        double inicio = 90;
        for (Segmento s : segmentos) {
            double angulo = total == 0 ? 0 : s.valor() / total * 360;
            Arc arc = new Arc(diametro / 2, diametro / 2, r, r, inicio, -Math.max(0, angulo - 1.6));
            arc.setType(ArcType.OPEN);
            arc.setFill(null);
            arc.setStrokeWidth(grosor);
            arc.setStrokeLineCap(StrokeLineCap.BUTT);
            arc.setStyle("-fx-stroke: " + s.color() + ";");
            arc.getStyleClass().add("donut-arc");
            String pct = String.format("%.0f %%", total == 0 ? 0 : s.valor() * 100 / total);
            arc.setOnMouseEntered(e -> {
                arc.setStrokeWidth(grosor + 5);
                centroValor.setText(pct);
                centroTexto.setText(s.nombre());
            });
            arc.setOnMouseExited(e -> {
                arc.setStrokeWidth(grosor);
                restablecer.run();
            });
            lienzo.getChildren().add(arc);
            inicio -= angulo;

            javafx.scene.layout.Region punto = new javafx.scene.layout.Region();
            punto.getStyleClass().add("legend-dot");
            punto.setStyle("-fx-background-color: " + s.color() + ";");
            HBox fila = new HBox(8, punto, UI.label(s.nombre(), "legend-text"), UI.spacer(), UI.label(pct, "legend-value"));
            fila.setAlignment(Pos.CENTER_LEFT);
            fila.setOnMouseEntered(e -> arc.getOnMouseEntered().handle(null));
            fila.setOnMouseExited(e -> arc.getOnMouseExited().handle(null));
            leyendas.add(fila);
        }
        VBox centro = new VBox(0, centroValor, centroTexto);
        centro.setAlignment(Pos.CENTER);
        centro.setMouseTransparent(true);
        centro.setMaxWidth(r * 1.6);
        centroTexto.setWrapText(true);
        centroTexto.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        StackPane dona = new StackPane(lienzo, centro);
        dona.setMinSize(diametro, diametro);
        VBox leyenda = new VBox(9);
        leyenda.getChildren().addAll(leyendas);
        leyenda.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leyenda, javafx.scene.layout.Priority.ALWAYS);
        if (vertical) {
            VBox col = new VBox(14, dona, leyenda);
            col.setAlignment(Pos.TOP_CENTER);
            HBox.setHgrow(col, javafx.scene.layout.Priority.ALWAYS);
            leyenda.setMaxWidth(Double.MAX_VALUE);
            getChildren().add(col);
        } else {
            getChildren().addAll(dona, leyenda);
        }
    }
}
