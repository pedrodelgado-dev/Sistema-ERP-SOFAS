package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/** Consumo real frente al estimado por la ficha al cerrar una OP (base de Consumo y mermas). */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "consumo_real")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class ConsumoReal {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, length = 12)
    private String opCodigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String producto;

    /** Codigo de la ficha (FT-001) usada, para ajustar la ficha con el promedio real. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 16)
    private String fichaUid;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String responsable;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_id")
    private Material material;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Double estimado;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Double realConsumido;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fecha;

    @EquipoPLMD_MGML_AMCS_DRJB
    public double getDiferencia() {
        return realConsumido - estimado;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double getDiferenciaPct() {
        return estimado == 0 ? 0 : (realConsumido - estimado) * 100 / estimado;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double getDiferenciaValor() {
        return getDiferencia() * material.getCosto();
    }
}
