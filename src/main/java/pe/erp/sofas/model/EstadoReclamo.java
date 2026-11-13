package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

@EquipoPLMD_MGML_AMCS_DRJB
public enum EstadoReclamo {
    @EquipoPLMD_MGML_AMCS_DRJB
    ABIERTO("Abierto"),
    @EquipoPLMD_MGML_AMCS_DRJB
    EN_DIAGNOSTICO("En diagnóstico"),
    @EquipoPLMD_MGML_AMCS_DRJB
    EN_REPARACION("En reparación"),
    @EquipoPLMD_MGML_AMCS_DRJB
    RESUELTO("Resuelto"),
    @EquipoPLMD_MGML_AMCS_DRJB
    RECHAZADO("Rechazado");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;

    @EquipoPLMD_MGML_AMCS_DRJB
    EstadoReclamo(String nombre) {
        this.nombre = nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean cerrado() {
        return this == RESUELTO || this == RECHAZADO;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
