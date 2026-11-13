package pe.erp.sofas.repository;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.erp.sofas.model.ConfiguracionGuardada;
import pe.erp.sofas.model.Usuario;

import java.util.List;
import java.util.Optional;

@EquipoPLMD_MGML_AMCS_DRJB
@Repository
public interface ConfiguracionGuardadaRepository extends JpaRepository<ConfiguracionGuardada, Long> {

    @EquipoPLMD_MGML_AMCS_DRJB
    List<ConfiguracionGuardada> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);

    @EquipoPLMD_MGML_AMCS_DRJB
    List<ConfiguracionGuardada> findByUsuarioAndEsFavoritoTrueOrderByFechaCreacionDesc(Usuario usuario);

    @EquipoPLMD_MGML_AMCS_DRJB
    Optional<ConfiguracionGuardada> findByIdAndUsuario(Long id, Usuario usuario);

    @EquipoPLMD_MGML_AMCS_DRJB
    List<ConfiguracionGuardada> findByUsuarioAndEtiquetaOrderByFechaCreacionDesc(Usuario usuario, String etiqueta);

    @EquipoPLMD_MGML_AMCS_DRJB
    long countByUsuario(Usuario usuario);
}
