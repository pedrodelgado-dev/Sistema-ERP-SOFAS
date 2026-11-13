package pe.erp.sofas.ui.three.parts.families;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import org.junit.jupiter.api.Test;
import pe.erp.sofas.model.ConfigSofa;
import pe.erp.sofas.ui.three.parts.PartLayout;
import pe.erp.sofas.ui.three.parts.SofaPartAssembler;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de validación para la familia Moderno.
 * Aseguran que la composición modular produzca las mismas dimensiones que el SofaBuilder.
 */
@EquipoPLMD_MGML_AMCS_DRJB
class ModernoFamilyTest {

    @EquipoPLMD_MGML_AMCS_DRJB
    private PhongMaterial materialTela() {
        PhongMaterial m = new PhongMaterial(Color.web("#B9B7B1"));
        m.setSpecularPower(8);
        return m;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testModerno1Cuerpo() {
        var factory = new FamiliaFactory();
        var config = new ConfigSofa(
            ConfigSofa.Modelo.MODERNO, 1, ConfigSofa.Chaise.NINGUNA, 92, null,
            ConfigSofa.Brazo.RECTO, ConfigSofa.Patas.MADERA, ConfigSofa.AcabadoMadera.NATURAL,
            ConfigSofa.Espuma.D23, 0, null, ConfigSofa.Mecanismo.FIJO
        );

        var partes = factory.construir(SofaFamilia.MODERNO, config, materialTela(), null);

        // Moderno 1 cuerpo: brazo izq (17) + 1 cojín (52) + brazo der (17) = 86 cm
        assertEquals(3, partes.size(), "Debe tener exactamente 3 partes");

        var ensamblador = new SofaPartAssembler()
            .conLayout(PartLayout.LINEAL)
            .agregarTodas(partes);

        var resultado = ensamblador.ensamblar();
        assertEquals(85, resultado.largoTotal());  // 17 + 51 + 17 (cojín es ANCHO_CUERPO - 1)
        assertEquals(92, resultado.fondoTotal());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testModerno3Cuerpos() {
        var factory = new FamiliaFactory();
        var config = new ConfigSofa(
            ConfigSofa.Modelo.MODERNO, 3, ConfigSofa.Chaise.NINGUNA, 92, null,
            ConfigSofa.Brazo.RECTO, ConfigSofa.Patas.MADERA, ConfigSofa.AcabadoMadera.NATURAL,
            ConfigSofa.Espuma.D23, 0, null, ConfigSofa.Mecanismo.FIJO
        );

        var partes = factory.construir(SofaFamilia.MODERNO, config, materialTela(), null);

        // Moderno 3 cuerpos: brazo (17) + 3×cojín (51 c/u) + brazo (17) = 187 cm
        assertEquals(5, partes.size(), "Debe tener exactamente 5 partes");

        var ensamblador = new SofaPartAssembler()
            .conLayout(PartLayout.LINEAL)
            .agregarTodas(partes);

        var resultado = ensamblador.ensamblar();
        assertEquals(187, resultado.largoTotal());  // 17 + 51 + 51 + 51 + 17
        assertEquals(92, resultado.fondoTotal());
        assertEquals(59.5, resultado.altoTotal(), 0.1);  // Brazo: 85 * 0.7 = 59.5
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testModerno4Cuerpos() {
        var factory = new FamiliaFactory();
        var config = new ConfigSofa(
            ConfigSofa.Modelo.MODERNO, 4, ConfigSofa.Chaise.NINGUNA, 92, null,
            ConfigSofa.Brazo.RECTO, ConfigSofa.Patas.MADERA, ConfigSofa.AcabadoMadera.NATURAL,
            ConfigSofa.Espuma.D23, 0, null, ConfigSofa.Mecanismo.FIJO
        );

        var partes = factory.construir(SofaFamilia.MODERNO, config, materialTela(), null);

        // Moderno 4 cuerpos: brazo (17) + 4×cojín (51 c/u) + brazo (17) = 238 cm
        var ensamblador = new SofaPartAssembler()
            .conLayout(PartLayout.LINEAL)
            .agregarTodas(partes);

        var resultado = ensamblador.ensamblar();
        assertEquals(238, resultado.largoTotal());  // 17 + 51 + 51 + 51 + 51 + 17
        assertEquals(92, resultado.fondoTotal());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testFamiliaFactoryRegistradoModerno() {
        var factory = new FamiliaFactory();
        var config = new ConfigSofa(
            ConfigSofa.Modelo.MODERNO, 3, ConfigSofa.Chaise.NINGUNA, 92, null,
            ConfigSofa.Brazo.RECTO, ConfigSofa.Patas.MADERA, ConfigSofa.AcabadoMadera.NATURAL,
            ConfigSofa.Espuma.D23, 0, null, ConfigSofa.Mecanismo.FIJO
        );

        // Factory debe poder construir Moderno sin error
        var partes = factory.construir(SofaFamilia.MODERNO, config, materialTela(), null);
        assertNotNull(partes);
        assertTrue(partes.size() > 0, "Debe retornar al menos 1 parte");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testSeccionalLEsquinero() {
        var factory = new FamiliaFactory();
        var config = new ConfigSofa(
            ConfigSofa.Modelo.MODERNO, 3, ConfigSofa.Chaise.NINGUNA, 92, null,
            ConfigSofa.Brazo.RECTO, ConfigSofa.Patas.MADERA, ConfigSofa.AcabadoMadera.NATURAL,
            ConfigSofa.Espuma.D23, 0, null, ConfigSofa.Mecanismo.FIJO
        );

        // Familia nueva: L esquinero
        var partes = factory.construir(SofaFamilia.SECCIONAL_L_ESQUINERO, config, materialTela(), null);
        assertNotNull(partes);
        assertTrue(partes.size() > 5, "L esquinero debe tener varios módulos");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testTodas13FamiliasCompilan() {
        var factory = new FamiliaFactory();
        var config = new ConfigSofa(
            ConfigSofa.Modelo.MODERNO, 2, ConfigSofa.Chaise.NINGUNA, 92, null,
            ConfigSofa.Brazo.RECTO, ConfigSofa.Patas.MADERA, ConfigSofa.AcabadoMadera.NATURAL,
            ConfigSofa.Espuma.D23, 0, null, ConfigSofa.Mecanismo.FIJO
        );

        for (SofaFamilia familia : SofaFamilia.values()) {
            var partes = factory.construir(familia, config, materialTela(), null);
            assertNotNull(partes, "Familia " + familia + " debe compilar");
            assertTrue(partes.size() > 0, "Familia " + familia + " debe retornar partes");
        }
    }
}
