package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/** Materia prima o insumo del almacen del taller. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Material {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final java.util.List<String> CATEGORIAS = java.util.List.of(
            "Madera", "Tableros", "Espuma", "Tapicería", "Patas", "Insumos", "Acabados", "Mecanismos", "Embalaje");

    /** Unidades que se consumen enteras (se redondean hacia arriba en la explosion). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private static final Set<String> ENTERAS = Set.of("unidad", "par", "juego");

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private String nombre;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, length = 30)
    private String categoria;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, length = 20)
    private String unidad;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Double stock = 0d;

    /** Suma de las reservas activas de las ordenes de produccion. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Double reservado = 0d;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Double minimo = 0d;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Double costo;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String ubicacion;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    /** No se mantiene en stock: se compra cuando hay un pedido (viscoelastica, zocalo, motor electrico). */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Boolean bajoPedido = false;

    @EquipoPLMD_MGML_AMCS_DRJB
    public double getDisponible() {
        return Math.max(0, stock - reservado);
    }

    /** Bajo pedido · Agotado · Critico · Stock bajo · Normal, segun el disponible (stock − reservado). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public String getEstado() {
        if (Boolean.TRUE.equals(bajoPedido) && stock <= 0) return "Bajo pedido";
        double d = stock - reservado;
        if (d <= 0.0001) return "Agotado";
        if (d < minimo) return "Crítico";
        if (d < minimo * 1.3) return "Stock bajo";
        return "Normal";
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isCritico() {
        String e = getEstado();
        return "Agotado".equals(e) || "Crítico".equals(e);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isBajoMinimo() {
        return !Boolean.TRUE.equals(bajoPedido) && stock - reservado < minimo;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isEntera() {
        return ENTERAS.contains(unidad);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isTela() {
        return TipoTela.porMaterial(codigo) != null;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double getValor() {
        return costo * stock;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return codigo + " · " + nombre;
    }
}
