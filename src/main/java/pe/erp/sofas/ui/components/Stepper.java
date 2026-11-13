package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import pe.erp.sofas.model.Etapa;

import java.util.function.Consumer;

/** Stepper de las 11 etapas del taller con su color e icono; cada paso es clicable. */
@EquipoPLMD_MGML_AMCS_DRJB
public class Stepper extends HBox {

    @EquipoPLMD_MGML_AMCS_DRJB
    public Stepper(Etapa actual, Consumer<Etapa> alElegir) {
        getStyleClass().add("stepper");
        setAlignment(Pos.TOP_CENTER);
        Etapa[] etapas = Etapa.values();
        for (int i = 0; i < etapas.length; i++) {
            Etapa e = etapas[i];
            boolean hecho = e.ordinal() < actual.ordinal();
            boolean activo = e == actual;
            StackPane circulo = new StackPane(hecho ? Ico.of("CHECK", 13) : Ico.of(e.icono(), 13));
            circulo.getStyleClass().add("step-circle");
            if (hecho || activo) {
                circulo.setStyle("-fx-background-color: " + e.color() + "; -fx-border-color: " + e.color() + ";");
                circulo.getStyleClass().add("step-filled");
            }
            if (activo) circulo.getStyleClass().add("step-active");
            var nombre = UI.wrap(e.nombre(), "step-label");
            nombre.setAlignment(Pos.CENTER);
            nombre.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            if (activo) nombre.getStyleClass().add("step-label-active");
            VBox paso = new VBox(5, circulo, nombre);
            paso.setAlignment(Pos.TOP_CENTER);
            paso.setPrefWidth(56);
            paso.setMinWidth(44);
            Tooltip.install(paso, new Tooltip((i + 1) + ". " + e.nombre()));
            if (alElegir != null) UI.onClick(paso, ev -> alElegir.accept(e));
            getChildren().add(paso);
            if (i < etapas.length - 1) {
                Region linea = new Region();
                linea.getStyleClass().add("step-line");
                if (e.ordinal() < actual.ordinal()) linea.setStyle("-fx-background-color: " + etapas[i + 1].color() + ";");
                HBox.setHgrow(linea, Priority.ALWAYS);
                linea.setTranslateY(13);
                getChildren().add(linea);
            }
        }
    }
}
