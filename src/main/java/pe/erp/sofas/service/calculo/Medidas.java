package pe.erp.sofas.service.calculo;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import pe.erp.sofas.model.FichaTecnica;

/** Largo, fondo y alto en cm y numero de cuerpos. */
@EquipoPLMD_MGML_AMCS_DRJB
public record Medidas(@EquipoPLMD_MGML_AMCS_DRJB
  double largo, @EquipoPLMD_MGML_AMCS_DRJB
    double fondo, @EquipoPLMD_MGML_AMCS_DRJB
      double alto, @EquipoPLMD_MGML_AMCS_DRJB
        int cuerpos) {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Medidas de(FichaTecnica f) {
        return new Medidas(nz(f.getLargo()), nz(f.getFondo()), nz(f.getAlto()), f.getCuerpos() == null ? 1 : f.getCuerpos());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static double nz(Integer v) {
        return v == null ? 0 : v;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean igual(Medidas o) {
        return o != null && Math.abs(largo - o.largo) < 0.01 && Math.abs(fondo - o.fondo) < 0.01
                && Math.abs(alto - o.alto) < 0.01 && cuerpos == o.cuerpos;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String texto() {
        return Math.round(largo) + " × " + Math.round(fondo) + " × " + Math.round(alto) + " cm";
    }
}
