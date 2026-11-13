package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.Node;
import javafx.stage.FileChooser;

import java.io.File;

/** Dialogo "Guardar como" con la carpeta de Documentos y el nombre sugerido. */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Archivos {

    @EquipoPLMD_MGML_AMCS_DRJB
    private static File ultimaCarpeta;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Archivos() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static File guardar(Node ancla, String nombre, String descripcion, String extension) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar " + descripcion);
        fc.setInitialFileName(nombre + "." + extension);
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(descripcion, "*." + extension));
        File dir = ultimaCarpeta != null ? ultimaCarpeta : new File(System.getProperty("user.home"), "Documents");
        if (dir.isDirectory()) fc.setInitialDirectory(dir);
        File f = fc.showSaveDialog(ancla == null || ancla.getScene() == null ? null : ancla.getScene().getWindow());
        if (f != null) ultimaCarpeta = f.getParentFile();
        return f;
    }
}
