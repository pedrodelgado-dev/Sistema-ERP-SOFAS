package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Reloj de la demo (AppClock). Toda la aplicacion usa esta fecha como "hoy" para que los datos de ejemplo
 * (retrasos, vencimientos, calendario) se vean igual que en el prototipo aprobado.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public final class AppClock {

    /** Fecha actual de la demo: jueves, 15 de octubre de 2026. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final LocalDate HOY = LocalDate.of(2026, 10, 15);
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final LocalTime HORA = LocalTime.of(10, 30);

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final Locale PERU = Locale.forLanguageTag("es-PE");
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final DateTimeFormatter FECHA_CORTA = DateTimeFormatter.ofPattern("dd/MM");
    @EquipoPLMD_MGML_AMCS_DRJB
    private static final DateTimeFormatter LARGA = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", PERU);
    @EquipoPLMD_MGML_AMCS_DRJB
    private static final DateTimeFormatter DIA_MES = DateTimeFormatter.ofPattern("d MMM", PERU);

    @EquipoPLMD_MGML_AMCS_DRJB
    private AppClock() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static LocalDate hoy() {
        return HOY;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String horaActual() {
        return HORA.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /** "Jueves, 15 de octubre de 2026". */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static String fechaLarga() {
        return capitalizar(HOY.format(LARGA));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String fmt(LocalDate fecha) {
        return fecha == null ? "—" : fecha.format(FECHA);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String corta(LocalDate fecha) {
        return fecha == null ? "—" : fecha.format(FECHA_CORTA);
    }

    /** "15 oct". */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static String diaMes(LocalDate fecha) {
        return fecha == null ? "—" : fecha.format(DIA_MES).replace(".", "");
    }

    /** Nombre corto del mes: "Oct". */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static String mesCorto(int mes) {
        String m = capitalizar(java.time.Month.of(mes).getDisplayName(java.time.format.TextStyle.SHORT, PERU)).replace(".", "");
        return m.equals("Set") ? "Sep" : m;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String mesLargo(int mes) {
        return capitalizar(java.time.Month.of(mes).getDisplayName(java.time.format.TextStyle.FULL, PERU));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String capitalizar(String s) {
        return s == null || s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
