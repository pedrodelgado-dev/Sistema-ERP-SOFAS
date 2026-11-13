package pe.erp.sofas.ui.three.parts;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import java.util.Map;

/**
 * Interfaz base para partes modulares de un sofá. Cada parte es responsable de:
 * - Generar su geometría (build)
 * - Reportar sus dimensiones físicas (ancho, fondo, alto)
 * - Proporcionar anclajes para encadenamiento de módulos (izq, der, frente)
 */
@EquipoPLMD_MGML_AMCS_DRJB
public interface SofaPart {

    /**
     * Construye la geometría de esta parte y la retorna como Group listo para posicionar.
     * El Group debe estar centrado en el origen, con Y hacia arriba.
     *
     * @return Group con la geometría de la parte (mallas, materiales, etc)
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    Group build();

    /**
     * Retorna las dimensiones físicas de esta parte en centímetros.
     * @return Dimensiones (ancho, fondo, alto)
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    PartDimensions getDimensions();

    /**
     * Retorna los puntos de anclaje para encadenar módulos.
     * Claves comunes: "left", "right", "front", "back"
     * @return Mapa de etiqueta → punto 3D
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    Map<String, Punto3D> getAnchors();

    /**
     * Información sobre esta parte para debugging.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    String getName();

    /**
     * Dimensiones físicas de una parte.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    record PartDimensions(
        @EquipoPLMD_MGML_AMCS_DRJB
        double ancho,    // eje X
        @EquipoPLMD_MGML_AMCS_DRJB
        double fondo,    // eje Z
        @EquipoPLMD_MGML_AMCS_DRJB
        double alto      // eje Y
    ) {}

    /**
     * Punto en 3D para anclajes.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    record Punto3D(@EquipoPLMD_MGML_AMCS_DRJB
     double x, @EquipoPLMD_MGML_AMCS_DRJB
       double y, @EquipoPLMD_MGML_AMCS_DRJB
         double z) {}
}
