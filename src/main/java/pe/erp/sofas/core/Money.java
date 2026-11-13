package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** Formato de moneda peruana: {@code S/ 85,450.00}. */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Money {

    @EquipoPLMD_MGML_AMCS_DRJB
    private static final DecimalFormatSymbols SIMBOLOS = DecimalFormatSymbols.getInstance(Locale.US);

    @EquipoPLMD_MGML_AMCS_DRJB
    private Money() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String fmt(BigDecimal monto) {
        return "S/ " + df("#,##0.00").format(monto == null ? BigDecimal.ZERO : monto);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String fmt(double monto) {
        return fmt(BigDecimal.valueOf(monto));
    }

    /** Sin decimales: {@code S/ 85,450}. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static String fmt0(BigDecimal monto) {
        return "S/ " + df("#,##0").format(monto == null ? BigDecimal.ZERO : monto.setScale(0, RoundingMode.HALF_UP));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String fmt0(double monto) {
        return fmt0(BigDecimal.valueOf(monto));
    }

    /** Abreviado para ejes de graficos: {@code S/ 85.4k}. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static String corto(double monto) {
        if (Math.abs(monto) >= 1000) return "S/ " + df("#,##0.#").format(monto / 1000) + "k";
        return "S/ " + df("#,##0").format(monto);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String numero(double n) {
        return df("#,##0.##").format(n);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String entero(long n) {
        return df("#,##0").format(n);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static BigDecimal of(String valor) {
        return new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static BigDecimal of(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    /** Interpreta texto escrito por el usuario ("1,250.50", "S/ 300"); devuelve null si no es valido. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static BigDecimal parse(String texto) {
        if (texto == null) return null;
        String limpio = texto.replace("S/", "").replace(",", "").trim();
        if (limpio.isEmpty()) return null;
        try {
            return new BigDecimal(limpio).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static DecimalFormat df(String patron) {
        return new DecimalFormat(patron, SIMBOLOS);
    }
}
