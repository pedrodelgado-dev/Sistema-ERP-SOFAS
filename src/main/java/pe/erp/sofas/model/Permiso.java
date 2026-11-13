package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

/** Celda de la matriz de permisos: que puede hacer un rol en un modulo. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "permiso", uniqueConstraints = @UniqueConstraint(columnNames = {"rol", "modulo"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Permiso {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, length = 30)
    private String modulo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Boolean ver = false;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Boolean editar = false;
}
