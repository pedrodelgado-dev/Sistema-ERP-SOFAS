package pe.erp.sofas.service.calculo;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import java.time.LocalDate;
import java.util.Set;

/**
 * Regla de garantia: pata floja, estructura quebrada y mecanismo trabado tienen 12 meses (estructura);
 * el resto 6 meses (tapiz y espuma).
 */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Cobertura {

    @EquipoPLMD_MGML_AMCS_DRJB
    private static final Set<String> ESTRUCTURA = Set.of("Pata floja", "Estructura quebrada", "Mecanismo trabado");

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Resultado(@EquipoPLMD_MGML_AMCS_DRJB
      boolean cubierto, @EquipoPLMD_MGML_AMCS_DRJB
        int meses, @EquipoPLMD_MGML_AMCS_DRJB
          LocalDate vence, @EquipoPLMD_MGML_AMCS_DRJB
            String tipo) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public String texto() {
            return cubierto ? "En garantía hasta " + pe.erp.sofas.core.AppClock.fmt(vence) : "Fuera de garantía";
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private Cobertura() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static int meses(String falla) {
        return ESTRUCTURA.contains(falla) ? 12 : 6;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Resultado evaluar(String falla, LocalDate fechaCompra, LocalDate fechaReclamo) {
        int meses = meses(falla);
        if (fechaCompra == null) return new Resultado(false, meses, null, tipo(meses));
        LocalDate vence = fechaCompra.plusMonths(meses);
        LocalDate ref = fechaReclamo == null ? pe.erp.sofas.core.AppClock.hoy() : fechaReclamo;
        return new Resultado(!ref.isAfter(vence), meses, vence, tipo(meses));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static String tipo(int meses) {
        return meses == 12 ? "Estructura · 12 meses" : "Tapiz y espuma · 6 meses";
    }
}
