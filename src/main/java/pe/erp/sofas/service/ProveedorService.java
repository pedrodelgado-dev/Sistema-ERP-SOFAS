package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.Material;
import pe.erp.sofas.model.OrdenCompra;
import pe.erp.sofas.model.Proveedor;
import pe.erp.sofas.repository.MaterialRepository;
import pe.erp.sofas.repository.OrdenCompraRepository;
import pe.erp.sofas.repository.ProveedorRepository;

import java.util.List;

@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProveedorService {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProveedorRepository repository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final MaterialRepository materialRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final OrdenCompraRepository compraRepository;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Proveedor> listar() {
        return repository.findAllByOrderByCodigoAsc();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Proveedor> activos() {
        return listar().stream().filter(Proveedor::getActivo).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Proveedor proveedor(Long id) {
        return repository.findById(id).orElseThrow(() -> new NegocioException("El proveedor ya no existe."));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Material> materiales(Proveedor p) {
        return materialRepository.findAllByOrderByCodigoAsc().stream()
                .filter(m -> m.getProveedor() != null && m.getProveedor().getId().equals(p.getId())).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<OrdenCompra> compras(Proveedor p) {
        return compraRepository.findAllByOrderByCodigoDesc().stream().filter(o -> o.getProveedor().getId().equals(p.getId())).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Proveedor guardar(Proveedor p) {
        if (p.getRazonSocial() == null || p.getRazonSocial().isBlank()) throw new NegocioException("La razón social es obligatoria.");
        if (p.getRuc() == null || !p.getRuc().matches("\\d{11}")) throw new NegocioException("El RUC debe tener 11 dígitos.");
        boolean duplicado = listar().stream().anyMatch(x -> x.getRuc() != null && x.getRuc().equals(p.getRuc()) && !x.getId().equals(p.getId()));
        if (duplicado) throw new NegocioException("Ya existe un proveedor con ese RUC.");
        if (p.getId() == null) p.setCodigo(Codigos.siguiente(repository.findAll(), Proveedor::getCodigo, "PR-", 2));
        return repository.save(p);
    }
}
