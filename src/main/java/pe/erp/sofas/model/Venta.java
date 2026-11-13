package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Venta de tienda (POS) con comprobante simulado B001 / F001. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "venta", uniqueConstraints = @UniqueConstraint(columnNames = {"serie", "numero"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Venta {

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Comprobante { @EquipoPLMD_MGML_AMCS_DRJB
        BOLETA, @EquipoPLMD_MGML_AMCS_DRJB
         FACTURA;

        @EquipoPLMD_MGML_AMCS_DRJB
        public String nombre() {
            return this == BOLETA ? "Boleta" : "Factura";
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String serie() {
            return this == BOLETA ? "B001" : "F001";
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, length = 4)
    private String serie;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Integer numero;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private LocalDateTime fecha;

    /** Nulo = "Cliente varios". */
    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Comprobante comprobante;

    /** Efectivo, Tarjeta, Transferencia, Yape, Plin o "Mixto (Efectivo + Yape)". */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String metodoPago;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(precision = 12, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(precision = 12, scale = 2)
    private BigDecimal recibido;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(precision = 12, scale = 2)
    private BigDecimal vuelto;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String vendedor;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<ItemVenta> items = new ArrayList<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    public void agregar(ItemVenta item) {
        item.setVenta(this);
        items.add(item);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getNumeroCompleto() {
        return serie + "-" + String.format("%06d", numero);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getClienteNombre() {
        return cliente == null ? "Cliente varios" : cliente.getNombre();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public int getUnidades() {
        return items.stream().mapToInt(ItemVenta::getCantidad).sum();
    }
}
