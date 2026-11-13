package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/** Material adicional a la ficha (extras del configurador: chaise, cojines, mecanismos...). Por unidad. */
@EquipoPLMD_MGML_AMCS_DRJB
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineaExtra {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(name = "material_codigo", length = 20)
    private String materialCodigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 20)
    private String componente;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Double cantidad;
}
