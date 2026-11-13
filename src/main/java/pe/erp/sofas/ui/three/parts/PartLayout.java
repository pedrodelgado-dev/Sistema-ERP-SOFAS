package pe.erp.sofas.ui.three.parts;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/**
 * Layouts soportados para ensamblar módulos de sofá.
 * Define cómo se encadenan las partes espacialmente.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public enum PartLayout {
    /**
     * Sofá lineal: una fila de módulos alineados.
     * Ejemplo: 3 cuerpos lineales.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    LINEAL("Sofá lineal"),

    /**
     * Seccional en L con chaise: lado principal + chaise extendida.
     * Ejemplo: 3 cuerpos + chaise derecha.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    L_CHAISE_IZQ("L con chaise izquierda"),
    @EquipoPLMD_MGML_AMCS_DRJB
    L_CHAISE_DER("L con chaise derecha"),

    /**
     * Seccional en L con esquinero: lado A + módulo esquina + lado B.
     * El esquinero es cuadrado con respaldo en dos lados.
     * Ejemplo: 3 cuerpos + esquinero 95×95 + 2 cuerpos.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    L_ESQUINERO_IZQ("L con esquinero izquierdo"),
    @EquipoPLMD_MGML_AMCS_DRJB
    L_ESQUINERO_DER("L con esquinero derecho"),

    /**
     * Seccional en U: dos esquineros y módulos entre ellos.
     * Ejemplo: 2 + E + 3 + E + 2.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    U_ESQUINEROS("Forma U"),

    /**
     * Constructor modular libre: cadena arbitraria de módulos.
     * Permite composiciones personalizadas pieza a pieza.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    MODULAR("Constructor modular");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;

    @EquipoPLMD_MGML_AMCS_DRJB
    PartLayout(String nombre) {
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
