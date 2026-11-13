package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;

/** Pildora de estado: fondo suave + texto del color del estado. */
@EquipoPLMD_MGML_AMCS_DRJB
public class StatusBadge extends Label {

    @EquipoPLMD_MGML_AMCS_DRJB
    private static final Map<String, String> VARIANTES = new HashMap<>();

    static {
        for (String s : new String[]{"Disponible", "Normal", "Aprobada", "Entregado", "Pagado", "Resuelto", "En garantía", "Listo",
                "Reservado", "Suficiente", "Recibida", "Activo", "Consumido", "Listo para entrega", "Cubierto", "Materiales reservados",
                "Materiales consumidos"}) VARIANTES.put(s, "green");
        for (String s : new String[]{"Stock bajo", "Borrador", "Pendiente", "Por vencer", "Abierto", "Parcial", "Faltará", "Por cotizar",
                "Media", "Vacaciones", "Sin ficha aprobada", "Sin ficha · autorizado", "Registrado", "Nueva", "En reparación"}) VARIANTES.put(s, "amber");
        for (String s : new String[]{"Agotado", "Crítico", "Retrasado", "Cancelado", "Rechazado", "Fuera de garantía", "Faltante", "Sin stock",
                "Alta", "Cancelada", "Vencido", "SLA vencido"}) VARIANTES.put(s, "red");
        for (String s : new String[]{"En producción", "En diagnóstico", "Aprobado", "Confirmado", "Solicitada", "Cotizado", "En taller",
                "Factura", "Aprobada OC", "Materiales disponibles"}) VARIANTES.put(s, "blue");
        for (String s : new String[]{"Personalizado", "Exhibición", "Recojo programado", "A pedido", "Bajo pedido", "Compuesta", "Retazo",
                "MRP", "Calculadora"}) VARIANTES.put(s, "wood");
        for (String s : new String[]{"Obsoleta", "Descontinuada", "Inactivo", "Estándar", "Boleta", "Baja", "Stock de tienda"}) VARIANTES.put(s, "gray");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public StatusBadge(String estado) {
        this(estado, variante(estado));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public StatusBadge(String texto, String variante) {
        super(texto);
        getStyleClass().addAll("badge", "badge-" + variante);
        setMinWidth(USE_PREF_SIZE);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String variante(String estado) {
        if (estado == null) return "gray";
        if (estado.startsWith("Faltan") || estado.startsWith("Faltarán")) return "red";
        if (estado.startsWith("En garantía")) return "green";
        if (estado.startsWith("OP-")) return "wood";
        return VARIANTES.getOrDefault(estado, "gray");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public StatusBadge conIcono(String icono) {
        setGraphic(Ico.of(icono, 13));
        return this;
    }
}
