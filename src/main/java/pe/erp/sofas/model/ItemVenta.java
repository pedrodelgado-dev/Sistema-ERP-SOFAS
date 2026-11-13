package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "item_venta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class ItemVenta {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venta_id")
    @ToString.Exclude
    private Venta venta;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Integer cantidad;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    /** Color de la tela vendida (para "telas mas vendidas"). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String telaCodigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
