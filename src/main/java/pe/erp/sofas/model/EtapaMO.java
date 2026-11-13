package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Etapas de mano de obra de una ficha tecnica con su costo por hora por defecto (S/). */
@EquipoPLMD_MGML_AMCS_DRJB
public enum EtapaMO {
    @EquipoPLMD_MGML_AMCS_DRJB
    CORTE("Corte", 8),
    @EquipoPLMD_MGML_AMCS_DRJB
    CARPINTERIA("Carpintería", 8),
    @EquipoPLMD_MGML_AMCS_DRJB
    COSTURA("Costura", 8),
    @EquipoPLMD_MGML_AMCS_DRJB
    TAPIZADO("Tapizado", 9),
    @EquipoPLMD_MGML_AMCS_DRJB
    ACABADO("Acabado", 7),
    @EquipoPLMD_MGML_AMCS_DRJB
    CONTROL_CALIDAD("Control de calidad", 7);

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final double costoHora;

    @EquipoPLMD_MGML_AMCS_DRJB
    EtapaMO(String nombre, double costoHora) {
        this.nombre = nombre;
        this.costoHora = costoHora;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double costoHora() {
        return costoHora;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
