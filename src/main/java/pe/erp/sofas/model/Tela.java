package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

/** Color del muestrario. Los metros suman al material de inventario de su tipo de tela. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "tela")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Tela {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTela tipo;

    /** Nombre del color: "Gris perla". */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private String color;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, length = 9)
    private String hex;

    /** Codigo del proveedor para el color (CH-210 GP). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String codigoProveedor;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Double stock = 0d;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Double minimo = 8d;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Boolean descontinuada = false;

    /** Disponible · Stock bajo · Agotado · Descontinuada. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public String getEstado() {
        if (Boolean.TRUE.equals(descontinuada)) return "Descontinuada";
        if (stock <= 0) return "Agotado";
        if (stock < minimo) return "Stock bajo";
        return "Disponible";
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getNombre() {
        return tipo.nombre() + " " + color;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return getNombre();
    }
}
