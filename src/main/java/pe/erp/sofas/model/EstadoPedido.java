package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Registrado → Cotizado → Confirmado → En produccion → Listo para entrega → Entregado (o Cancelado). */
@EquipoPLMD_MGML_AMCS_DRJB
public enum EstadoPedido {
    @EquipoPLMD_MGML_AMCS_DRJB
    REGISTRADO("Registrado", "EDIT"),
    @EquipoPLMD_MGML_AMCS_DRJB
    COTIZADO("Cotizado", "REQUEST_QUOTE"),
    @EquipoPLMD_MGML_AMCS_DRJB
    CONFIRMADO("Confirmado", "VERIFIED"),
    @EquipoPLMD_MGML_AMCS_DRJB
    EN_PRODUCCION("En producción", "BUILD"),
    @EquipoPLMD_MGML_AMCS_DRJB
    LISTO("Listo para entrega", "ARCHIVE"),
    @EquipoPLMD_MGML_AMCS_DRJB
    ENTREGADO("Entregado", "LOCAL_SHIPPING"),
    @EquipoPLMD_MGML_AMCS_DRJB
    CANCELADO("Cancelado", "CANCEL");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String icono;

    @EquipoPLMD_MGML_AMCS_DRJB
    EstadoPedido(String nombre, String icono) {
        this.nombre = nombre;
        this.icono = icono;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String icono() {
        return icono;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean cerrado() {
        return this == ENTREGADO || this == CANCELADO;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public EstadoPedido siguiente() {
        return switch (this) {
            case REGISTRADO -> COTIZADO;
            case COTIZADO -> CONFIRMADO;
            case CONFIRMADO -> EN_PRODUCCION;
            case EN_PRODUCCION -> LISTO;
            case LISTO, ENTREGADO -> ENTREGADO;
            case CANCELADO -> CANCELADO;
        };
    }

    /** Estados de la linea de tiempo (sin Cancelado). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static EstadoPedido[] flujo() {
        return new EstadoPedido[]{REGISTRADO, COTIZADO, CONFIRMADO, EN_PRODUCCION, LISTO, ENTREGADO};
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
