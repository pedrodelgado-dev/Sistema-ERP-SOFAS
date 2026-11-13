package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "empleado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Empleado {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final java.util.List<String> AREAS = java.util.List.of(
            "Taller · Carpintería", "Taller · Costura", "Taller · Tapizado", "Taller · Espumado", "Tienda", "Almacén", "Administración");

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private String nombre;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 8)
    private String dni;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String cargo;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String area;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Rol rol;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String telefono;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate ingreso;

    /** Activo · Vacaciones · Inactivo. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private String estado = "Activo";

    /** Etapa del taller en la que trabaja (para el pago a destajo). */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Etapa etapa;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String color;

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean isTaller() {
        return area != null && area.startsWith("Taller");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
