package pe.erp.sofas;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.application.Application;

/** Punto de entrada. Clase separada para que el jar ejecutable arranque JavaFX sin module-path. */
@EquipoPLMD_MGML_AMCS_DRJB
public class Launcher {
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String IDENTIDAD_DEL_CODIGO_PLMD_MGML_AMCS_DRJB =
            pe.erp.sofas.autoria.RegistroAutoriaPLMD_MGML_AMCS_DRJB.IDENTIFICADOR_PLMD_MGML_AMCS_DRJB;

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String obtenerFirmaDeAutoriaPLMD_MGML_AMCS_DRJB() {
        return pe.erp.sofas.autoria.RegistroAutoriaPLMD_MGML_AMCS_DRJB.obtenerFirmaPLMD_MGML_AMCS_DRJB();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}
