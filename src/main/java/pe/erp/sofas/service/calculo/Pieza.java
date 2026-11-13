package pe.erp.sofas.service.calculo;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import pe.erp.sofas.model.Componente;

/**
 * Pieza a cortar: ancho (a lo ancho de la plancha o del rollo) × alto (a lo largo), en cm.
 * En madera solo cuenta el largo ({@code ancho}).
 */
@EquipoPLMD_MGML_AMCS_DRJB
public record Pieza(@EquipoPLMD_MGML_AMCS_DRJB
  String nombre, @EquipoPLMD_MGML_AMCS_DRJB
    Componente componente, @EquipoPLMD_MGML_AMCS_DRJB
      double ancho, @EquipoPLMD_MGML_AMCS_DRJB
        double alto, @EquipoPLMD_MGML_AMCS_DRJB
          int cantidad,
                    @EquipoPLMD_MGML_AMCS_DRJB
                    boolean girable, @EquipoPLMD_MGML_AMCS_DRJB
                      String origen) {

    @EquipoPLMD_MGML_AMCS_DRJB
    public Pieza conCantidad(int n) {
        return new Pieza(nombre, componente, ancho, alto, n, girable, origen);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Pieza conOrigen(String o) {
        return new Pieza(nombre, componente, ancho, alto, cantidad, girable, o);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Pieza conGiro(boolean g) {
        return new Pieza(nombre, componente, ancho, alto, cantidad, g, origen);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double area() {
        return ancho * alto;
    }
}
