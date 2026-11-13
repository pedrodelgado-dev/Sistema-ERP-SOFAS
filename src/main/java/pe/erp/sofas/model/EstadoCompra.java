package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Borrador → Solicitada → Aprobada → Recibida (o Cancelada). */
@EquipoPLMD_MGML_AMCS_DRJB
public enum EstadoCompra {
    @EquipoPLMD_MGML_AMCS_DRJB
    BORRADOR("Borrador"),
    @EquipoPLMD_MGML_AMCS_DRJB
    SOLICITADA("Solicitada"),
    @EquipoPLMD_MGML_AMCS_DRJB
    APROBADA("Aprobada"),
    @EquipoPLMD_MGML_AMCS_DRJB
    RECIBIDA("Recibida"),
    @EquipoPLMD_MGML_AMCS_DRJB
    CANCELADA("Cancelada");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;

    @EquipoPLMD_MGML_AMCS_DRJB
    EstadoCompra(String nombre) {
        this.nombre = nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    /** Compra en transito para el MRP. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean enTransito() {
        return this == SOLICITADA || this == APROBADA;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
