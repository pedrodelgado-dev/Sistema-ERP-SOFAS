package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.EmpresaRepository;
import pe.erp.sofas.repository.PermisoRepository;
import pe.erp.sofas.repository.UsuarioRepository;

import java.util.List;

/** Empresa, usuarios, roles y matriz de permisos. */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConfiguracionService {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final EmpresaRepository empresaRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final UsuarioRepository usuarioRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PermisoRepository permisoRepository;

    @EquipoPLMD_MGML_AMCS_DRJB
    public Empresa empresa() {
        return empresaRepository.findById(1L).orElseGet(() -> Empresa.builder().id(1L).razonSocial("SISTEMA ERP SOFAS S.A.C.").build());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Empresa guardarEmpresa(Empresa e) {
        if (e.getRazonSocial() == null || e.getRazonSocial().isBlank()) throw new NegocioException("La razón social es obligatoria.");
        if (e.getRuc() == null || !e.getRuc().matches("\\d{11}")) throw new NegocioException("El RUC debe tener 11 dígitos.");
        e.setId(1L);
        return empresaRepository.save(e);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Usuario> usuarios() {
        return usuarioRepository.findAll();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Usuario guardarUsuario(Usuario u, String password) {
        if (u.getUsername() == null || !u.getUsername().matches("[a-zA-Z0-9._]{3,30}")) throw new NegocioException("Usuario de 3 a 30 caracteres, sin espacios.");
        if (u.getNombre() == null || u.getNombre().isBlank()) throw new NegocioException("El nombre es obligatorio.");
        boolean duplicado = usuarios().stream().anyMatch(x -> x.getUsername().equalsIgnoreCase(u.getUsername()) && !x.getId().equals(u.getId()));
        if (duplicado) throw new NegocioException("El usuario " + u.getUsername() + " ya existe.");
        if (u.getId() == null && (password == null || password.length() < 6)) throw new NegocioException("La contraseña debe tener al menos 6 caracteres.");
        if (password != null && !password.isEmpty()) u.setPasswordHash(AuthService.hash(password));
        return usuarioRepository.save(u);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Permiso> permisos(Rol rol) {
        return permisoRepository.findByRol(rol);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Permiso fijarPermiso(Rol rol, String modulo, boolean ver, boolean editar) {
        Permiso p = permisoRepository.findByRolAndModulo(rol, modulo).orElseGet(() -> Permiso.builder().rol(rol).modulo(modulo).build());
        p.setVer(ver || editar);
        p.setEditar(editar);
        return permisoRepository.save(p);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean puedeVer(Rol rol, String modulo) {
        if (rol == Rol.GERENTE) return true;
        return permisoRepository.findByRolAndModulo(rol, modulo).map(Permiso::getVer).orElse(false);
    }
}
