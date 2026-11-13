package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import java.util.ArrayList;
import java.util.List;

/** Tabla generica para reportes y exportaciones (PDF y Excel). {@code derecha[i]} alinea la columna a la derecha. */
@EquipoPLMD_MGML_AMCS_DRJB
public record Tabla(@EquipoPLMD_MGML_AMCS_DRJB
  String titulo, @EquipoPLMD_MGML_AMCS_DRJB
    String subtitulo, @EquipoPLMD_MGML_AMCS_DRJB
      List<String> columnas, @EquipoPLMD_MGML_AMCS_DRJB
        boolean[] derecha, @EquipoPLMD_MGML_AMCS_DRJB
          List<List<String>> filas,
                    @EquipoPLMD_MGML_AMCS_DRJB
                    List<String> totales) {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Tabla de(String titulo, String subtitulo, List<String> columnas, boolean[] derecha) {
        return new Tabla(titulo, subtitulo, columnas, derecha, new ArrayList<>(), null);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Tabla fila(Object... valores) {
        List<String> f = new ArrayList<>();
        for (Object v : valores) f.add(v == null ? "—" : v.toString());
        filas.add(f);
        return this;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Tabla conTotales(String... valores) {
        return new Tabla(titulo, subtitulo, columnas, derecha, filas, List.of(valores));
    }
}
