package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.model.OrdenProduccion;
import pe.erp.sofas.service.ProduccionService;

/**
 * Tarjeta del Kanban: codigo, producto × cantidad, cliente, muestra de la tela, responsable, entrega (roja si
 * vencio), prioridad, avance (etapa / 10) e indicador de materiales. Se arrastra entre columnas.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class KanbanCard extends VBox {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String DRAG_PREFIX = "OP:";

    @EquipoPLMD_MGML_AMCS_DRJB
    public KanbanCard(OrdenProduccion o, ProduccionService.Indicador ind, Runnable abrir) {
        super(8);
        getStyleClass().add("kanban-card");
        setStyle("-fx-border-color: transparent transparent transparent " + o.getPrioridad().color() + ";");

        HBox top = UI.hbox(6, UI.label(o.getCodigo(), "mono", "kc-code"), UI.spacer(), new StatusBadge(o.getPrioridad().nombre()));
        Label producto = UI.wrap(o.getProductoNombre() + "  ×" + o.getCantidad(), "kc-title");
        Label cliente = UI.label(o.getClienteNombre(), "kc-client");
        cliente.setGraphic(Ico.of(o.getCliente() == null ? "STOREFRONT" : "PERSON_OUTLINE", 14));

        HBox tela = new HBox(7);
        tela.setAlignment(Pos.CENTER_LEFT);
        if (o.getTela() != null) {
            tela.getChildren().addAll(new ColorSwatch(o.getTela().getHex(), o.getTela().getTipo(), 16), UI.label(o.getTela().getNombre(), "kc-tela"));
        } else {
            tela.getChildren().add(UI.label("Tela según ficha", "kc-tela"));
        }

        ProgressBarMini barra = new ProgressBarMini(o.getEtapa().avance(), o.getEtapa().color(), 6);
        HBox progreso = UI.hbox(8, UI.grow(barra), UI.label(o.getEtapa().paso() + "/10", "kc-pct"));

        Label indicador = UI.label(ind.texto(), "kc-ind", "kc-ind-" + ind.variante());
        indicador.setGraphic(Ico.of(ind.icono(), 13));

        AvatarInitials resp = o.getResponsable() == null ? new AvatarInitials("?", 24, "gray")
                : AvatarInitials.conColor(o.getResponsable().getNombre(), 24, o.getResponsable().getColor() == null ? "#A47148" : o.getResponsable().getColor());
        Tooltip.install(resp, new Tooltip(o.getResponsable() == null ? "Sin responsable" : o.getResponsable().getNombre()));
        Label fecha = UI.label(AppClock.diaMes(o.getFechaEntrega()), "kc-date");
        fecha.setGraphic(Ico.of("EVENT", 14));
        if (o.isRetrasada()) fecha.getStyleClass().add("kc-late");
        HBox pie = UI.hbox(6, resp, UI.label(o.getResponsable() == null ? "—" : o.getResponsable().getNombre().split(" ")[0], "cell-sub"), UI.spacer(), fecha);

        getChildren().addAll(top, producto, cliente, tela, progreso, indicador, pie);
        setOnMouseClicked(e -> abrir.run());
        setFocusTraversable(true);
        setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER) abrir.run();
        });
        setOnDragDetected(e -> {
            var db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(DRAG_PREFIX + o.getId());
            db.setContent(cc);
            db.setDragView(snapshot(null, null), e.getX(), e.getY());
            getStyleClass().add("dragging");
            e.consume();
        });
        setOnDragDone(e -> getStyleClass().remove("dragging"));
    }
}
