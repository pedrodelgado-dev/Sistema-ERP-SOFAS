package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pe.erp.sofas.ui.components.Ico;
import pe.erp.sofas.ui.components.UI;

/** Notificaciones flotantes abajo a la derecha (4 s) con icono ok / info / warn / error. */
@EquipoPLMD_MGML_AMCS_DRJB
@Component
@Lazy
public class Toast {

    @EquipoPLMD_MGML_AMCS_DRJB
    private static final Duration DURACION = Duration.seconds(4);
    @EquipoPLMD_MGML_AMCS_DRJB
    private VBox zona;

    @EquipoPLMD_MGML_AMCS_DRJB
    public void registrar(VBox zona) {
        this.zona = zona;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void exito(String mensaje) {
        mostrar(mensaje, "success", "CHECK_CIRCLE");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void error(String mensaje) {
        mostrar(mensaje, "error", "ERROR");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void advertencia(String mensaje) {
        mostrar(mensaje, "warn", "WARNING");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void info(String mensaje) {
        mostrar(mensaje, "info", "INFO");
    }

    /** Ejecuta una accion de negocio; si falla muestra el mensaje como toast de error. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean intentar(Runnable accion) {
        try {
            accion.run();
            return true;
        } catch (NegocioException e) {
            error(e.getMessage());
        } catch (RuntimeException e) {
            Throwable causa = e;
            while (causa.getCause() != null && !(causa instanceof NegocioException)) causa = causa.getCause();
            if (causa instanceof NegocioException ne) {
                error(ne.getMessage());
            } else {
                org.slf4j.LoggerFactory.getLogger(Toast.class).error("Error inesperado", e);
                error("Ocurrió un error inesperado: " + e.getMessage());
            }
        }
        return false;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private void mostrar(String mensaje, String tipo, String icono) {
        if (zona == null) return;
        StackPane ic = new StackPane(Ico.of(icono, 18));
        ic.getStyleClass().add("toast-icon");
        Label texto = UI.wrap(mensaje, "toast-text");
        texto.setMaxWidth(300);
        var cerrar = UI.iconButton("CLOSE", "Cerrar", "btn-ghost", "btn-xs");
        HBox t = new HBox(12, ic, texto, UI.spacer(), cerrar);
        t.setAlignment(Pos.CENTER_LEFT);
        t.getStyleClass().addAll("toast", "toast-" + tipo);
        t.setMaxWidth(380);
        t.setPrefWidth(380);
        zona.getChildren().add(t);

        t.setOpacity(0);
        t.setTranslateX(24);
        FadeTransition in = new FadeTransition(Duration.millis(220), t);
        in.setToValue(1);
        TranslateTransition slide = new TranslateTransition(Duration.millis(220), t);
        slide.setToX(0);
        new ParallelTransition(in, slide).play();

        Runnable quitar = () -> {
            FadeTransition out = new FadeTransition(Duration.millis(200), t);
            out.setToValue(0);
            out.setOnFinished(e -> zona.getChildren().remove(t));
            out.play();
        };
        cerrar.setOnAction(e -> quitar.run());
        PauseTransition espera = new PauseTransition(DURACION);
        espera.setOnFinished(e -> quitar.run());
        espera.play();
        if (zona.getChildren().size() > 4) zona.getChildren().remove(0);
    }
}
