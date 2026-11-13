package pe.erp.sofas.ui.three.parts;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Translate;
import pe.erp.sofas.ui.three.Mallas;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase base abstracta para partes de sofá modulares.
 * Proporciona utilidades comunes: crear cajas redondeadas, posicionar nodos, manejar materiales.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public abstract class BaseSofaPart implements SofaPart {

    @EquipoPLMD_MGML_AMCS_DRJB
    protected final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    protected final double ancho;
    @EquipoPLMD_MGML_AMCS_DRJB
    protected final double fondo;
    @EquipoPLMD_MGML_AMCS_DRJB
    protected final double alto;

    @EquipoPLMD_MGML_AMCS_DRJB
    public BaseSofaPart(String nombre, double ancho, double fondo, double alto) {
        this.nombre = nombre;
        this.ancho = ancho;
        this.fondo = fondo;
        this.alto = alto;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public PartDimensions getDimensions() {
        return new PartDimensions(ancho, fondo, alto);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public Map<String, Punto3D> getAnchors() {
        // Anclajes por defecto: esquinas del rectángulo de planta
        Map<String, Punto3D> anchors = new HashMap<>();
        anchors.put("left", new Punto3D(-ancho / 2, 0, 0));
        anchors.put("right", new Punto3D(ancho / 2, 0, 0));
        anchors.put("front", new Punto3D(0, 0, -fondo / 2));
        anchors.put("back", new Punto3D(0, 0, fondo / 2));
        return anchors;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String getName() {
        return nombre;
    }

    /**
     * Utilidad: crea una caja redondeada usando Mallas.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    protected MeshView cajaRedondeada(double w, double h, double d, double radio, PhongMaterial material) {
        MeshView mv = new MeshView(Mallas.cajaRedondeada(w, h, d, radio, 4));
        mv.setMaterial(material);
        mv.setCullFace(javafx.scene.shape.CullFace.NONE);
        return mv;
    }

    /**
     * Utilidad: suma un nodo a un Group en una posición específica.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    protected Node agregar(Group grupo, Node nodo, double x, double yCentro, double z) {
        nodo.setTranslateX(x);
        nodo.setTranslateY(-yCentro);
        nodo.setTranslateZ(z);
        grupo.getChildren().add(nodo);
        return nodo;
    }

    /**
     * Material gris claro por defecto.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    protected PhongMaterial materialPorDefecto() {
        return new PhongMaterial(javafx.scene.paint.Color.web("#B9B7B1"));
    }
}
