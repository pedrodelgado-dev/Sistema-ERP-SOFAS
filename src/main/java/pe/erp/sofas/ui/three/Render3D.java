package pe.erp.sofas.ui.three;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.image.Image;
import pe.erp.sofas.model.Producto;

import java.util.HashMap;
import java.util.Map;

/** Miniaturas renderizadas en 3D de los modelos sin foto (snapshot de la SubScene, cacheado). */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Render3D {

    @EquipoPLMD_MGML_AMCS_DRJB
    private static final Map<String, Image> CACHE = new HashMap<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    private Render3D() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Image modelo(Producto p) {
        return CACHE.computeIfAbsent(p.getCodigo() + p.getColorHex() + p.getLargo(), k ->
                Viewer3D.render(new SofaBuilder().build(SofaBuilder.Opciones.de(p)), 640, 420, -28, 16));
    }
}
