package pe.erp.sofas.ui;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pe.erp.sofas.core.ThemeManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/** Ventana principal: carga fuentes y tema, muestra el login y luego el shell. */
@EquipoPLMD_MGML_AMCS_DRJB
@Component
@Lazy
@RequiredArgsConstructor
@Slf4j
public class AppWindow {

    @EquipoPLMD_MGML_AMCS_DRJB
    private static final String[] FUENTES = {
            "Inter-Regular", "Inter-Medium", "Inter-SemiBold", "Inter-Bold", "Inter-ExtraBold",
            "JetBrainsMono-Regular", "JetBrainsMono-Medium", "JetBrainsMono-Bold"};

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Cargado<T>(@EquipoPLMD_MGML_AMCS_DRJB
      Parent nodo, @EquipoPLMD_MGML_AMCS_DRJB
        T controlador) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ApplicationContext context;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ThemeManager themeManager;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Stage stage;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Scene scene;
    @EquipoPLMD_MGML_AMCS_DRJB
    private HostServices hostServices;

    @EquipoPLMD_MGML_AMCS_DRJB
    public void iniciar(Stage stage, HostServices hostServices) {
        this.stage = stage;
        this.hostServices = hostServices;
        cargarFuentes();

        Rectangle2D pantalla = Screen.getPrimary().getVisualBounds();
        double ancho = Math.min(1440, pantalla.getWidth() - 40);
        double alto = Math.min(900, pantalla.getHeight() - 40);
        scene = new Scene(cargar("/fxml/login.fxml").nodo(), ancho, alto);
        themeManager.aplicar(scene);

        stage.setTitle("SISTEMA ERP SOFAS · ERP de sofás · Juliaca");
        stage.setScene(scene);
        stage.setMinWidth(1180);
        stage.setMinHeight(720);
        try (InputStream icono = getClass().getResourceAsStream("/images/icono.png")) {
            if (icono != null) stage.getIcons().add(new Image(icono));
        } catch (IOException ignored) {
            // sin icono de ventana
        }
        stage.show();

        String tour = System.getProperty("innova.tour");
        if (tour != null) {
            context.getBean(DemoTour.class).ejecutar(tour);
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void mostrarLogin() {
        scene.setRoot(cargar("/fxml/login.fxml").nodo());
        themeManager.reaplicar();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void mostrarShell(String moduloInicio) {
        Cargado<ShellController> shell = cargar("/fxml/shell.fxml");
        scene.setRoot(shell.nodo());
        themeManager.reaplicar();
        shell.controlador().arrancar(moduloInicio);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public <T> Cargado<T> cargar(String ruta) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
        loader.setControllerFactory(context::getBean);
        // Explicito: en macOS el hilo de eventos puede no tener class loader de contexto.
        loader.setClassLoader(getClass().getClassLoader());
        try {
            Parent nodo = loader.load();
            return new Cargado<>(nodo, loader.getController());
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar " + ruta, e);
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Stage stage() {
        return stage;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Scene scene() {
        return scene;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public HostServices hostServices() {
        return hostServices;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private void cargarFuentes() {
        for (String f : FUENTES) {
            try (InputStream in = getClass().getResourceAsStream("/fonts/" + f + ".ttf")) {
                Font font = in == null ? null : Font.loadFont(in, 13);
                if (font != null) log.debug("Fuente {} → familia '{}'", f, font.getFamily());
            } catch (IOException e) {
                log.warn("No se pudo cargar la fuente {}", f);
            }
        }
    }
}
