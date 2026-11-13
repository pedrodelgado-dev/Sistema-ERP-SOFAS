package pe.erp.sofas.ui.three.parts.concrete;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import pe.erp.sofas.ui.three.parts.BaseSofaPart;
import pe.erp.sofas.ui.three.Mallas;

/**
 * Patas del sofá: pueden ser madera cónica, metal, o zócalo oculto.
 * Se distribuyen en las esquinas del sofá.
 * Esta parte no tiene visualización directa (es accesoria);
 * más bien, es un placeholder para que otras partes sepan dónde van las patas.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class LegsPart extends BaseSofaPart {

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum TipoPata {
        @EquipoPLMD_MGML_AMCS_DRJB
        MADERA_CONICA(13),
        @EquipoPLMD_MGML_AMCS_DRJB
        METAL_NEGRO(16),
        @EquipoPLMD_MGML_AMCS_DRJB
        METAL_DORADO(16),
        @EquipoPLMD_MGML_AMCS_DRJB
        ZOCALO(7);

        @EquipoPLMD_MGML_AMCS_DRJB
        final double altura;

        @EquipoPLMD_MGML_AMCS_DRJB
        TipoPata(double altura) {
            this.altura = altura;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final TipoPata tipo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PhongMaterial material;

    @EquipoPLMD_MGML_AMCS_DRJB
    public LegsPart(double ancho, double fondo, TipoPata tipo, PhongMaterial material) {
        super("Patas " + tipo.name(), ancho, fondo, tipo.altura);
        this.tipo = tipo;
        this.material = material == null ? materialPorDefecto() : material;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public Group build() {
        Group g = new Group();

        // Las patas son pequeñas, solo dibujamos 4 en las esquinas
        double[] xs = {-ancho / 2 + 7, ancho / 2 - 7};
        double[] zs = {-fondo / 2 + 7, fondo / 2 - 7};

        for (double x : xs) {
            for (double z : zs) {
                pata(g, x, z, alto);
            }
        }

        return g;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private void pata(Group g, double x, double z, double h) {
        switch (tipo) {
            case ZOCALO -> {
                // Zócalo: solo un pequeño rectángulo oculto
                agregar(g, cajaRedondeada(2, h, 2, 0.5, material), x, h / 2, z);
            }
            default -> {
                // Cono o cilindro de metal
                Cylinder pata = new Cylinder(1.1, h, 14);
                pata.setMaterial(material);
                pata.setTranslateX(x);
                pata.setTranslateY(-h / 2);
                pata.setTranslateZ(z);
                g.getChildren().add(pata);
            }
        }
    }
}
