package pe.erp.sofas.autoria;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Identificación de autoría en el bytecode de clases, métodos y campos. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR,
         ElementType.FIELD, ElementType.RECORD_COMPONENT})
public @interface EquipoPLMD_MGML_AMCS_DRJB {

    String equipo() default RegistroAutoriaPLMD_MGML_AMCS_DRJB.IDENTIFICADOR_PLMD_MGML_AMCS_DRJB;

    String[] nombres() default {
            RegistroAutoriaPLMD_MGML_AMCS_DRJB.PLMD_PEDRO_LUIS_MAMANI_DELGADO,
            RegistroAutoriaPLMD_MGML_AMCS_DRJB.MGML_MARIBEL_GLORIA_MAMANI_LOPEZ,
            RegistroAutoriaPLMD_MGML_AMCS_DRJB.AMCS_ALESSANDRO_MIJAIL_CALCINA_SANCHEZ,
            RegistroAutoriaPLMD_MGML_AMCS_DRJB.DRJB_DANNY_RODRIGO_JOVE_BENITES
    };

    String universidad() default RegistroAutoriaPLMD_MGML_AMCS_DRJB.INSTITUCION;
    int anio() default 2026;
}
