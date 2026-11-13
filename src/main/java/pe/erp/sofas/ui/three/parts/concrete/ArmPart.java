package pe.erp.sofas.ui.three.parts.concrete;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import pe.erp.sofas.ui.three.parts.BaseSofaPart;

/**
 * Brazo del sofá: puede ser recto, redondeado o delgado.
 * Tipos: RECTO (17 cm ancho), REDONDEADO (18 cm), DELGADO (9 cm).
 * Altura: variable según el tipo (típico 70% del alto total del sofá).
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class ArmPart extends BaseSofaPart {

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum TipoBrazo {
        @EquipoPLMD_MGML_AMCS_DRJB
        RECTO(17, 4),
        @EquipoPLMD_MGML_AMCS_DRJB
        REDONDEADO(18, 9),
        @EquipoPLMD_MGML_AMCS_DRJB
        DELGADO(9, 3);

        @EquipoPLMD_MGML_AMCS_DRJB
        final double ancho;
        @EquipoPLMD_MGML_AMCS_DRJB
        final double radio;

        @EquipoPLMD_MGML_AMCS_DRJB
        TipoBrazo(double ancho, double radio) {
            this.ancho = ancho;
            this.radio = radio;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final TipoBrazo tipo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PhongMaterial material;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final double altoBrazo;

    @EquipoPLMD_MGML_AMCS_DRJB
    public ArmPart(TipoBrazo tipo, double altoBrazo, double fondo, PhongMaterial material) {
        super("Brazo " + tipo.name(), tipo.ancho, fondo, altoBrazo);
        this.tipo = tipo;
        this.material = material == null ? materialPorDefecto() : material;
        this.altoBrazo = altoBrazo;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public Group build() {
        Group g = new Group();

        // Cuerpo principal del brazo
        agregar(g, cajaRedondeada(ancho, altoBrazo, fondo, tipo.radio, material), 0, altoBrazo / 2, 0);

        // Si es redondeado, agregar un cilindro decorativo enrollado
        if (tipo == TipoBrazo.REDONDEADO) {
            Cylinder rollo = new Cylinder(ancho / 2 + 1, fondo - 2, 28);
            rollo.setMaterial(material);
            rollo.getTransforms().addAll(
                new Translate(0, -(altoBrazo - 1), 0),
                new Rotate(90, Rotate.X_AXIS)
            );
            g.getChildren().add(rollo);
        }

        return g;
    }
}
