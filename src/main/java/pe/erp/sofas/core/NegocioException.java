package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Regla de negocio incumplida; el mensaje se muestra tal cual al usuario. */
@EquipoPLMD_MGML_AMCS_DRJB
public class NegocioException extends RuntimeException {
    @EquipoPLMD_MGML_AMCS_DRJB
    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
