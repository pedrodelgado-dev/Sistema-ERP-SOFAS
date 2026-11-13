package pe.erp.sofas;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Contexto de Spring (sin servidor web): repositorios JPA sobre SQLite, servicios y controladores. */
@EquipoPLMD_MGML_AMCS_DRJB
@SpringBootApplication
public class SofasApplication {
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String IDENTIDAD_DEL_CODIGO_PLMD_MGML_AMCS_DRJB =
            pe.erp.sofas.autoria.RegistroAutoriaPLMD_MGML_AMCS_DRJB.IDENTIFICADOR_PLMD_MGML_AMCS_DRJB;

}
