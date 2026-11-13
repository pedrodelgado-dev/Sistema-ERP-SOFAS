package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Roles del sistema; la matriz de permisos por modulo esta en {@link Permiso}. */
@EquipoPLMD_MGML_AMCS_DRJB
public enum Rol {
    @EquipoPLMD_MGML_AMCS_DRJB
    GERENTE("Gerente"),
    @EquipoPLMD_MGML_AMCS_DRJB
    ADMINISTRADOR("Administrador"),
    @EquipoPLMD_MGML_AMCS_DRJB
    VENDEDOR("Vendedor"),
    @EquipoPLMD_MGML_AMCS_DRJB
    SUPERVISOR("Supervisor de taller"),
    @EquipoPLMD_MGML_AMCS_DRJB
    CARPINTERO("Carpintero"),
    @EquipoPLMD_MGML_AMCS_DRJB
    COSTURERA("Costurera"),
    @EquipoPLMD_MGML_AMCS_DRJB
    TAPICERO("Tapicero"),
    @EquipoPLMD_MGML_AMCS_DRJB
    ALMACENERO("Almacenero");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;

    @EquipoPLMD_MGML_AMCS_DRJB
    Rol(String nombre) {
        this.nombre = nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    /** Puede autorizar que una OP avance sin ficha aprobada. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean autoriza() {
        return this == GERENTE || this == SUPERVISOR || this == ADMINISTRADOR;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
