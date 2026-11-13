package pe.erp.sofas.service.calculo;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import pe.erp.sofas.model.FichaTecnica;
import pe.erp.sofas.model.Material;

/** Lo que la explosion necesita consultar: fichas aprobadas (para las compuestas) y materiales. */
@EquipoPLMD_MGML_AMCS_DRJB
public interface Catalogo {

    /** Version aprobada de la ficha con ese codigo, o null. */
    @EquipoPLMD_MGML_AMCS_DRJB
    FichaTecnica fichaAprobada(String codigo);

    @EquipoPLMD_MGML_AMCS_DRJB
    Material material(String codigo);
}
