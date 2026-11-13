package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

/** Estado de una alerta calculada (leida o descartada), identificada por su clave estable. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "notificacion_estado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionEstado {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @Column(length = 80)
    private String clave;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Boolean leida;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Boolean descartada;
}
