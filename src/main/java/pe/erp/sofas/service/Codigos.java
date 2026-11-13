package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import java.util.Collection;
import java.util.function.Function;

/** Genera el siguiente codigo correlativo (SF-012, PE-0250, OP-1031, OC-0324...). */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Codigos {

    @EquipoPLMD_MGML_AMCS_DRJB
    private Codigos() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static <T> String siguiente(Collection<T> existentes, Function<T, String> codigo, String prefijo, int digitos) {
        int max = existentes.stream()
                .map(codigo)
                .filter(c -> c != null && c.startsWith(prefijo))
                .map(c -> c.substring(prefijo.length()).replaceAll("\\D", ""))
                .filter(s -> !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        return prefijo + String.format("%0" + digitos + "d", max + 1);
    }
}
