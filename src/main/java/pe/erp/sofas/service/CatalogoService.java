package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.erp.sofas.model.EstadoFicha;
import pe.erp.sofas.model.FichaTecnica;
import pe.erp.sofas.model.Material;
import pe.erp.sofas.repository.FichaTecnicaRepository;
import pe.erp.sofas.repository.MaterialRepository;
import pe.erp.sofas.service.calculo.Catalogo;

/** Implementacion del catalogo de la explosion sobre los repositorios. */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
public class CatalogoService implements Catalogo {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final FichaTecnicaRepository fichaRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final MaterialRepository materialRepository;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public FichaTecnica fichaAprobada(String codigo) {
        return fichaRepository.findByCodigoOrderByVersionDesc(codigo).stream()
                .filter(f -> f.getEstado() == EstadoFicha.APROBADA).findFirst().orElse(null);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public Material material(String codigo) {
        return materialRepository.findByCodigo(codigo).orElse(null);
    }
}
