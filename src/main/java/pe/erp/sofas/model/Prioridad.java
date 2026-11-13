package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

@EquipoPLMD_MGML_AMCS_DRJB
public enum Prioridad {
    @EquipoPLMD_MGML_AMCS_DRJB
    ALTA("Alta", "#DC2626"),
    @EquipoPLMD_MGML_AMCS_DRJB
    MEDIA("Media", "#F59E0B"),
    @EquipoPLMD_MGML_AMCS_DRJB
    BAJA("Baja", "#94A3B8");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String color;

    @EquipoPLMD_MGML_AMCS_DRJB
    Prioridad(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String color() {
        return color;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
