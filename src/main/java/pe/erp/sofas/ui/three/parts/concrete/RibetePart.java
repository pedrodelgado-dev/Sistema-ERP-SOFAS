package pe.erp.sofas.ui.three.parts.concrete;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import pe.erp.sofas.ui.three.geom.PipingMesh;
import pe.erp.sofas.ui.three.parts.BaseSofaPart;

/**
 * Ribete decorativo: tubo delgado que delinea los bordes de un cojín.
 * Se posiciona en el perímetro (típicamente en la arista superior de un cojín de asiento).
 *
 * El ribete es principalmente decorativo pero añade realismo al sofá.
 * Color: típicamente 10-15% más oscuro que el cojín.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class RibetePart extends BaseSofaPart {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final PhongMaterial materialRibete;

    @EquipoPLMD_MGML_AMCS_DRJB
    public RibetePart(double ancho, double profundidad, PhongMaterial materialBase) {
        super("Ribete", ancho, profundidad, 1);  // Alto mínimo, casi invisible
        // Ribete: color ligeramente más oscuro que el base
        if (materialBase != null) {
            Color colBase = (Color) materialBase.getDiffuseColor();
            Color colOscuro = colBase.deriveColor(0, 1, 0.85, 1);  // 15% más oscuro
            this.materialRibete = new PhongMaterial(colOscuro);
            this.materialRibete.setSpecularPower(materialBase.getSpecularPower());
        } else {
            this.materialRibete = new PhongMaterial(Color.web("#8B8B8B"));
            this.materialRibete.setSpecularPower(8);
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public Group build() {
        Group g = new Group();

        // Crear tubo de ribete alrededor del perímetro
        MeshView ribete = new MeshView(PipingMesh.crearDefecto(ancho, fondo));
        ribete.setMaterial(materialRibete);
        ribete.setCullFace(javafx.scene.shape.CullFace.NONE);

        agregar(g, ribete, 0, 0, 0);

        return g;
    }
}
