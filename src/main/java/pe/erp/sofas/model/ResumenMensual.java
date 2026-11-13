package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/** Cierre historico de un mes (ventas y egresos) para los graficos de 12 meses. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "resumen_mensual", uniqueConstraints = @UniqueConstraint(columnNames = {"anio", "mes"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class ResumenMensual {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer anio;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer mes;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(precision = 12, scale = 2)
    private BigDecimal ventas;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(precision = 12, scale = 2)
    private BigDecimal egresos;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer sofas;
}
