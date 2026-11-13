package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

@EquipoPLMD_MGML_AMCS_DRJB
public enum EstadoFicha {
    @EquipoPLMD_MGML_AMCS_DRJB
    BORRADOR("Borrador"),
    @EquipoPLMD_MGML_AMCS_DRJB
    APROBADA("Aprobada"),
    @EquipoPLMD_MGML_AMCS_DRJB
    OBSOLETA("Obsoleta");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;

    @EquipoPLMD_MGML_AMCS_DRJB
    EstadoFicha(String nombre) {
        this.nombre = nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
