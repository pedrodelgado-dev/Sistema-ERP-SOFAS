package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Ficha tecnica (BOM + mano de obra) de un modelo. Cada version tiene un uid "FT-001v3";
 * las OP guardan el uid de la version con la que se planificaron.
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "ficha_tecnica", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo", "version"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class FichaTecnica {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, length = 10)
    private String codigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Integer version;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 16)
    private String uid;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EstadoFicha estado;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private String nombre;

    /** Medidas base L0 / F0 / A0 (cm) y cuerpos n0. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer largo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer fondo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer alto;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Integer cuerpos = 3;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Boolean chaise = false;

    /** Recto · Seccional · Chesterfield · Puff (para el despiece de la hoja de corte). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String tipoCorte;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Double indirectosPct = 6d;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String aprobadoPor;
    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaAprobacion;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 500)
    private String notas;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Boolean compuesta = false;

    /** Codigos de las fichas hijas (juego 3-2-1: FT-001, FT-002, FT-003). */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ficha_hijo", joinColumns = @JoinColumn(name = "ficha_id"))
    @OrderColumn(name = "orden")
    @Column(name = "codigo_hijo", length = 10)
    @Fetch(FetchMode.SUBSELECT)
    private List<String> hijos = new ArrayList<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @OneToMany(mappedBy = "ficha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("orden ASC")
    @Fetch(FetchMode.SUBSELECT)
    private List<LineaBOM> lineas = new ArrayList<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @OneToMany(mappedBy = "ficha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("orden ASC")
    @Fetch(FetchMode.SUBSELECT)
    private List<OperacionMO> operaciones = new ArrayList<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String uid(String codigo, int version) {
        return codigo + "v" + version;
    }

    /** "FT-001 v3". */
    @EquipoPLMD_MGML_AMCS_DRJB
    public String getEtiqueta() {
        return codigo + " v" + version;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isEditable() {
        return estado == EstadoFicha.BORRADOR;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void agregarLinea(LineaBOM l) {
        l.setFicha(this);
        l.setOrden(lineas.size());
        lineas.add(l);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void agregarOperacion(OperacionMO o) {
        o.setFicha(this);
        o.setOrden(operaciones.size());
        operaciones.add(o);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double getHorasMO() {
        return operaciones.stream().mapToDouble(OperacionMO::getHoras).sum();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return getEtiqueta() + " · " + nombre;
    }
}
