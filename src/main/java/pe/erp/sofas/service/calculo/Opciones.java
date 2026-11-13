package pe.erp.sofas.service.calculo;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import pe.erp.sofas.model.LineaExtra;

import java.util.List;
import java.util.Map;

/**
 * Opciones de una explosion: medidas a pedido (null = las de la ficha), sustituciones de material
 * (codigo original → elegido), materiales extra por unidad y factor adicional de mano de obra.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public record Opciones(@EquipoPLMD_MGML_AMCS_DRJB
  Medidas medidas, @EquipoPLMD_MGML_AMCS_DRJB
    Map<String, String> sustituciones, @EquipoPLMD_MGML_AMCS_DRJB
       List<LineaExtra> extras,
                       @EquipoPLMD_MGML_AMCS_DRJB
                       boolean incluirOpcionales, @EquipoPLMD_MGML_AMCS_DRJB
                         double recargoManoObraPct, @EquipoPLMD_MGML_AMCS_DRJB
                           double manoObraFija) {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final Opciones NINGUNA = new Opciones(null, Map.of(), List.of(), false, 0, 0);

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Opciones medidas(Medidas m) {
        return new Opciones(m, Map.of(), List.of(), false, 0, 0);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Opciones sustituir(Map<String, String> sust) {
        return new Opciones(null, sust, List.of(), false, 0, 0);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Opciones conMedidas(Medidas m) {
        return new Opciones(m, sustituciones, extras, incluirOpcionales, recargoManoObraPct, manoObraFija);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Map<String, String> sust() {
        return sustituciones == null ? Map.of() : sustituciones;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<LineaExtra> ext() {
        return extras == null ? List.of() : extras;
    }
}
