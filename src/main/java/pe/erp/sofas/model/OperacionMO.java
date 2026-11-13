package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

/** Horas de mano de obra de una etapa en la ficha tecnica. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "operacion_mo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class OperacionMO {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ficha_id")
    @ToString.Exclude
    private FichaTecnica ficha;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer orden;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EtapaMO etapa;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Double horas;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Double costoHora;

    @EquipoPLMD_MGML_AMCS_DRJB
    public double getCosto() {
        return horas * costoHora;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public OperacionMO copia() {
        return toBuilder().id(null).ficha(null).build();
    }
}
