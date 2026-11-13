package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String razonSocial;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String nombreComercial;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String ruc;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String direccion;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String ciudad;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String telefono;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String email;
    @EquipoPLMD_MGML_AMCS_DRJB
    private String web;
    /** Ruta local del logo (opcional). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String logo;
}
