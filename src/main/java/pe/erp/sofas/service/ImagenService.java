package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.erp.sofas.core.NegocioException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Guarda las fotos en {@code ~/.innovasur/images/{id}.jpg}, recortadas en modo cover a la proporcion pedida.
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
public class ImagenService {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final File carpeta;

    @EquipoPLMD_MGML_AMCS_DRJB
    public ImagenService(@Value("${app.db.dir:${user.home}/.innovasur}") String dir) {
        this.carpeta = new File(dir, "images");
        carpeta.mkdirs();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public File carpeta() {
        return carpeta;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public File archivo(String id) {
        return new File(carpeta, id + ".jpg");
    }

    /** Recorta (cover) a {@code ancho × alto} y guarda como JPG. Devuelve la ruta absoluta. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public String guardar(File origen, String id, int ancho, int alto) {
        String n = origen.getName().toLowerCase(Locale.ROOT);
        if (!(n.endsWith(".jpg") || n.endsWith(".jpeg") || n.endsWith(".png") || n.endsWith(".webp"))) {
            throw new NegocioException("Formato no soportado: usa JPG, PNG o WebP.");
        }
        BufferedImage img;
        try {
            img = ImageIO.read(origen);
        } catch (IOException e) {
            img = null;
        }
        if (img == null) {
            throw new NegocioException(n.endsWith(".webp")
                    ? "Este equipo no puede leer WebP; convierte la foto a JPG o PNG."
                    : "No se pudo leer la imagen.");
        }
        BufferedImage recorte = cover(img, ancho, alto);
        File destino = archivo(id);
        try {
            ImageIO.write(recorte, "jpg", destino);
        } catch (IOException e) {
            throw new NegocioException("No se pudo guardar la imagen: " + e.getMessage());
        }
        return destino.getAbsolutePath();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static BufferedImage cover(BufferedImage img, int ancho, int alto) {
        double ratio = ancho / (double) alto;
        int w = img.getWidth(), h = img.getHeight();
        int vw = (int) Math.min(w, Math.round(h * ratio));
        int vh = (int) Math.round(vw / ratio);
        int x = (w - vw) / 2, y = (h - vh) / 2;
        BufferedImage out = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ancho, alto);
        g.drawImage(img, 0, 0, ancho, alto, x, y, x + vw, y + vh, null);
        g.dispose();
        return out;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void eliminar(String ruta) {
        if (ruta == null) return;
        File f = new File(ruta);
        if (f.getParentFile() != null && f.getParentFile().equals(carpeta)) f.delete();
    }
}
