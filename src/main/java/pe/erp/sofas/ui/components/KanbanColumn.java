package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pe.erp.sofas.model.Etapa;

import java.util.List;
import java.util.function.BiConsumer;

/** Columna del Kanban: acepta tarjetas soltadas y se resalta al arrastrar encima. */
@EquipoPLMD_MGML_AMCS_DRJB
public class KanbanColumn extends VBox {

    @EquipoPLMD_MGML_AMCS_DRJB
    public KanbanColumn(Etapa etapa, List<KanbanCard> tarjetas, int sofas, BiConsumer<Long, Etapa> alSoltar) {
        super(10);
        getStyleClass().add("kanban-column");
        setMinWidth(248);
        setPrefWidth(248);
        setMaxWidth(248);

        Region punto = new Region();
        punto.getStyleClass().add("legend-dot");
        punto.setStyle("-fx-background-color: " + etapa.color() + ";");
        var cabecera = UI.hbox(8, punto, Ico.of(etapa.icono(), 15), UI.label(etapa.nombre(), "kanban-title"), UI.spacer(),
                UI.label(String.valueOf(tarjetas.size()), "kanban-count"));
        cabecera.setPadding(new Insets(2, 4, 0, 4));
        var sub = UI.label(sofas + (sofas == 1 ? " sofá" : " sofás"), "cell-sub");
        sub.setPadding(new Insets(0, 4, 2, 22));

        VBox lista = new VBox(10);
        lista.getChildren().addAll(tarjetas);
        lista.setPadding(new Insets(2, 2, 8, 2));
        if (tarjetas.isEmpty()) {
            var vacio = UI.label("Arrastra una orden aquí", "kanban-empty");
            vacio.setMaxWidth(Double.MAX_VALUE);
            lista.getChildren().add(vacio);
        }
        ScrollPane sp = new ScrollPane(lista);
        sp.setFitToWidth(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.getStyleClass().add("kanban-scroll");
        VBox.setVgrow(sp, Priority.ALWAYS);
        getChildren().addAll(cabecera, sub, sp);

        setOnDragOver(e -> {
            if (e.getDragboard().hasString() && e.getDragboard().getString().startsWith(KanbanCard.DRAG_PREFIX)) e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });
        setOnDragEntered(e -> {
            if (e.getDragboard().hasString()) getStyleClass().add("drop-target");
        });
        setOnDragExited(e -> getStyleClass().remove("drop-target"));
        setOnDragDropped(e -> {
            boolean ok = false;
            if (e.getDragboard().hasString() && e.getDragboard().getString().startsWith(KanbanCard.DRAG_PREFIX)) {
                long id = Long.parseLong(e.getDragboard().getString().substring(KanbanCard.DRAG_PREFIX.length()));
                ok = true;
                // Se ejecuta despues de terminar el drag para poder abrir dialogos.
                javafx.application.Platform.runLater(() -> alSoltar.accept(id, etapa));
            }
            e.setDropCompleted(ok);
            e.consume();
        });
    }
}
