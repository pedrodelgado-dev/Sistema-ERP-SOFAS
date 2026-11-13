package pe.erp.sofas.ui.three.parts;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import org.junit.jupiter.api.Test;
import pe.erp.sofas.ui.three.parts.concrete.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests básicos de la arquitectura modular de partes.
 * Valida que:
 * - Las partes se construyen sin errores
 * - Los ensamblados retornan dimensiones correctas
 * - No hay regresiones con el SofaBuilder existente
 */
@EquipoPLMD_MGML_AMCS_DRJB
class SofaPartAssemblerTest {

    @EquipoPLMD_MGML_AMCS_DRJB
    private PhongMaterial materialTela() {
        PhongMaterial m = new PhongMaterial(Color.web("#B9B7B1"));
        m.setSpecularPower(8);
        return m;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private PhongMaterial materialMetal() {
        PhongMaterial m = new PhongMaterial(Color.web("#1F1F22"));
        m.setSpecularPower(30);
        return m;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testParteBaseSeatFrame() {
        var base = new BaseSeatFramePart(210, 92, 13, materialTela());
        var geom = base.build();
        var dims = base.getDimensions();

        assertNotNull(geom);
        assertEquals(210, dims.ancho());
        assertEquals(92, dims.fondo());
        assertEquals(18, dims.alto());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testParteSeatCushion() {
        var asiento = new SeatCushionPart(52, 92, materialTela());
        var geom = asiento.build();
        var dims = asiento.getDimensions();

        assertNotNull(geom);
        assertEquals(52, dims.ancho());
        assertEquals(92, dims.fondo());
        assertEquals(14, dims.alto());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testParteBackrest() {
        var respaldo = new BackrestPart(52, 85, materialTela());
        var geom = respaldo.build();
        var dims = respaldo.getDimensions();

        assertNotNull(geom);
        assertEquals(52, dims.ancho());
        assertEquals(20, dims.fondo());
        assertEquals(85, dims.alto());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testParteArmRecto() {
        var brazo = new ArmPart(ArmPart.TipoBrazo.RECTO, 60, 92, materialTela());
        var geom = brazo.build();
        var dims = brazo.getDimensions();

        assertNotNull(geom);
        assertEquals(17, dims.ancho());
        assertEquals(92, dims.fondo());
        assertEquals(60, dims.alto());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testParteCorner() {
        var esquina = new CornerPart(95, materialTela(), materialTela());
        var geom = esquina.build();
        var dims = esquina.getDimensions();

        assertNotNull(geom);
        assertEquals(95, dims.ancho());
        assertEquals(95, dims.fondo());
        assertEquals(85, dims.alto());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testParteLegsMadera() {
        var patas = new LegsPart(210, 92, LegsPart.TipoPata.MADERA_CONICA, materialTela());
        var geom = patas.build();
        var dims = patas.getDimensions();

        assertNotNull(geom);
        assertEquals(210, dims.ancho());
        assertEquals(92, dims.fondo());
        assertEquals(13, dims.alto());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testParteChaiseLoungeModule() {
        var chaise = new ChaseLoungeModule(65, materialTela());
        var geom = chaise.build();
        var dims = chaise.getDimensions();

        assertNotNull(geom);
        assertEquals(65, dims.ancho());
        assertEquals(92, dims.fondo());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testParteOttoman() {
        var puff = new OttomanPart(60, 60, materialTela());
        var geom = puff.build();
        var dims = puff.getDimensions();

        assertNotNull(geom);
        assertEquals(60, dims.ancho());
        assertEquals(60, dims.fondo());
        assertEquals(40, dims.alto());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testParteDecorativeCushion() {
        var cojin = new DecorativeCushionPart(44, materialTela());
        var geom = cojin.build();
        var dims = cojin.getDimensions();

        assertNotNull(geom);
        assertEquals(44, dims.ancho());
        assertEquals(44, dims.fondo());
        assertEquals(12, dims.alto());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testEnsambladorLineal3Cuerpos() {
        var material = materialTela();
        var asiento1 = new SeatCushionPart(52, 92, material);
        var asiento2 = new SeatCushionPart(52, 92, material);
        var asiento3 = new SeatCushionPart(52, 92, material);

        var ensamblador = new SofaPartAssembler()
            .conLayout(PartLayout.LINEAL)
            .agregar(asiento1)
            .agregar(asiento2)
            .agregar(asiento3);

        var resultado = ensamblador.ensamblar();
        assertNotNull(resultado.grupo());
        assertEquals(156, resultado.largoTotal());  // 52 + 52 + 52
        assertEquals(92, resultado.fondoTotal());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void testEnsambladorLEsquinero() {
        var material = materialTela();
        var base = new BaseSeatFramePart(343, 92, 13, material);
        var asientos1 = new SeatCushionPart(52, 92, material);
        var asientos2 = new SeatCushionPart(52, 92, material);
        var asientos3 = new SeatCushionPart(52, 92, material);
        var esquina = new CornerPart(95, material, material);
        var asientos4 = new SeatCushionPart(52, 92, material);
        var asientos5 = new SeatCushionPart(52, 92, material);

        var ensamblador = new SofaPartAssembler()
            .conLayout(PartLayout.L_ESQUINERO_DER)
            .agregar(base)
            .agregar(asientos1)
            .agregar(asientos2)
            .agregar(asientos3)
            .agregar(esquina)
            .agregar(asientos4)
            .agregar(asientos5);

        var resultado = ensamblador.ensamblar();
        assertNotNull(resultado.grupo());
        assertTrue(resultado.largoTotal() > 300);  // L es larga
    }
}
