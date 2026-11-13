package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "item_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class ItemCompra {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_id")
    @ToString.Exclude
    private OrdenCompra orden;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_id")
    private Material material;

    /** Para las telas: color que se compra (los metros entran a ese color del muestrario). */
    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tela_id")
    private Tela tela;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Double cantidad;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @EquipoPLMD_MGML_AMCS_DRJB
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
