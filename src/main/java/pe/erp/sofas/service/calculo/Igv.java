package pe.erp.sofas.service.calculo;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Los precios de venta incluyen IGV (18 %): subtotal = total / 1.18. */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Igv {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final BigDecimal TASA = new BigDecimal("0.18");
    @EquipoPLMD_MGML_AMCS_DRJB
    private static final BigDecimal FACTOR = BigDecimal.ONE.add(TASA);

    @EquipoPLMD_MGML_AMCS_DRJB
    private Igv() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static BigDecimal subtotal(BigDecimal total) {
        return total.divide(FACTOR, 2, RoundingMode.HALF_UP);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static BigDecimal igv(BigDecimal total) {
        return total.setScale(2, RoundingMode.HALF_UP).subtract(subtotal(total));
    }
}
