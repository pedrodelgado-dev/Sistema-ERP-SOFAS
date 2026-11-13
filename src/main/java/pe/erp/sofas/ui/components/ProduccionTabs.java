package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import pe.erp.sofas.core.Modulo;
import pe.erp.sofas.core.Navigator;

/**
 * Pestanas de Produccion: Tablero · Fichas tecnicas · Requerimiento de materiales · Hojas de corte · Consumo y mermas,
 * mas el boton "Calculadora de materiales". Cada pestana es un modulo propio del menu.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class ProduccionTabs extends HBox {

    @EquipoPLMD_MGML_AMCS_DRJB
    public ProduccionTabs(Modulo actual, Navigator navigator, Runnable calculadora) {
        getStyleClass().add("prod-tabs");
        SegmentedControl tabs = new SegmentedControl();
        tabs.getStyleClass().add("tabs");
        String[][] defs = {{"produccion", "Tablero", "VIEW_WEEK"}, {"fichas", "Fichas técnicas", "DESCRIPTION"},
                {"mrp", "Requerimiento de materiales", "FACT_CHECK"}, {"corte", "Hojas de corte", "CONTENT_CUT"},
                {"consumo", "Consumo y mermas", "ANALYTICS"}};
        for (String[] d : defs) tabs.agregar(d[1], d[2]);
        for (String[] d : defs) if (d[0].equals(actual.clave())) tabs.setValor(d[1]);
        tabs.valorProperty().addListener((o, a, b) -> {
            for (String[] d : defs) if (d[1].equals(b) && !d[0].equals(actual.clave())) navigator.goTo(d[0]);
        });
        Button calc = UI.secondary("Calculadora de materiales", "CALCULATE");
        calc.setOnAction(e -> calculadora.run());
        getChildren().addAll(tabs, UI.spacer(), calc);
        setMinHeight(USE_PREF_SIZE);
    }
}
