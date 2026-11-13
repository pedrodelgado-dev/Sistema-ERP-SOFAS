package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pe.erp.sofas.ui.components.Ico;
import pe.erp.sofas.ui.components.UI;

/** Modal centrado con icono, titulo, texto y botones "Cancelar" y la accion. Enter confirma, Esc cancela. */
@EquipoPLMD_MGML_AMCS_DRJB
@Component
@Lazy
@RequiredArgsConstructor
public class ConfirmDialog {

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Tipo { @EquipoPLMD_MGML_AMCS_DRJB
        PELIGRO, @EquipoPLMD_MGML_AMCS_DRJB
         AVISO, @EquipoPLMD_MGML_AMCS_DRJB
          INFO }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final Overlays overlays;

    @EquipoPLMD_MGML_AMCS_DRJB
    public void mostrar(String titulo, String mensaje, String accion, Runnable alConfirmar) {
        mostrar(Tipo.PELIGRO, titulo, mensaje, accion, alConfirmar);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void mostrar(Tipo tipo, String titulo, String mensaje, String accion, Runnable alConfirmar) {
        String icono = switch (tipo) {
            case PELIGRO -> "WARNING";
            case AVISO -> "REPORT_PROBLEM";
            case INFO -> "HELP_OUTLINE";
        };
        StackPane ic = new StackPane(Ico.of(icono, 26));
        ic.getStyleClass().addAll("confirm-icon", "confirm-" + tipo.name().toLowerCase());
        VBox cuerpo = new VBox(12, ic, UI.label(titulo, "confirm-title"), UI.wrap(mensaje, "confirm-text"));
        cuerpo.setAlignment(Pos.CENTER);
        cuerpo.getStyleClass().add("confirm-body");

        Button cancelar = UI.secondary("Cancelar", null);
        Button ok = tipo == Tipo.PELIGRO ? UI.danger(accion, null) : UI.primary(accion, null);
        ok.setDefaultButton(true);
        Overlays.Handle h = overlays.modal(null, null, cuerpo, 440, cancelar, ok);
        cancelar.setOnAction(e -> h.cerrar());
        ok.setOnAction(e -> {
            h.cerrar();
            alConfirmar.run();
        });
        cancelar.setCancelButton(true);
        javafx.application.Platform.runLater(ok::requestFocus);
    }
}
