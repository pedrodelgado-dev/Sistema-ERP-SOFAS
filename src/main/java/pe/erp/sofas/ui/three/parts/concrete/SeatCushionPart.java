package pe.erp.sofas.ui.three.parts.concrete;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import pe.erp.sofas.ui.three.geom.PillowMesh;
import pe.erp.sofas.ui.three.parts.BaseSofaPart;

/**
 * Cojín de asiento: malla "inflada" (PillowMesh) para aspecto mullido.
 * Altura típica: 14 cm (asiento confortable).
 * Se posiciona sobre la base.
 *
 * Usa PillowMesh en lugar de cajas redondeadas para un aspecto más orgánico
 * y realista, con caras levemente abombadas hacia afuera.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class SeatCushionPart extends BaseSofaPart {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final PhongMaterial material;

    @EquipoPLMD_MGML_AMCS_DRJB
    public SeatCushionPart(double ancho, double fondo, PhongMaterial material) {
        super("Cojín de asiento", ancho, fondo, 14);  // 14 cm de alto
        this.material = material == null ? materialPorDefecto() : material;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public Group build() {
        Group g = new Group();

        // Usar PillowMesh (inflado) en lugar de caja redondeada
        MeshView cojin = new MeshView(PillowMesh.crearDefecto(ancho, alto, fondo, 4));
        cojin.setMaterial(material);
        cojin.setCullFace(javafx.scene.shape.CullFace.NONE);

        agregar(g, cojin, 0, alto / 2, 0);

        // Opcionalmente, agregar ribete decorativo en el borde (comentado para rendimiento)
        // MeshView ribete = new MeshView(PipingMesh.crearDefecto(ancho, fondo));
        // ribete.setMaterial(material);
        // agregar(g, ribete, 0, alto / 2, 0);

        return g;
    }
}
