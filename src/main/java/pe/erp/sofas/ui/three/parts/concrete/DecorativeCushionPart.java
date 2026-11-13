package pe.erp.sofas.ui.three.parts.concrete;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import pe.erp.sofas.ui.three.parts.BaseSofaPart;

/**
 * Cojín decorativo: pequeños cojines cuadrados o rectangulares inclinados
 * que se colocan sobre el respaldo o el asiento para mayor comodidad visual.
 * Altura típica: 12 cm, ancho variable (40-50 cm).
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class DecorativeCushionPart extends BaseSofaPart {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final PhongMaterial material;

    @EquipoPLMD_MGML_AMCS_DRJB
    public DecorativeCushionPart(double ancho, PhongMaterial material) {
        super("Cojín decorativo", ancho, ancho, 12);  // Cuadrado, bajo
        this.material = material == null ? materialPorDefecto() : material;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public Group build() {
        Group g = new Group();

        // Cojín con radio 5 cm
        var cojin = cajaRedondeada(ancho, alto, fondo, 5, material);

        // Inclinación: 12° para darle dinamismo
        cojin.getTransforms().add(new Rotate(-12, Rotate.X_AXIS));
        cojin.getTransforms().add(new Rotate(6, Rotate.Z_AXIS));

        agregar(g, cojin, 0, alto / 2, 0);

        return g;
    }
}
