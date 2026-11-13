package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.springframework.stereotype.Component;
import pe.erp.sofas.model.Rol;

/** Usuario que firma los movimientos del kardex y las aprobaciones (lo fija el login). */
@EquipoPLMD_MGML_AMCS_DRJB
@Component
public class UsuarioActual {

    @EquipoPLMD_MGML_AMCS_DRJB
    private String username = "sistema";
    @EquipoPLMD_MGML_AMCS_DRJB
    private String nombre = "Sistema";
    @EquipoPLMD_MGML_AMCS_DRJB
    private Rol rol = Rol.GERENTE;

    @EquipoPLMD_MGML_AMCS_DRJB
    public void fijar(String username, String nombre, Rol rol) {
        this.username = username;
        this.nombre = nombre;
        this.rol = rol;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String username() {
        return username;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Rol rol() {
        return rol;
    }
}
