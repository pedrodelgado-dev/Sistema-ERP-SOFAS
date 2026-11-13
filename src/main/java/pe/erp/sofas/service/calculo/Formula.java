package pe.erp.sofas.service.calculo;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import pe.erp.sofas.model.Dependencia;

import java.util.EnumMap;
import java.util.Map;

/**
 * Factores de escalado de las lineas de la ficha (seccion 6.2), evaluados con exp4j:
 * F = 1 · L = L/L0 · LF = L·F/(L0·F0) · LA = L·A/(L0·A0) · T = L·(F+A)/(L0·(F0+A0)) · N = n/n0 ·
 * P = patas(L)/patas(L0), con 4 patas si L ≤ 160 y 6 si es mayor.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Formula {

    /** patas(L): 4 hasta 160 cm, 6 por encima. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private static final Function PATAS = new Function("patas", 1) {
        @EquipoPLMD_MGML_AMCS_DRJB
        @Override
        public double apply(double... args) {
            return patas(args[0]);
        }
    };

    @EquipoPLMD_MGML_AMCS_DRJB
    private static final Map<Dependencia, Expression> CACHE = new EnumMap<>(Dependencia.class);

    @EquipoPLMD_MGML_AMCS_DRJB
    private Formula() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static int patas(double largo) {
        return largo <= 160 ? 4 : 6;
    }

    /** Factor para pasar de las medidas base de la ficha a las medidas pedidas. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static synchronized double factor(Dependencia dep, Medidas pedidas, Medidas base) {
        if (dep == null || dep == Dependencia.F || pedidas == null || base == null) return 1;
        Expression e = CACHE.computeIfAbsent(dep, d -> new ExpressionBuilder(d.expresion())
                .variables("L", "F", "A", "n", "L0", "F0", "A0", "n0")
                .function(PATAS)
                .build());
        e.setVariable("L", pedidas.largo()).setVariable("F", pedidas.fondo()).setVariable("A", pedidas.alto())
                .setVariable("n", pedidas.cuerpos())
                .setVariable("L0", nz(base.largo())).setVariable("F0", nz(base.fondo())).setVariable("A0", nz(base.alto()))
                .setVariable("n0", Math.max(1, base.cuerpos()));
        double v = e.evaluate();
        return Double.isFinite(v) ? v : 1;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static double nz(double v) {
        return v <= 0 ? 1 : v;
    }

    /** Escalado de la mano de obra con medidas a pedido: (L/L0)^0.7. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static double factorManoObra(Medidas pedidas, Medidas base) {
        if (pedidas == null || base == null || base.largo() <= 0 || pedidas.igual(base)) return 1;
        return Math.pow(pedidas.largo() / base.largo(), 0.7);
    }

    /** Redondeo a la decena: 2,418 → 2,420. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static double redondearDecena(double v) {
        return Math.round(v / 10.0) * 10.0;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static double r2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
