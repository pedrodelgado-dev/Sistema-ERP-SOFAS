package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Pedido {

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Tipo { @EquipoPLMD_MGML_AMCS_DRJB
        ESTANDAR, @EquipoPLMD_MGML_AMCS_DRJB
         PERSONALIZADO;

        @EquipoPLMD_MGML_AMCS_DRJB
        public String nombre() {
            return this == ESTANDAR ? "Estándar" : "Personalizado";
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 12)
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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Tipo tipo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Integer cantidad = 1;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fecha;
    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaEntrega;

    /** Precio total del pedido (IGV incluido). */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal adelanto = BigDecimal.ZERO;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoPedido estado;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 12)
    private String opCodigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer largo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer fondo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer alto;

    /** "Chenille", "Cuero sintético"... */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String material;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tela_id")
    private Tela tela;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 600)
    private String observaciones;

    /** Configuracion serializada del configurador 3D (clave=valor;...). */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 800)
    private String configuracion;

    /** Resumen legible de la configuracion. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 600)
    private String especificacion;

    @EquipoPLMD_MGML_AMCS_DRJB
    public BigDecimal getSaldo() {
        return precio.subtract(adelanto).max(BigDecimal.ZERO);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double getPctPagado() {
        return precio.signum() == 0 ? 0 : adelanto.divide(precio, 4, RoundingMode.HALF_UP).doubleValue();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isRetrasado() {
        return !estado.cerrado() && estado != EstadoPedido.LISTO && fechaEntrega != null
                && fechaEntrega.isBefore(pe.erp.sofas.core.AppClock.hoy());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getProductoNombre() {
        return producto == null ? "Sofá a medida" : producto.getNombre();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getMedidas() {
        if (largo == null || largo == 0) return producto == null ? "—" : producto.getMedidas();
        return largo + " × " + fondo + " × " + alto + " cm";
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getColor() {
        return tela == null ? "—" : tela.getColor();
    }
}
