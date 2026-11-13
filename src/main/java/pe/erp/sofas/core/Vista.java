package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Controlador de modulo: se refresca cada vez que el usuario entra al modulo. */
@EquipoPLMD_MGML_AMCS_DRJB
public interface Vista {

    /** Se invoca al mostrar el modulo; {@code intent} puede pedir abrir un formulario o un registro. */
    @EquipoPLMD_MGML_AMCS_DRJB
    void onShow(Intent intent);

    /** Vistas con recursos que deben pausarse al salir (p. ej. el AnimationTimer del 3D). */
    @EquipoPLMD_MGML_AMCS_DRJB
    interface ConCiclo extends Vista {
        @EquipoPLMD_MGML_AMCS_DRJB
        void onHide();
    }
}
