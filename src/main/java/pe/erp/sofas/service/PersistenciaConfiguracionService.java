package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.erp.sofas.model.ConfigSofa;
import pe.erp.sofas.model.ConfiguracionGuardada;
import pe.erp.sofas.model.Usuario;
import pe.erp.sofas.repository.ConfiguracionGuardadaRepository;

import java.util.List;
import java.util.Optional;

@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
public class PersistenciaConfiguracionService {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ConfiguracionGuardadaRepository repo;

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfiguracionGuardada guardar(Usuario usuario, ConfigSofa config, String nombre, String descripcion) {
        ConfiguracionGuardada guardada = ConfiguracionGuardada.fromConfigSofa(config, usuario, nombre, descripcion);
        return repo.save(guardada);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa cargar(Long id, Usuario usuario) {
        ConfiguracionGuardada guardada = repo.findByIdAndUsuario(id, usuario)
            .orElseThrow(() -> new IllegalArgumentException("Configuración no encontrada"));
        return guardada.toConfigSofa();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Optional<ConfiguracionGuardada> obtener(Long id, Usuario usuario) {
        return repo.findByIdAndUsuario(id, usuario);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<ConfiguracionGuardada> listarPorUsuario(Usuario usuario) {
        return repo.findByUsuarioOrderByFechaCreacionDesc(usuario);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<ConfiguracionGuardada> listarFavoritos(Usuario usuario) {
        return repo.findByUsuarioAndEsFavoritoTrueOrderByFechaCreacionDesc(usuario);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<ConfiguracionGuardada> listarPorEtiqueta(Usuario usuario, String etiqueta) {
        return repo.findByUsuarioAndEtiquetaOrderByFechaCreacionDesc(usuario, etiqueta);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfiguracionGuardada marcarFavorito(Long id, Usuario usuario, boolean esFavorito) {
        ConfiguracionGuardada guardada = repo.findByIdAndUsuario(id, usuario)
            .orElseThrow(() -> new IllegalArgumentException("Configuración no encontrada"));
        guardada.setEsFavorito(esFavorito);
        return repo.save(guardada);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfiguracionGuardada actualizar(Long id, Usuario usuario, String nombre, String descripcion, String etiqueta) {
        ConfiguracionGuardada guardada = repo.findByIdAndUsuario(id, usuario)
            .orElseThrow(() -> new IllegalArgumentException("Configuración no encontrada"));
        guardada.setNombre(nombre);
        guardada.setDescripcion(descripcion);
        guardada.setEtiqueta(etiqueta);
        return repo.save(guardada);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void eliminar(Long id, Usuario usuario) {
        ConfiguracionGuardada guardada = repo.findByIdAndUsuario(id, usuario)
            .orElseThrow(() -> new IllegalArgumentException("Configuración no encontrada"));
        repo.delete(guardada);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public long contarPorUsuario(Usuario usuario) {
        return repo.countByUsuario(usuario);
    }
}
