package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Usuario {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 40)
    private String username;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private String nombre;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String email;

    /** Cargo mostrado en la barra superior (p. ej. "Gerente General"). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String cargo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    /** Hash BCrypt de la contrasena. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate ultimoAcceso;

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getPrimerNombre() {
        return nombre.trim().split("\\s+")[0];
    }
}
