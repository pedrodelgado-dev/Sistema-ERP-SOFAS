package pe.erp.sofas.repository;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.erp.sofas.model.*;

import java.util.List;
import java.util.Optional;

@EquipoPLMD_MGML_AMCS_DRJB
@Repository
public interface OrdenProduccionRepository extends JpaRepository<OrdenProduccion, Long> {

    @EquipoPLMD_MGML_AMCS_DRJB
    Optional<OrdenProduccion> findByCodigo(String codigo);

    @EquipoPLMD_MGML_AMCS_DRJB
    List<OrdenProduccion> findAllByOrderByCodigoAsc();
}
