package pe.erp.sofas.ui.three.parts.families;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.paint.PhongMaterial;
import pe.erp.sofas.model.ConfigSofa;
import pe.erp.sofas.ui.three.parts.SofaPart;
import pe.erp.sofas.ui.three.parts.concrete.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Familia Seccional U: dos esquineros, composición en forma de U.
 * Código: SF-011
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class SeccionalUFamily implements FamiliaFactory.FamiliaConstructor {
    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public List<SofaPart> construir(ConfigSofa config, PhongMaterial materialTela, PhongMaterial materialRespaldo) {
        List<SofaPart> partes = new ArrayList<>();
        PhongMaterial matResp = materialRespaldo == null ? materialTela : materialRespaldo;

        // TODO: Implementar forma U (2+E+3+E+2)
        partes.add(new BaseSeatFramePart(400, 92, 13, materialTela));
        return partes;
    }
}
