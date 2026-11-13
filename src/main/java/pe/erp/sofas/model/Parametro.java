package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

/** Parametro de configuracion clave/valor (mermas por defecto, costos por hora, tarifas de destajo...). */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "parametro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parametro {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @Column(length = 60)
    private String clave;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private String valor;
}
