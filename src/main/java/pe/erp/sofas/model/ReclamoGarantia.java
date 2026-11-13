package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "reclamo_garantia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class ReclamoGarantia {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final java.util.List<String> FALLAS = java.util.List.of(
            "Pata floja", "Estructura quebrada", "Mecanismo trabado", "Espuma hundida", "Costura abierta",
            "Tela con pilling", "Decoloración de tela", "Cierre roto");
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final java.util.List<String> SOLUCIONES = java.util.List.of(
            "Por definir", "Reparación en domicilio", "Reparación en taller", "Cambio de espuma",
            "Cambio de funda", "Reemplazo del mecanismo", "Cambio de producto", "No procede");

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaCompra;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private String falla;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 600)
    private String descripcion;

    /** Fecha del reclamo. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fecha;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReclamo estado;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 600)
    private String diagnostico;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String solucion;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(precision = 12, scale = 2)
    private BigDecimal costo = BigDecimal.ZERO;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaCierre;

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getProductoNombre() {
        return producto == null ? "—" : producto.getNombre();
    }
}
