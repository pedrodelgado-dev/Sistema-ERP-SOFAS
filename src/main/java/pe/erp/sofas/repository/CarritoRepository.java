package pe.erp.sofas.repository;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.erp.sofas.model.Carrito;
import pe.erp.sofas.model.Usuario;

import java.util.Optional;

@EquipoPLMD_MGML_AMCS_DRJB
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    @EquipoPLMD_MGML_AMCS_DRJB
    Optional<Carrito> findByUsuario(Usuario usuario);

    @EquipoPLMD_MGML_AMCS_DRJB
    void deleteByUsuario(Usuario usuario);
}
