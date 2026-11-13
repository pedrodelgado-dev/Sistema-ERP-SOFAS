package pe.erp.sofas.ui.three.parts.concrete;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import pe.erp.sofas.ui.three.parts.BaseSofaPart;

/**
 * Chaise lounge: módulo extendido para apoyar las piernas.
 * Es como un asiento adicional pero más largo (típicamente 65 cm de largo).
 * Se posiciona a la derecha o izquierda del sofá principal.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class ChaseLoungeModule extends BaseSofaPart {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final PhongMaterial material;

    @EquipoPLMD_MGML_AMCS_DRJB
    public ChaseLoungeModule(double largo, PhongMaterial material) {
        super("Chaise lounge", largo, 92, 85);  // 65 cm de largo típico, 92 cm de fondo
        this.material = material == null ? materialPorDefecto() : material;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public Group build() {
        Group g = new Group();

        // Base de la chaise
        agregar(g, cajaRedondeada(ancho, 18, fondo, 3, material), 0, 18 / 2, 0);

        // Cojín de asiento alargado
        double largoCojin = ancho - 4;
        agregar(g, cajaRedondeada(largoCojin, 14, fondo - 4, 5, material),
                0, 18 + 14 / 2, 0);

        // Brazo en una punta (pequeño remate)
        agregar(g, cajaRedondeada(17, 14, 20, 4, material),
                ancho / 2 - 17 / 2, 18 + 14 / 2, -fondo / 2 + 10);

        return g;
    }
}
