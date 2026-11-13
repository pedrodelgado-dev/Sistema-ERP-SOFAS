package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

/** Linea de la lista de materiales: cantidad base para las medidas de la ficha, merma y escalado. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "linea_bom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class LineaBOM {

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
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_id")
    private Material material;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Componente componente;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Double cantidad;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Double mermaPct = 0d;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 4)
    @Builder.Default
    private Dependencia dependencia = Dependencia.F;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Boolean opcional = false;

    /** Copia sin id ni ficha (para duplicar versiones). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public LineaBOM copia() {
        return toBuilder().id(null).ficha(null).build();
    }
}
