package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pe.erp.sofas.core.AppClock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "orden_produccion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class OrdenProduccion {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 12)
    private String codigo;

    /** Nulo en los retapizados. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    /** "Retapizado · Sofá 2 cuerpos" o un nombre a medida. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String nombrePersonalizado;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Integer cantidad = 1;

    /** Nulo cuando la orden es para stock de tienda. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "responsable_id")
    private Empleado responsable;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 10)
    private Prioridad prioridad = Prioridad.MEDIA;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private Etapa etapa = Etapa.PENDIENTE;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaInicio;
    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaEntrega;
    /** Fecha en que llego a Listo. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaFin;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(precision = 12, scale = 2)
    private BigDecimal costo;

    /** Version de la ficha usada ("FT-001v3"); nulo si no hay ficha aprobada. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 16)
    private String fichaUid;

    /** Medidas a pedido (nulo = las de la ficha). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer largo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer fondo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer alto;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer cuerpos;

    /** Sustituciones de material: codigo original → codigo elegido (tela, espuma, patas). */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "op_sustitucion", joinColumns = @JoinColumn(name = "op_id"))
    @MapKeyColumn(name = "original", length = 20)
    @Column(name = "elegido", length = 20)
    @Fetch(FetchMode.SUBSELECT)
    private Map<String, String> sustituciones = new HashMap<>();

    /** Materiales adicionales por unidad (configurador). */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "op_extra", joinColumns = @JoinColumn(name = "op_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<LineaExtra> extras = new ArrayList<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tela_id")
    private Tela tela;

    /** Texto de la autorizacion cuando avanzo sin ficha aprobada. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 300)
    private String autorizacion;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 12)
    private String pedidoCodigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 12)
    private String retapizadoCodigo;

    /** Resumen de la configuracion (configurador 3D). */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 600)
    private String especificacion;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Boolean anulada = false;

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getProductoNombre() {
        if (nombrePersonalizado != null && !nombrePersonalizado.isBlank()) return nombrePersonalizado;
        return producto == null ? "—" : producto.getNombre();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isRetrasada() {
        return etapa != Etapa.LISTO && !Boolean.TRUE.equals(anulada)
                && fechaEntrega != null && fechaEntrega.isBefore(AppClock.hoy());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isActiva() {
        return !Boolean.TRUE.equals(anulada) && etapa != Etapa.LISTO;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getClienteNombre() {
        return cliente == null ? "Stock de tienda" : cliente.getNombre();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isSinFicha() {
        return fichaUid == null;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getEstado() {
        if (Boolean.TRUE.equals(anulada)) return "Cancelado";
        if (etapa == Etapa.LISTO) return "Listo";
        if (isRetrasada()) return "Retrasado";
        if (etapa == Etapa.PENDIENTE) return "Pendiente";
        return "En producción";
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getTelaNombre() {
        return tela == null ? "Según ficha" : tela.getNombre();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return codigo + " · " + getProductoNombre();
    }
}
