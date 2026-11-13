package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

/** Cantidad de un material apartada para una orden de produccion. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "reserva", uniqueConstraints = @UniqueConstraint(columnNames = {"op_id", "material_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Reserva {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "op_id")
    private OrdenProduccion op;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_id")
    private Material material;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Double cantidad;
}
