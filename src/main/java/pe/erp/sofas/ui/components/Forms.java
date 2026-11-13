package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.control.*;
import javafx.util.StringConverter;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.core.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.function.Function;

/** Controles de formulario con filtros de entrada y conversion. */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Forms {

    @EquipoPLMD_MGML_AMCS_DRJB
    private Forms() {
    }

    /** Campo numerico decimal (acepta digitos, punto y coma de miles). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static TextField decimal(BigDecimal valor) {
        TextField t = new TextField(valor == null ? "" : valor.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        t.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().matches("[0-9.,]*") ? c : null));
        t.getStyleClass().add("num-field");
        return t;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static TextField decimal(double valor) {
        TextField t = decimal((BigDecimal) null);
        t.setText(valor == Math.floor(valor) ? String.valueOf((long) valor) : String.valueOf(valor));
        return t;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static TextField entero(Integer valor) {
        TextField t = new TextField(valor == null ? "" : String.valueOf(valor));
        t.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().matches("\\d{0,7}") ? c : null));
        return t;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static TextField digitos(String valor, int max) {
        TextField t = new TextField(valor == null ? "" : valor);
        t.setTextFormatter(new TextFormatter<String>(c -> c.getControlNewText().matches("\\d{0," + max + "}") ? c : null));
        return t;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static BigDecimal dinero(TextField t) {
        return Money.parse(t.getText());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static double numero(TextField t) {
        BigDecimal b = Money.parse(t.getText());
        return b == null ? 0 : b.doubleValue();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static int entero(TextField t) {
        try {
            return Integer.parseInt(t.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static <T> ComboBox<T> combo(Collection<T> items, T seleccion, Function<T, String> texto) {
        ComboBox<T> c = new ComboBox<>();
        c.getItems().setAll(items);
        c.setMaxWidth(Double.MAX_VALUE);
        Function<T, String> seguro = t -> {
            try {
                return texto.apply(t);
            } catch (NullPointerException e) {
                return "";
            }
        };
        c.setConverter(new StringConverter<>() {
            @EquipoPLMD_MGML_AMCS_DRJB
            @Override
            public String toString(T t) {
                return seguro.apply(t);
            }

            @EquipoPLMD_MGML_AMCS_DRJB
            @Override
            public T fromString(String s) {
                return null;
            }
        });
        // Celdas propias para que la opcion nula ("Todos", "Según ficha"...) tambien muestre su texto.
        javafx.util.Callback<ListView<T>, ListCell<T>> fabrica = lv -> new ListCell<>() {
            @EquipoPLMD_MGML_AMCS_DRJB
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty && item == null && getIndex() < 0 ? seguro.apply(null) : empty ? null : seguro.apply(item));
            }
        };
        c.setCellFactory(fabrica);
        c.setButtonCell(new ListCell<>() {
            @EquipoPLMD_MGML_AMCS_DRJB
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(seguro.apply(item));
            }
        });
        c.setValue(seleccion);
        return c;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static DatePicker fecha(LocalDate valor) {
        DatePicker d = new DatePicker(valor);
        d.setMaxWidth(Double.MAX_VALUE);
        d.setConverter(new StringConverter<>() {
            @EquipoPLMD_MGML_AMCS_DRJB
            @Override
            public String toString(LocalDate f) {
                return f == null ? "" : f.format(AppClock.FECHA);
            }

            @EquipoPLMD_MGML_AMCS_DRJB
            @Override
            public LocalDate fromString(String s) {
                try {
                    return s == null || s.isBlank() ? null : LocalDate.parse(s.trim(), AppClock.FECHA);
                } catch (Exception e) {
                    return null;
                }
            }
        });
        d.setPromptText("dd/mm/aaaa");
        return d;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static TextArea area(String valor, String prompt, int filas) {
        TextArea a = new TextArea(valor == null ? "" : valor);
        a.setPromptText(prompt);
        a.setPrefRowCount(filas);
        a.setWrapText(true);
        return a;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static TextField soloLectura(String valor) {
        TextField t = new TextField(valor);
        t.setEditable(false);
        t.setFocusTraversable(false);
        t.getStyleClass().add("readonly-field");
        return t;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String txt(TextInputControl t) {
        return t.getText() == null ? "" : t.getText().trim();
    }
}
