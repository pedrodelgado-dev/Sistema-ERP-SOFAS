package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Servicio de retapizado de un sofa del cliente. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "retapizado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Retapizado {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final java.util.List<String> ESTRUCTURAS = java.util.List.of("Buena", "Regular", "Requiere refuerzo");
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final java.util.List<String> ESPUMAS = java.util.List.of("Buena", "Hundida", "Cambio total");

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
    @Column(nullable = false)
    private String descripcion;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Integer cuerpos = 3;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer largo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer fondo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer alto;

    /** Buena · Regular · Requiere refuerzo. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String estructura;

    /** Buena · Hundida · Cambio total. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String espuma;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tela_id")
    private Tela tela;

    /** Metros de tela estimados para la cotizacion. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private Double metros;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaRegistro;
    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaRecojo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaEntrega;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoRetapizado estado;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 12)
    private String opCodigo;

    /** Foto del sofa del cliente. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String foto;

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getMedidas() {
        return (largo == null ? "—" : largo) + " × " + (fondo == null ? "—" : fondo) + " × " + (alto == null ? "—" : alto) + " cm";
    }
}
