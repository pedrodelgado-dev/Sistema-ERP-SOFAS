package pe.erp.sofas.ui.three;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

/**
 * Visor 3D: SubScene con PerspectiveCamera, orbita con arrastre, zoom con la rueda, doble clic para centrar,
 * camaras predefinidas con transicion animada, giro automatico 360° (AnimationTimer que se pausa cuando
 * el visor no esta en pantalla), piso con sombra suave y luz de estudio (ambiental + 2 direccionales).
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class Viewer3D extends Region {

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Camara { @EquipoPLMD_MGML_AMCS_DRJB
        FRONTAL("Frontal", 0, 8), @EquipoPLMD_MGML_AMCS_DRJB
           LATERAL("Lateral", 90, 8), @EquipoPLMD_MGML_AMCS_DRJB
              TRES_CUARTOS("3/4", -35, 20), @EquipoPLMD_MGML_AMCS_DRJB
                 SUPERIOR("Superior", 0, 80);

        @EquipoPLMD_MGML_AMCS_DRJB
        private final String nombre;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final double yaw;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final double pitch;

        @EquipoPLMD_MGML_AMCS_DRJB
        Camara(String nombre, double yaw, double pitch) {
            this.nombre = nombre;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String nombre() {
            return nombre;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final Group mundo = new Group();
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Group contenido = new Group();
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Group piso = new Group();
    @EquipoPLMD_MGML_AMCS_DRJB
    private final SubScene subScene;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PerspectiveCamera camara = new PerspectiveCamera(true);
    @EquipoPLMD_MGML_AMCS_DRJB
    private final DoubleProperty yaw = new SimpleDoubleProperty(-35);
    @EquipoPLMD_MGML_AMCS_DRJB
    private final DoubleProperty pitch = new SimpleDoubleProperty(20);
    @EquipoPLMD_MGML_AMCS_DRJB
    private final DoubleProperty distancia = new SimpleDoubleProperty(520);
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Rotate rotY = new Rotate(0, Rotate.Y_AXIS);
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Rotate rotX = new Rotate(0, Rotate.X_AXIS);
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Translate objetivo = new Translate(0, -40, 0);
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Translate alejar = new Translate(0, 0, -520);
    @EquipoPLMD_MGML_AMCS_DRJB
    private double mx, my;
    @EquipoPLMD_MGML_AMCS_DRJB
    private boolean girando;
    @EquipoPLMD_MGML_AMCS_DRJB
    private double distanciaBase = 520;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final AnimationTimer giro = new AnimationTimer() {
        @EquipoPLMD_MGML_AMCS_DRJB
        private long ultimo;

        @EquipoPLMD_MGML_AMCS_DRJB
        @Override
        public void handle(long now) {
            if (ultimo != 0) yaw.set(yaw.get() + (now - ultimo) / 1e9 * 30);
            ultimo = now;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        @Override
        public void stop() {
            super.stop();
            ultimo = 0;
        }
    };

    @EquipoPLMD_MGML_AMCS_DRJB
    public Viewer3D(boolean conPiso) {
        getStyleClass().add("viewer3d");
        camara.setFieldOfView(32);
        camara.setNearClip(1);
        camara.setFarClip(20000);
        Group rig = new Group(camara);
        camara.getTransforms().add(alejar);
        rig.getTransforms().addAll(objetivo, rotY, rotX);
        rotY.angleProperty().bind(yaw);
        rotX.angleProperty().bind(pitch.negate());
        alejar.zProperty().bind(distancia.negate());

        AmbientLight ambiente = new AmbientLight(Color.gray(0.52));
        DirectionalLight principal = new DirectionalLight(Color.gray(0.62));
        principal.setDirection(new javafx.geometry.Point3D(-0.6, 1, 0.8));
        DirectionalLight relleno = new DirectionalLight(Color.gray(0.28));
        relleno.setDirection(new javafx.geometry.Point3D(0.8, 0.6, -0.5));
        PointLight brillo = new PointLight(Color.gray(0.18));
        brillo.setTranslateY(-400);
        brillo.setTranslateZ(-300);
        mundo.getChildren().addAll(ambiente, principal, relleno, brillo, piso, contenido, rig);
        if (conPiso) construirPiso(600, 600);

        subScene = new SubScene(mundo, 400, 300, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camara);
        subScene.setFill(Color.TRANSPARENT);
        getChildren().add(subScene);

        subScene.setOnMousePressed(e -> {
            mx = e.getSceneX();
            my = e.getSceneY();
            if (girando) detenerGiro();
        });
        subScene.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mx, dy = e.getSceneY() - my;
            mx = e.getSceneX();
            my = e.getSceneY();
            if (e.getButton() == MouseButton.SECONDARY || e.isShiftDown()) {
                objetivo.setX(objetivo.getX() - dx * distancia.get() / 900);
                objetivo.setY(objetivo.getY() - dy * distancia.get() / 900);
            } else {
                yaw.set(yaw.get() + dx * 0.4);
                pitch.set(Math.max(-5, Math.min(85, pitch.get() + dy * 0.3)));
            }
        });
        subScene.setOnScroll(e -> distancia.set(Math.max(distanciaBase * 0.35, Math.min(distanciaBase * 3, distancia.get() * (e.getDeltaY() > 0 ? 0.92 : 1.08)))));
        subScene.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) centrar(true);
        });
        // Rendimiento: el giro solo corre cuando el visor esta en pantalla
        sceneProperty().addListener((o, a, s) -> {
            if (s == null) giro.stop();
            else if (girando) giro.start();
        });
        visibleProperty().addListener((o, a, v) -> {
            if (!v) giro.stop();
            else if (girando) giro.start();
        });
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private void construirPiso(double w, double d) {
        piso.getChildren().clear();
        MeshView suelo = new MeshView(Mallas.plano(9000, 9000));
        PhongMaterial ms = new PhongMaterial(Color.web("#EEF1F5"));
        ms.setSpecularColor(Color.gray(0.05));
        suelo.setMaterial(ms);
        suelo.setCullFace(CullFace.NONE);
        suelo.setTranslateY(0.2);
        MeshView sombra = new MeshView(Mallas.plano(w, d * 0.75));
        PhongMaterial msh = new PhongMaterial(Color.WHITE);
        msh.setDiffuseMap(Texturas.sombra());
        msh.setSpecularColor(Color.gray(0.01));
        sombra.setMaterial(msh);
        sombra.setCullFace(CullFace.NONE);
        sombra.setTranslateY(-0.1);
        piso.getChildren().addAll(suelo, sombra);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void setFondoPiso(String hex) {
        if (!piso.getChildren().isEmpty() && piso.getChildren().get(0) instanceof MeshView mv) {
            ((PhongMaterial) mv.getMaterial()).setDiffuseColor(Color.web(hex));
        }
    }

    /** Reemplaza el contenido (p. ej. el sofa reconstruido) conservando la camara. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public void setContenido(Node n, boolean recentrar) {
        contenido.getChildren().setAll(n);
        if (recentrar) centrar(false);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Group contenido() {
        return contenido;
    }

    /** Ajusta objetivo y distancia al tamaño del contenido. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public void centrar(boolean animar) {
        Bounds b = contenido.getBoundsInParent();
        if (b.isEmpty()) return;
        double tam = Math.max(b.getWidth(), Math.max(b.getHeight(), b.getDepth()));
        distanciaBase = Math.max(160, tam * 2.25);
        double cx = (b.getMinX() + b.getMaxX()) / 2, cy = (b.getMinY() + b.getMaxY()) / 2, cz = (b.getMinZ() + b.getMaxZ()) / 2;
        if (piso.getChildren().size() > 1) {
            piso.getChildren().get(1).setScaleX(Math.max(0.4, b.getWidth() / 420));
            piso.getChildren().get(1).setScaleZ(Math.max(0.4, b.getDepth() / 300));
            piso.getChildren().get(1).setTranslateX(cx);
            piso.getChildren().get(1).setTranslateZ(cz);
        }
        if (animar) {
            new Timeline(new KeyFrame(Duration.millis(450),
                    new KeyValue(objetivo.xProperty(), cx, Interpolator.EASE_BOTH),
                    new KeyValue(objetivo.yProperty(), cy, Interpolator.EASE_BOTH),
                    new KeyValue(objetivo.zProperty(), cz, Interpolator.EASE_BOTH),
                    new KeyValue(distancia, distanciaBase, Interpolator.EASE_BOTH))).play();
        } else {
            objetivo.setX(cx);
            objetivo.setY(cy);
            objetivo.setZ(cz);
            distancia.set(distanciaBase);
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void camara(Camara c) {
        detenerGiro();
        double destino = c.yaw;
        // Gira por el camino mas corto
        double actual = yaw.get() % 360;
        yaw.set(actual);
        double delta = ((destino - actual + 540) % 360) - 180;
        new Timeline(new KeyFrame(Duration.millis(650),
                new KeyValue(yaw, actual + delta, Interpolator.EASE_BOTH),
                new KeyValue(pitch, c.pitch, Interpolator.EASE_BOTH),
                new KeyValue(distancia, distanciaBase * (c == Camara.SUPERIOR ? 1.15 : 1), Interpolator.EASE_BOTH))).play();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean alternarGiro() {
        if (girando) detenerGiro();
        else {
            girando = true;
            if (getScene() != null && isVisible()) giro.start();
        }
        return girando;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void detenerGiro() {
        girando = false;
        giro.stop();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isGirando() {
        return girando;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public WritableImage captura(double escala) {
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.WHITE);
        sp.setTransform(javafx.scene.transform.Transform.scale(escala, escala));
        return subScene.snapshot(sp, null);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public SubScene subScene() {
        return subScene;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void fijarVista(double yawGrados, double pitchGrados) {
        yaw.set(yawGrados);
        pitch.set(pitchGrados);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public DoubleProperty yawProperty() {
        return yaw;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public DoubleProperty pitchProperty() {
        return pitch;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    protected void layoutChildren() {
        subScene.setWidth(getWidth());
        subScene.setHeight(getHeight());
    }

    /** Render fuera de pantalla de un nodo 3D (miniaturas de modelos sin foto). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static WritableImage render(Node n, double w, double h, double yawGrados, double pitchGrados) {
        Viewer3D v = new Viewer3D(true);
        v.subScene.setFill(Color.web("#EEF2F7"));
        // Escena temporal con depth buffer para que el snapshot pinte el 3D fuera de pantalla.
        Scene temporal = new Scene(new Group(v), w, h, true, SceneAntialiasing.BALANCED);
        temporal.setFill(Color.web("#EEF2F7"));
        v.resize(w, h);
        v.layoutChildren();
        v.setContenido(n, true);
        v.fijarVista(yawGrados, pitchGrados);
        v.distancia.set(v.distanciaBase * 0.95);
        return temporal.snapshot(null);
    }
}
