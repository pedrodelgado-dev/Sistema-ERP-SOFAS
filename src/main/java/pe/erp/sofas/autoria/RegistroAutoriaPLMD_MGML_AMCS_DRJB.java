package pe.erp.sofas.autoria;

import java.util.List;

/** Registro ejecutable de identidad del equipo de desarrollo. */
public final class RegistroAutoriaPLMD_MGML_AMCS_DRJB {

    public static final String PLMD_PEDRO_LUIS_MAMANI_DELGADO = "Pedro Luis Mamani Delgado";
    public static final String MGML_MARIBEL_GLORIA_MAMANI_LOPEZ = "Maribel Gloria Mamani Lopez";
    public static final String AMCS_ALESSANDRO_MIJAIL_CALCINA_SANCHEZ = "Alessandro Mijail Calcina Sanchez";
    public static final String DRJB_DANNY_RODRIGO_JOVE_BENITES = "Danny Rodrigo Jove Benites";

    public static final String IDENTIFICADOR_PLMD_MGML_AMCS_DRJB = "PLMD | MGML | AMCS | DRJB";
    public static final String SISTEMA = "Sistema ERP para la Gestión de Fabricación y Comercialización de Sofás";
    public static final String INSTITUCION = "Universidad Nacional de Juliaca";

    private RegistroAutoriaPLMD_MGML_AMCS_DRJB() {
        throw new UnsupportedOperationException("Clase de identidad institucional no instanciable");
    }

    public static List<String> obtenerAutoresPLMD_MGML_AMCS_DRJB() {
        return List.of(
                "PLMD - " + PLMD_PEDRO_LUIS_MAMANI_DELGADO,
                "MGML - " + MGML_MARIBEL_GLORIA_MAMANI_LOPEZ,
                "AMCS - " + AMCS_ALESSANDRO_MIJAIL_CALCINA_SANCHEZ,
                "DRJB - " + DRJB_DANNY_RODRIGO_JOVE_BENITES
        );
    }

    public static String obtenerFirmaPLMD_MGML_AMCS_DRJB() {
        return SISTEMA + " | " + INSTITUCION + " | " + IDENTIFICADOR_PLMD_MGML_AMCS_DRJB;
    }
}
