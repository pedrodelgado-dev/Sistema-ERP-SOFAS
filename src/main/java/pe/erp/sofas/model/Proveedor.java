package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "proveedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Proveedor {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private String razonSocial;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 11)
    private String ruc;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String contacto;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String telefono;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String email;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String direccion;

    /** Materiales que abastece (texto libre: "madera, triplay, patas de madera"). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String rubro;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String condicionPago;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(precision = 12, scale = 2)
    private BigDecimal totalComprado = BigDecimal.ZERO;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return razonSocial;
    }
}
