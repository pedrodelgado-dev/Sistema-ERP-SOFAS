package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Egreso operativo (planilla, alquiler, servicios, transporte...). */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "gasto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Gasto {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fecha;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String categoria;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String concepto;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;
}
