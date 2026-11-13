package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pe.erp.sofas.ui.components.UI;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Capa de modales y paneles laterales sobre la ventana principal.
 * Esc cierra el de arriba; clic fuera cierra los paneles laterales.
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Component
@Lazy
public class Overlays {

    /** Referencia a un modal o panel abierto. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public final class Handle {
        @EquipoPLMD_MGML_AMCS_DRJB
        private final StackPane capa;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final Region caja;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final boolean panel;
        @EquipoPLMD_MGML_AMCS_DRJB
        private Runnable alCerrar;

        @EquipoPLMD_MGML_AMCS_DRJB
        private Handle(StackPane capa, Region caja, boolean panel) {
            this.capa = capa;
            this.caja = caja;
            this.panel = panel;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public void cerrar() {
            if (!abiertos.remove(this)) return;
            FadeTransition f = new FadeTransition(Duration.millis(160), capa);
            f.setToValue(0);
            f.setOnFinished(e -> raiz.getChildren().remove(capa));
            f.play();
            if (alCerrar != null) alCerrar.run();
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public Handle alCerrar(Runnable r) {
            this.alCerrar = r;
            return this;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public Region caja() {
            return caja;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private StackPane raiz;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Deque<Handle> abiertos = new ArrayDeque<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    public void registrar(StackPane raiz) {
        this.raiz = raiz;
        raiz.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE && !abiertos.isEmpty()) {
                abiertos.peek().cerrar();
                e.consume();
            }
        });
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean hayAbiertos() {
        return !abiertos.isEmpty();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void cerrarTodos() {
        while (!abiertos.isEmpty()) abiertos.peek().cerrar();
    }

    /**
     * Modal centrado: cabecera (titulo, subtitulo, cerrar), cuerpo desplazable y pie con botones.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    public Handle modal(String titulo, String subtitulo, Node cuerpo, double ancho, Node... pie) {
        VBox caja = new VBox();
        caja.getStyleClass().add("modal");
        caja.setMaxWidth(ancho);
        caja.setPrefWidth(ancho);
        caja.setMaxHeight(Region.USE_PREF_SIZE);

        Handle[] ref = new Handle[1];
        if (titulo != null) {
            VBox textos = new VBox(3, UI.label(titulo, "modal-title"));
            if (subtitulo != null) textos.getChildren().add(UI.label(subtitulo, "modal-subtitle"));
            Button x = UI.iconButton("CLOSE", "Cerrar (Esc)", "btn-ghost");
            x.setOnAction(e -> ref[0].cerrar());
            HBox header = UI.hbox(12, textos, UI.spacer(), x);
            header.getStyleClass().add("modal-header");
            caja.getChildren().add(header);
        }
        ScrollPane sp = new ScrollPane(cuerpo);
        sp.setFitToWidth(true);
        sp.getStyleClass().add("modal-scroll");
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox body = new VBox(sp);
        body.getStyleClass().add("modal-body");
        VBox.setVgrow(sp, Priority.ALWAYS);
        caja.getChildren().add(body);
        if (pie.length > 0) {
            HBox footer = UI.hbox(10);
            footer.setAlignment(Pos.CENTER_RIGHT);
            footer.getChildren().addAll(pie);
            footer.getStyleClass().add("modal-footer");
            caja.getChildren().add(footer);
        }
        // El cuerpo crece hasta el 86 % del alto disponible y luego desplaza.
        sp.prefViewportHeightProperty().bind(javafx.beans.binding.Bindings.createDoubleBinding(
                () -> Math.min(Math.max(cuerpo.prefHeight(ancho - 48), cuerpo.getLayoutBounds().getHeight()),
                        raiz.getHeight() * 0.86 - 150), raiz.heightProperty(), cuerpo.layoutBoundsProperty()));

        StackPane capa = new StackPane(caja);
        capa.getStyleClass().add("overlay-backdrop");
        capa.setPadding(new Insets(24));
        StackPane.setAlignment(caja, Pos.CENTER);
        ref[0] = abrir(capa, caja, false);

        caja.setScaleX(0.97);
        caja.setScaleY(0.97);
        caja.setTranslateY(8);
        ScaleTransition s = new ScaleTransition(Duration.millis(220), caja);
        s.setToX(1);
        s.setToY(1);
        TranslateTransition t = new TranslateTransition(Duration.millis(220), caja);
        t.setToY(0);
        new ParallelTransition(s, t).play();
        return ref[0];
    }

    /** Modal de espera con indicador de progreso (tareas en segundo plano). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public Handle cargando(String mensaje) {
        javafx.scene.control.ProgressIndicator pi = new javafx.scene.control.ProgressIndicator();
        pi.setPrefSize(36, 36);
        VBox v = new VBox(14, pi, UI.label(mensaje, "card-title"));
        v.setAlignment(Pos.CENTER);
        v.setPadding(new Insets(18, 10, 10, 10));
        return modal(null, null, v, 320);
    }

    /** Panel lateral derecho (drawer) de ancho fijo; clic fuera lo cierra. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public Handle panel(Node contenido, double ancho) {
        VBox caja = new VBox(contenido);
        VBox.setVgrow(contenido, Priority.ALWAYS);
        caja.getStyleClass().add("side-panel");
        caja.setPrefWidth(ancho);
        caja.setMaxWidth(ancho);
        caja.setMaxHeight(Double.MAX_VALUE);

        StackPane capa = new StackPane(caja);
        capa.getStyleClass().add("overlay-backdrop");
        StackPane.setAlignment(caja, Pos.CENTER_RIGHT);
        Handle h = abrir(capa, caja, true);
        capa.setOnMousePressed(e -> {
            if (e.getTarget() == capa) h.cerrar();
        });
        caja.setTranslateX(ancho);
        TranslateTransition t = new TranslateTransition(Duration.millis(260), caja);
        t.setToX(0);
        t.setInterpolator(Interpolator.SPLINE(0.2, 0.8, 0.2, 1));
        t.play();
        return h;
    }

    /** Panel lateral con cabecera (titulo, subtitulo, cerrar), cuerpo desplazable y pie opcional. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public Handle panel(String titulo, String subtitulo, Node cuerpo, HBox pie, double ancho) {
        VBox textos = new VBox(3, UI.label(titulo, "modal-title"));
        if (subtitulo != null) textos.getChildren().add(UI.label(subtitulo, "modal-subtitle"));
        Button x = UI.iconButton("CLOSE", "Cerrar (Esc)", "btn-ghost");
        HBox cabecera = UI.hbox(12, textos, UI.spacer(), x);
        cabecera.getStyleClass().add("panel-header");
        cuerpo.getStyleClass().add("panel-body");
        Handle h = panel(panelLayout(cabecera, cuerpo, pie), ancho);
        x.setOnAction(e -> h.cerrar());
        return h;
    }

    /** Envuelve el contenido de un panel lateral: cabecera fija, cuerpo con scroll y pie. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static VBox panelLayout(Node cabecera, Node cuerpo, Node pie) {
        ScrollPane sp = new ScrollPane(cuerpo);
        sp.setFitToWidth(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.getStyleClass().add("modal-scroll");
        VBox.setVgrow(sp, Priority.ALWAYS);
        VBox v = new VBox();
        if (cabecera != null) v.getChildren().add(cabecera);
        v.getChildren().add(sp);
        if (pie != null) {
            pie.getStyleClass().add("panel-footer");
            v.getChildren().add(pie);
        }
        return v;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private Handle abrir(StackPane capa, Region caja, boolean panel) {
        Handle h = new Handle(capa, caja, panel);
        capa.setMinSize(0, 0);
        capa.setOpacity(0);
        raiz.getChildren().add(capa);
        abiertos.push(h);
        FadeTransition f = new FadeTransition(Duration.millis(180), capa);
        f.setToValue(1);
        f.play();
        capa.requestFocus();
        return h;
    }
}
