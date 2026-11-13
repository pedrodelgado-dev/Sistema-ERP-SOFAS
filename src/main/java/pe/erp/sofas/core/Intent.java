package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/**
 * "Intencion" de navegacion: ademas de abrir un modulo, le pide que haga algo al mostrarse
 * (p. ej. abrir el formulario de nuevo registro o el detalle de un codigo).
 */
@EquipoPLMD_MGML_AMCS_DRJB
public record Intent(@EquipoPLMD_MGML_AMCS_DRJB
  String accion, @EquipoPLMD_MGML_AMCS_DRJB
    Object dato) {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String ACCION_NUEVO = "NEW";
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String ACCION_ABRIR = "OPEN";
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String ACCION_PRESELECCIONAR = "PRESELECT";

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final Intent NONE = new Intent(null, null);
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final Intent NEW = new Intent(ACCION_NUEVO, null);

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Intent abrir(Object dato) {
        return new Intent(ACCION_ABRIR, dato);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Intent preseleccionar(Object dato) {
        return new Intent(ACCION_PRESELECCIONAR, dato);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean es(String accion) {
        return accion.equals(this.accion);
    }
}
