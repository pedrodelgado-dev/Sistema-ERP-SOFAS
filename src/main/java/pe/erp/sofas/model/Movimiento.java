package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/** Movimiento del kardex. La cantidad es positiva salvo en los ajustes, que llevan signo. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "movimiento", indexes = @Index(columnList = "material_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Movimiento {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_id")
    private Material material;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private LocalDateTime fecha;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimiento tipo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Double cantidad;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String referencia;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String concepto;

    /** Saldo fisico del material despues del movimiento. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private Double saldo;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String usuario;

    /** Cantidad con signo segun su efecto en el saldo (0 si no lo afecta). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public double getEfecto() {
        return switch (tipo) {
            case ENTRADA -> cantidad;
            case SALIDA, CONSUMO_OP -> -cantidad;
            case AJUSTE -> cantidad;
            default -> 0;
        };
    }
}
