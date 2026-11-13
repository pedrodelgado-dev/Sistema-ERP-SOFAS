package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/** Captura un nodo como PNG (para incrustar graficos en los PDF) sin depender de javafx-swing. */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Snapshot {

    @EquipoPLMD_MGML_AMCS_DRJB
    private Snapshot() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static BufferedImage imagen(WritableImage img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        PixelReader pr = img.getPixelReader();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) bi.setRGB(x, y, pr.getArgb(x, y));
        }
        return bi;
    }

    /** PNG a doble resolucion con fondo blanco; null si el nodo no esta disponible. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static byte[] png(Node nodo) {
        if (nodo == null || nodo.getScene() == null) return null;
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.WHITE);
        sp.setTransform(Transform.scale(2, 2));
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(imagen(nodo.snapshot(sp, null)), "png", out);
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
}
