package pe.erp.sofas.ui.three.parts;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import org.junit.jupiter.api.Test;
import pe.erp.sofas.ui.three.parts.concrete.CapitonePart;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para CapitonePart (capitoné con botones).
 */
@EquipoPLMD_MGML_AMCS_DRJB
class CapitonePartTest {

    @EquipoPLMD_MGML_AMCS_DRJB
    private PhongMaterial materialTela() {
        PhongMaterial m = new PhongMaterial(Color.web("#5B4135"));  // Cuero cognac
        m.setSpecularPower(40);
        return m;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testCapitonePartCreation() {
        var capitone = new CapitonePart(156, 85, 20, materialTela());
        assertNotNull(capitone);
        assertEquals("Capitoné", capitone.getName());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testCapitonePartDimensions() {
        var capitone = new CapitonePart(156, 85, 20, materialTela());
        var dims = capitone.getDimensions();

        assertEquals(156, dims.ancho());
        assertEquals(20, dims.fondo());
        assertEquals(85, dims.alto());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testCapitonePartBuild() {
        var capitone = new CapitonePart(156, 85, 20, materialTela());
        Group geom = capitone.build();

        assertNotNull(geom);
        assertTrue(geom.getChildren().size() > 1, "Debe tener malla + botones");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testCapitonePartAnchors() {
        var capitone = new CapitonePart(156, 85, 20, materialTela());
        var anchors = capitone.getAnchors();

        assertNotNull(anchors);
        assertTrue(anchors.containsKey("left"));
        assertTrue(anchors.containsKey("right"));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testCapitonePartChesterfield() {
        // Caso real: respaldo Chesterfield
        var material = new PhongMaterial(Color.web("#8B4513"));  // Saddle brown
        var capitone = new CapitonePart(156, 85, 20, material);

        Group geom = capitone.build();
        assertNotNull(geom);

        // Debe tener malla + botones
        int numNodos = geom.getChildren().size();
        assertTrue(numNodos > 10, "Chesterfield debe tener muchos botones");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testCapitonePartNullMaterial() {
        // Con material null debe usar default
        var capitone = new CapitonePart(100, 70, 15, null);
        Group geom = capitone.build();
        assertNotNull(geom);
    }
}
