package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Cliente {

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Tipo { @EquipoPLMD_MGML_AMCS_DRJB
        PERSONA, @EquipoPLMD_MGML_AMCS_DRJB
         EMPRESA;

        @EquipoPLMD_MGML_AMCS_DRJB
        public String nombre() {
            return this == PERSONA ? "Persona" : "Empresa";
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Tipo tipo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private String nombre;

    /** DNI (8 digitos) o RUC (11 digitos). */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 11)
    private String documento;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String telefono;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String email;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String direccion;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String ciudad;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fechaRegistro;

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean tieneRuc() {
        return documento != null && documento.length() == 11;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getTipoDocumento() {
        return tieneRuc() ? "RUC" : "DNI";
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
