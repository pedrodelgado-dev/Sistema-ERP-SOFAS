package pe.erp.sofas.ui.three.parts.concrete;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import pe.erp.sofas.ui.three.parts.BaseSofaPart;

/**
 * Puff u ottomán: módulo bajo sin respaldo, usado como posapies o asiento extra.
 * Típicamente cuadrado o rectangular, altura baja (40-50 cm).
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class OttomanPart extends BaseSofaPart {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final PhongMaterial material;

    @EquipoPLMD_MGML_AMCS_DRJB
    public OttomanPart(double ancho, double fondo, PhongMaterial material) {
        super("Puff", ancho, fondo, 40);  // Altura baja
        this.material = material == null ? materialPorDefecto() : material;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public Group build() {
        Group g = new Group();

        // Cuerpo principal del puff: caja redondeada
        agregar(g, cajaRedondeada(ancho, alto, fondo, 6, material), 0, alto / 2, 0);

        return g;
    }
}
