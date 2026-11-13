package pe.erp.sofas.ui.three.parts.concrete;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import pe.erp.sofas.ui.three.parts.BaseSofaPart;

/**
 * Base o armazón de asiento: caja redondeada tapizada que forma la tarima del sofá.
 * Altura típica: 18 cm (incluyendo altura de patas).
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class BaseSeatFramePart extends BaseSofaPart {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final PhongMaterial material;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final double altoPatas;

    @EquipoPLMD_MGML_AMCS_DRJB
    public BaseSeatFramePart(double ancho, double fondo, double altoPatas, PhongMaterial material) {
        super("Base de asiento", ancho, fondo, 18);  // ancho, fondo, alto
        this.material = material == null ? materialPorDefecto() : material;
        this.altoPatas = altoPatas;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public Group build() {
        Group g = new Group();
        // Armazón: caja redondeada con radio 3 cm
        agregar(g, cajaRedondeada(ancho, alto, fondo, 3, material), 0, alto / 2, 0);
        return g;
    }
}
