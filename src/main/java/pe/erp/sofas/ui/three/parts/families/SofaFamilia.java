package pe.erp.sofas.ui.three.parts.families;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/**
 * Las 13 familias de sofás del catálogo SISTEMA ERP SOFAS.
 * Cada familia tiene su constructores y reglas de composición.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public enum SofaFamilia {
    /**
     * Sofá Moderno: brazo recto ancho, cojines sueltos, respaldo cojines.
     * Código: SF-001/002/003
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    MODERNO("Sofá Moderno", "SF-001", true),

    /**
     * Seccional L con chaise: sofá + chaise extendida a la derecha o izquierda.
     * Código: SF-005
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    SECCIONAL_L_CHAISE("Seccional L con chaise", "SF-005", true),

    /**
     * Seccional L con esquinero: lado A + módulo esquina cuadrado + lado B.
     * NUEVO: Módulo de esquina real con respaldos en dos lados.
     * Código: SF-012
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    SECCIONAL_L_ESQUINERO("Seccional L con esquinero", "SF-012", false),

    /**
     * Seccional U: lado B + esquinero + lado A + esquinero + lado B.
     * NUEVO: Forma completa de U.
     * Código: SF-011
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    SECCIONAL_U("Seccional U", "SF-011", false),

    /**
     * Sofá Modular: constructor libre, pieza a pieza.
     * NUEVO: Permite composiciones personalizadas.
     * Código: SF-013
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    MODULAR("Sofá Modular", "SF-013", false),

    /**
     * Chesterfield: brazo enrollado, capitoné con botones, ribete, patas bola.
     * Código: SF-006
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    CHESTERFIELD("Chesterfield", "SF-006", true),

    /**
     * Escandinavo: perfil delgado, patas inclinadas, asiento banco corrido.
     * Código: SF-009
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    ESCANDINAVO("Escandinavo", "SF-009", true),

    /**
     * Sofá cama: respaldo abatible tipo clic-clac, colchoneta plegable.
     * Código: SF-007
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    SOFA_CAMA("Sofá cama", "SF-007", true),

    /**
     * Reclinable: respaldo acolchado alto, reposapiés desplegable por cuerpo.
     * Código: SF-008
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    RECLINABLE("Reclinable", "SF-008", true),

    /**
     * Retro / Mid-century: brazos inclinados, respaldo con canales verticales.
     * NUEVO: Estilo retro distintivo.
     * Código: SF-014
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    RETRO("Retro / Mid-century", "SF-014", false),

    /**
     * Cloud / Modular profundo: muy profundo (110 cm), cojines gruesos inflados, sin patas visibles.
     * NUEVO: Ultra confortable, aspecto premium.
     * Código: SF-015
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    CLOUD("Cloud / Modular profundo", "SF-015", false),

    /**
     * Tuxedo: brazos y respaldo a la misma altura, líneas rectas, capitoné opcional.
     * NUEVO: Diseño minimalista elegante.
     * Código: SF-016
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    TUXEDO("Tuxedo", "SF-016", false),

    /**
     * Sillón: variante simple (1 cuerpo). Existen versiones: Moderno, Chesterfield, Escandinavo, Bergère.
     * Código: SF-003
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    SILLON("Sillón", "SF-003", true);

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String codigoProducto;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final boolean yaExistia;  // true = existía en SofaBuilder, false = nueva en Fase 2

    @EquipoPLMD_MGML_AMCS_DRJB
    SofaFamilia(String nombre, String codigoProducto, boolean yaExistia) {
        this.nombre = nombre;
        this.codigoProducto = codigoProducto;
        this.yaExistia = yaExistia;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String codigoProducto() {
        return codigoProducto;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean yaExistia() {
        return yaExistia;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
