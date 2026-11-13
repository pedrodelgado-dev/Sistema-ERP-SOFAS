package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.Usuario;
import pe.erp.sofas.repository.UsuarioRepository;

/**
 * Inicio de sesion con contrasenas BCrypt (jBCrypt). En modo prototipo ({@code app.auth.modo-prototipo=true})
 * cualquier credencial no vacia ingresa como el usuario demo.
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
public class AuthService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String USUARIO_DEMO = "cmendoza";

    @EquipoPLMD_MGML_AMCS_DRJB
    private final UsuarioRepository repository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final UsuarioActual usuarioActual;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Value("${app.auth.modo-prototipo:false}")
    private boolean modoPrototipo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Usuario login(String username, String password) {
        String user = username == null ? "" : username.trim();
        if (user.isEmpty() || password == null || password.isEmpty()) throw new NegocioException("Ingresa tu usuario y contraseña.");
        var encontrado = repository.findByUsernameIgnoreCase(user);
        Usuario u;
        if (modoPrototipo) {
            u = encontrado.filter(Usuario::getActivo).orElseGet(() -> repository.findByUsernameIgnoreCase(USUARIO_DEMO).orElseThrow());
        } else {
            u = encontrado.orElseThrow(() -> new NegocioException("Usuario o contraseña incorrectos."));
            if (!u.getActivo()) throw new NegocioException("El usuario está desactivado.");
            if (!verificar(password, u.getPasswordHash())) throw new NegocioException("Usuario o contraseña incorrectos.");
        }
        u.setUltimoAcceso(AppClock.hoy());
        usuarioActual.fijar(u.getUsername(), u.getNombre(), u.getRol());
        return repository.save(u);
    }

    /** Valida la clave de un Supervisor (o Gerente/Administrador) para autorizar una excepcion. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public Usuario autorizar(String username, String password) {
        Usuario u = repository.findByUsernameIgnoreCase(username == null ? "" : username.trim())
                .orElseThrow(() -> new NegocioException("Usuario no encontrado."));
        if (!u.getRol().autoriza()) throw new NegocioException(u.getNombre() + " no puede autorizar: se requiere un Supervisor.");
        if (!verificar(password == null ? "" : password, u.getPasswordHash())) throw new NegocioException("Contraseña de autorización incorrecta.");
        return u;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public java.util.List<Usuario> autorizadores() {
        return repository.findAll().stream().filter(u -> u.getActivo() && u.getRol().autoriza()).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static boolean verificar(String password, String hash) {
        try {
            return hash != null && BCrypt.checkpw(password, hash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
