package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Por cotizar → Cotizado → Aprobado → Recojo programado → En taller → Listo → Entregado. */
@EquipoPLMD_MGML_AMCS_DRJB
public enum EstadoRetapizado {
    @EquipoPLMD_MGML_AMCS_DRJB
    POR_COTIZAR("Por cotizar", "Cotizar"),
    @EquipoPLMD_MGML_AMCS_DRJB
    COTIZADO("Cotizado", "Aprobar"),
    @EquipoPLMD_MGML_AMCS_DRJB
    APROBADO("Aprobado", "Programar recojo"),
    @EquipoPLMD_MGML_AMCS_DRJB
    RECOJO_PROGRAMADO("Recojo programado", "Ingresar a taller"),
    @EquipoPLMD_MGML_AMCS_DRJB
    EN_TALLER("En taller", "Marcar listo"),
    @EquipoPLMD_MGML_AMCS_DRJB
    LISTO("Listo", "Entregar"),
    @EquipoPLMD_MGML_AMCS_DRJB
    ENTREGADO("Entregado", null);

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String accion;

    @EquipoPLMD_MGML_AMCS_DRJB
    EstadoRetapizado(String nombre, String accion) {
        this.nombre = nombre;
        this.accion = accion;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    /** Texto del boton que lleva al siguiente estado. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public String accion() {
        return accion;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public EstadoRetapizado siguiente() {
        return this == ENTREGADO ? ENTREGADO : values()[ordinal() + 1];
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
