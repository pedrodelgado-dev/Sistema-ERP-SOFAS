package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "orden_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class OrdenCompra {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final BigDecimal IGV = new BigDecimal("0.18");

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 12)
    private String codigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fecha;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaEntrega;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private EstadoCompra estado;

    /** MRP · Calculadora · OP-1024 · Manual · Alerta de stock. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private String origen = "Manual";

    /** Recien generada (badge "Nueva") hasta que se abre. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Boolean nueva = false;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaRecepcion;
    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaPago;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 500)
    private String observaciones;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<ItemCompra> items = new ArrayList<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    public void agregar(ItemCompra item) {
        item.setOrden(this);
        items.add(item);
    }

    /** Los precios de compra se registran sin IGV. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public BigDecimal getSubtotal() {
        return items.stream().map(ItemCompra::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public BigDecimal getIgv() {
        return getSubtotal().multiply(IGV).setScale(2, RoundingMode.HALF_UP);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public BigDecimal getTotal() {
        return getSubtotal().add(getIgv());
    }
}
