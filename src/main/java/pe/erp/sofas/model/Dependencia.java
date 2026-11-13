package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/**
 * Formula de escalado de una linea de la ficha segun las medidas pedidas (L largo, F fondo, A alto,
 * n cuerpos) frente a las medidas base de la ficha (L0, F0, A0, n0). La expresion se evalua con exp4j.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public enum Dependencia {
    @EquipoPLMD_MGML_AMCS_DRJB
    F("Fija", "1"),
    @EquipoPLMD_MGML_AMCS_DRJB
    L("× largo", "L / L0"),
    @EquipoPLMD_MGML_AMCS_DRJB
    LF("× largo·fondo", "(L * F) / (L0 * F0)"),
    @EquipoPLMD_MGML_AMCS_DRJB
    LA("× largo·alto", "(L * A) / (L0 * A0)"),
    @EquipoPLMD_MGML_AMCS_DRJB
    T("× superficie tapizada", "(L * (F + A)) / (L0 * (F0 + A0))"),
    @EquipoPLMD_MGML_AMCS_DRJB
    N("× cuerpos", "n / n0"),
    @EquipoPLMD_MGML_AMCS_DRJB
    P("Patas 4/6 según largo", "patas(L) / patas(L0)");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String expresion;

    @EquipoPLMD_MGML_AMCS_DRJB
    Dependencia(String nombre, String expresion) {
        this.nombre = nombre;
        this.expresion = expresion;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String expresion() {
        return expresion;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
