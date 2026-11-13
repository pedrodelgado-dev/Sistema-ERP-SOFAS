package pe.erp.sofas.ui.views.comparador;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pe.erp.sofas.core.Intent;
import pe.erp.sofas.core.Session;
import pe.erp.sofas.core.Toast;
import pe.erp.sofas.core.Vista;
import pe.erp.sofas.model.ConfigSofa;
import pe.erp.sofas.service.ConfiguradorService;
import pe.erp.sofas.service.GestorCarritoService;
import pe.erp.sofas.service.PersistenciaConfiguracionService;
import pe.erp.sofas.service.TelaService;
import pe.erp.sofas.ui.AppWindow;
import pe.erp.sofas.ui.components.UI;
import pe.erp.sofas.ui.three.SofaBuilder;
import pe.erp.sofas.ui.three.Viewer3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Comparador A/B/C: vista de 3 sofás lado a lado con sincronización de rotación.
 *
 * Permite comparar hasta 3 configuraciones diferentes en tiempo real.
 * Al rotar uno, todos los demás rotan al mismo tiempo.
 * Útil para decidir entre opciones de color, tamaño, etc.
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Component
@Lazy
@Scope("prototype")
@RequiredArgsConstructor
public class ComparadorController implements Vista.ConCiclo {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ConfiguradorService configurador;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final TelaService telas;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Toast toast;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final AppWindow appWindow;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final GestorCarritoService gestorCarrito;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PersistenciaConfiguracionService persistencia;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final Session session;

    @EquipoPLMD_MGML_AMCS_DRJB
    private final SofaBuilder builder = new SofaBuilder();
    @EquipoPLMD_MGML_AMCS_DRJB
    private final List<Viewer3D> visores = new ArrayList<>();
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ConfigSofa[] configs = new ConfigSofa[3];

    @EquipoPLMD_MGML_AMCS_DRJB
    private VBox raiz;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Label[] labelConfiguraciones;

    @EquipoPLMD_MGML_AMCS_DRJB
    private void inicializar() {
        raiz = new VBox(10);
        raiz.setPadding(new Insets(12));
        raiz.setStyle("-fx-background-color: #f5f5f5;");

        // Título
        Label titulo = UI.label("Comparador A/B/C", "title");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Contenedor de 3 visores lado a lado
        HBox comparador = crearComparador();
        VBox.setVgrow(comparador, Priority.ALWAYS);

        // Panel de información (tabla comparativa)
        VBox infoPanel = crearPanelInfo();

        raiz.getChildren().addAll(titulo, comparador, infoPanel);
    }

    /**
     * Crea 3 visores sincronizados lado a lado.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    private HBox crearComparador() {
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(10));
        hbox.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 8;");

        labelConfiguraciones = new Label[3];

        for (int i = 0; i < 3; i++) {
            VBox visorPanel = new VBox(5);
            visorPanel.setStyle("-fx-border-color: #ddd; -fx-border-radius: 4; -fx-padding: 10;");

            // Etiqueta
            labelConfiguraciones[i] = UI.label("Opción " + (i + 1), "card");
            labelConfiguraciones[i].setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            // Visor 3D
            Viewer3D visor = new Viewer3D(true);
            visor.setMinSize(300, 400);
            visores.add(visor);

            // Sincronizar rotación: cuando uno rota, todos rotan
            final int index = i;
            visor.yawProperty().addListener((obs, oldV, newV) -> {
                for (int j = 0; j < 3; j++) {
                    if (j != index) {
                        visores.get(j).yawProperty().setValue(newV);
                    }
                }
            });
            visor.pitchProperty().addListener((obs, oldV, newV) -> {
                for (int j = 0; j < 3; j++) {
                    if (j != index) {
                        visores.get(j).pitchProperty().setValue(newV);
                    }
                }
            });

            // Botón agregar al carrito
            Button btnAgregarCarrito = UI.button("Agregar al carrito", null, "btn-primary");
            btnAgregarCarrito.setMaxWidth(Double.MAX_VALUE);
            btnAgregarCarrito.setOnAction(e -> agregarAlCarrito(index));

            visorPanel.getChildren().addAll(labelConfiguraciones[i], visor, btnAgregarCarrito);
            HBox.setHgrow(visorPanel, Priority.ALWAYS);
            hbox.getChildren().add(visorPanel);

            // Configuración inicial
            if (i == 0) {
                configs[i] = configurador.defecto(ConfigSofa.Modelo.MODERNO);
            } else {
                configs[i] = null;  // Vacío hasta que se cargue
            }
        }

        return hbox;
    }

    /**
     * Panel de información con tabla comparativa.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    private VBox crearPanelInfo() {
        VBox panel = new VBox(8);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ddd; -fx-border-radius: 4; -fx-background-color: #fafafa;");

        Label titulo = UI.label("Comparativa", "subtitle");
        titulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Tabla de comparación simple (será expandida en integración real)
        TableView<ComparisonRow> tabla = new TableView<>();
        tabla.setMinHeight(150);
        tabla.setMaxHeight(300);

        TableColumn<ComparisonRow, String> propiedadCol = new TableColumn<>("Propiedad");
        propiedadCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().propiedad));
        propiedadCol.setPrefWidth(100);

        TableColumn<ComparisonRow, String> opcion1Col = new TableColumn<>("Opción A");
        opcion1Col.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().opcion1));
        opcion1Col.setPrefWidth(120);

        TableColumn<ComparisonRow, String> opcion2Col = new TableColumn<>("Opción B");
        opcion2Col.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().opcion2));
        opcion2Col.setPrefWidth(120);

        TableColumn<ComparisonRow, String> opcion3Col = new TableColumn<>("Opción C");
        opcion3Col.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().opcion3));
        opcion3Col.setPrefWidth(120);

        tabla.getColumns().addAll(propiedadCol, opcion1Col, opcion2Col, opcion3Col);

        // Datos de ejemplo
        tabla.getItems().addAll(
            new ComparisonRow("Modelo", "Moderno", "-", "-"),
            new ComparisonRow("Cuerpos", "3", "-", "-"),
            new ComparisonRow("Largo", "210 cm", "-", "-"),
            new ComparisonRow("Tela", "Chenille gris", "-", "-"),
            new ComparisonRow("Precio", "S/ 3,500", "-", "-")
        );

        panel.getChildren().addAll(titulo, tabla);
        return panel;
    }

    /**
     * Carga una configuración en una posición específica (A, B, o C).
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    public void cargarConfiguracion(int posicion, ConfigSofa config) {
        if (posicion < 0 || posicion >= 3) return;

        configs[posicion] = config;
        var tela = config.telaId() != null ? telas.tela(config.telaId()) : null;
        var telaCojines = config.telaCojinesId() != null ? telas.tela(config.telaCojinesId()) : null;

        Group sofaGeom = builder.build(SofaBuilder.Opciones.de(config, tela, telaCojines));
        visores.get(posicion).setContenido(sofaGeom, true);

        // Actualizar etiqueta
        labelConfiguraciones[posicion].setText(config.modelo().nombre());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public void onShow(Intent intent) {
        if (raiz == null) {
            inicializar();
            // Cargar primera configuración después de inicializar
            if (configs[0] != null && labelConfiguraciones != null) {
                cargarConfiguracion(0, configs[0]);
            }
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public void onHide() {
        // Limpiar recursos
        visores.forEach(Viewer3D::detenerGiro);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private void agregarAlCarrito(int posicion) {
        if (configs[posicion] == null) {
            toast.advertencia("Opción " + (posicion + 1) + " no configurada");
            return;
        }

        try {
            String nombre = "Comparador - " + configs[posicion].modelo().nombre();
            var configGuardada = persistencia.guardar(session.usuario(), configs[posicion], nombre, "");
            gestorCarrito.agregarAlCarrito(session.usuario(), configGuardada.getId(), 1);
            toast.exito("Agregado al carrito: " + nombre);
        } catch (Exception e) {
            toast.error("Error: " + e.getMessage());
        }
    }

    /**
     * Fila de la tabla comparativa.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static class ComparisonRow {
        @EquipoPLMD_MGML_AMCS_DRJB
        public String propiedad;
        @EquipoPLMD_MGML_AMCS_DRJB
        public String opcion1;
        @EquipoPLMD_MGML_AMCS_DRJB
        public String opcion2;
        @EquipoPLMD_MGML_AMCS_DRJB
        public String opcion3;

        @EquipoPLMD_MGML_AMCS_DRJB
        public ComparisonRow(String propiedad, String opcion1, String opcion2, String opcion3) {
            this.propiedad = propiedad;
            this.opcion1 = opcion1;
            this.opcion2 = opcion2;
            this.opcion3 = opcion3;
        }
    }
}
