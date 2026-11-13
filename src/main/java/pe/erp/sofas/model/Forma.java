package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Forma del modelo de sofa: decide como se dibuja en 3D y que despiece usa la hoja de corte. */
@EquipoPLMD_MGML_AMCS_DRJB
public enum Forma {
    @EquipoPLMD_MGML_AMCS_DRJB
    MODERNO("Sofá Moderno"),
    @EquipoPLMD_MGML_AMCS_DRJB
    SECCIONAL_L("Seccional L"),
    @EquipoPLMD_MGML_AMCS_DRJB
    SILLON("Sillón"),
    @EquipoPLMD_MGML_AMCS_DRJB
    JUEGO("Juego de sala"),
    @EquipoPLMD_MGML_AMCS_DRJB
    CHESTERFIELD("Chesterfield"),
    @EquipoPLMD_MGML_AMCS_DRJB
    SOFA_CAMA("Sofá cama"),
    @EquipoPLMD_MGML_AMCS_DRJB
    RECLINABLE("Reclinable"),
    @EquipoPLMD_MGML_AMCS_DRJB
    ESCANDINAVO("Escandinavo"),
    @EquipoPLMD_MGML_AMCS_DRJB
    PUFF("Puff"),
    @EquipoPLMD_MGML_AMCS_DRJB
    SECCIONAL_U("Seccional U");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;

    @EquipoPLMD_MGML_AMCS_DRJB
    Forma(String nombre) {
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
