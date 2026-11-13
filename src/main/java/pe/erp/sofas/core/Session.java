package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pe.erp.sofas.model.Rol;
import pe.erp.sofas.model.Usuario;
import pe.erp.sofas.service.UsuarioActual;

/** Usuario con sesion iniciada (tambien firma los movimientos del kardex). */
@EquipoPLMD_MGML_AMCS_DRJB
@Component
@Lazy
@RequiredArgsConstructor
public class Session {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final UsuarioActual usuarioActual;
    @EquipoPLMD_MGML_AMCS_DRJB
    private Usuario usuario;

    @EquipoPLMD_MGML_AMCS_DRJB
    public Usuario usuario() {
        return usuario;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void iniciar(Usuario usuario) {
        this.usuario = usuario;
        usuarioActual.fijar(usuario.getUsername(), usuario.getNombre(), usuario.getRol());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void cerrar() {
        this.usuario = null;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return usuario == null ? "Carlos Mendoza" : usuario.getNombre();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String cargo() {
        return usuario == null ? "Gerente General" : usuario.getCargo();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Rol rol() {
        return usuario == null ? Rol.GERENTE : usuario.getRol();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String username() {
        return usuario == null ? "cmendoza" : usuario.getUsername();
    }
}
