package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Componente del sofa al que pertenece una linea de la lista de materiales. */
@EquipoPLMD_MGML_AMCS_DRJB
public enum Componente {
    @EquipoPLMD_MGML_AMCS_DRJB
    ESTRUCTURA("Estructura", "#A47148"),
    @EquipoPLMD_MGML_AMCS_DRJB
    ASIENTO("Asiento", "#0EA5E9"),
    @EquipoPLMD_MGML_AMCS_DRJB
    RESPALDO("Respaldo", "#6366F1"),
    @EquipoPLMD_MGML_AMCS_DRJB
    BRAZOS("Brazos", "#8B5CF6"),
    @EquipoPLMD_MGML_AMCS_DRJB
    TAPIZADO("Tapizado", "#EC4899"),
    @EquipoPLMD_MGML_AMCS_DRJB
    COJINES("Cojines", "#F59E0B"),
    @EquipoPLMD_MGML_AMCS_DRJB
    ENSAMBLAJE("Ensamblaje", "#64748B"),
    @EquipoPLMD_MGML_AMCS_DRJB
    PATAS("Patas", "#C69A6D"),
    @EquipoPLMD_MGML_AMCS_DRJB
    ACABADO("Acabado", "#14B8A6"),
    @EquipoPLMD_MGML_AMCS_DRJB
    EMBALAJE("Embalaje", "#94A3B8"),
    @EquipoPLMD_MGML_AMCS_DRJB
    MECANISMO("Mecanismo", "#2563EB");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String color;

    @EquipoPLMD_MGML_AMCS_DRJB
    Componente(String nombre, String color) {
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
