package pe.erp.sofas.ui.three.parts.concrete;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import pe.erp.sofas.ui.three.parts.BaseSofaPart;

/**
 * Esquinero: módulo cuadrado de 90° para seccionales en L y U.
 * Tiene respaldo en sus dos lados interiores (norte y este por ejemplo).
 * Cojín de asiento cuadrado en el centro.
 * Tamaño típico: 95×95 o 105×105 cm.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class CornerPart extends BaseSofaPart {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final PhongMaterial materialAsiento;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PhongMaterial materialRespaldo;

    @EquipoPLMD_MGML_AMCS_DRJB
    public CornerPart(double tamanio, PhongMaterial materialAsiento, PhongMaterial materialRespaldo) {
        super("Esquinero", tamanio, tamanio, 85);  // Cuadrado, altura 85 cm estándar
        this.materialAsiento = materialAsiento == null ? materialPorDefecto() : materialAsiento;
        this.materialRespaldo = materialRespaldo == null ? materialPorDefecto() : materialRespaldo;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public Group build() {
        Group g = new Group();

        // Base del esquinero (igual al asiento)
        agregar(g, cajaRedondeada(ancho, 18, fondo, 3, materialAsiento), 0, 18 / 2, 0);

        // Cojín de asiento cuadrado en el centro
        double alturaCojin = 14;
        agregar(g, cajaRedondeada(ancho - 4, alturaCojin, fondo - 4, 5, materialAsiento),
                0, 18 + alturaCojin / 2, 0);

        // Respaldos: dos paneles perpendiculares (norte y este)
        double altoResp = alto - 18 - 6;  // Alto total menos base y espacio

        // Respaldo norte (eje +Z)
        agregar(g, cajaRedondeada(ancho - 4, altoResp, 20, 6, materialRespaldo),
                0, 18 + altoResp / 2, fondo / 2 - 10);

        // Respaldo este (eje +X)
        agregar(g, cajaRedondeada(20, altoResp, fondo - 4, 6, materialRespaldo),
                ancho / 2 - 10, 18 + altoResp / 2, 0);

        return g;
    }
}
