package pe.erp.sofas.ui.three.parts.families;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.paint.PhongMaterial;
import pe.erp.sofas.model.ConfigSofa;
import pe.erp.sofas.ui.three.parts.SofaPart;
import pe.erp.sofas.ui.three.parts.concrete.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Familia Moderno: sofá lineal con brazo recto ancho, cojines sueltos, respaldo de cojines.
 * Características:
 * - 1 a 4 cuerpos (52 cm c/u)
 * - Brazo recto (17 cm ancho)
 * - Cojines de asiento y respaldo sueltos
 * - Patas de madera cónica (por defecto)
 *
 * Esta es la familia de VALIDACIÓN: debe renderizar idéntico al SofaBuilder existente.
 * Código: SF-001
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class ModernoFamily implements FamiliaFactory.FamiliaConstructor {

    @EquipoPLMD_MGML_AMCS_DRJB
    private static final double ANCHO_CUERPO = 52;  // Ancho de un cuerpo en cm
    @EquipoPLMD_MGML_AMCS_DRJB
    private static final double FONDO_ESTANDAR = 92;
    @EquipoPLMD_MGML_AMCS_DRJB
    private static final double ALTO_ESTANDAR = 85;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public List<SofaPart> construir(ConfigSofa config, PhongMaterial materialTela, PhongMaterial materialRespaldo) {
        List<SofaPart> partes = new ArrayList<>();

        int cuerpos = config.cuerpos();
        double fondo = config.profundidad();
        double alto = ALTO_ESTANDAR;

        // Material del respaldo (si es diferente)
        PhongMaterial matResp = materialRespaldo == null ? materialTela : materialRespaldo;

        // Partes ensambladas LINEALMENTE en X: solo lo que suma ancho secuencial
        // Brazo izquierdo (17 cm) + N cojines (52 cm c/u) + Brazo derecho (17 cm)
        // Base, respaldo, patas ocupan el MISMO espacio en X, se renderizan superpuestos

        // 1. Brazo izquierdo
        partes.add(new ArmPart(ArmPart.TipoBrazo.RECTO, alto * 0.7, fondo, materialTela));

        // 2. Cojines de asiento (uno por cuerpo)
        for (int i = 0; i < cuerpos; i++) {
            partes.add(new SeatCushionPart(ANCHO_CUERPO - 1, fondo, materialTela));
        }

        // 3. Brazo derecho
        partes.add(new ArmPart(ArmPart.TipoBrazo.RECTO, alto * 0.7, fondo, materialTela));

        return partes;
    }

    /**
     * Calcula el largo total para N cuerpos.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static double largoParaCuerpos(int cuerpos) {
        return cuerpos * ANCHO_CUERPO;
    }
}
