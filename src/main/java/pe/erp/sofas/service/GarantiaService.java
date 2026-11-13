package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.EstadoReclamo;
import pe.erp.sofas.model.ReclamoGarantia;
import pe.erp.sofas.repository.ClienteRepository;
import pe.erp.sofas.repository.ProductoRepository;
import pe.erp.sofas.repository.ReclamoGarantiaRepository;
import pe.erp.sofas.service.calculo.Cobertura;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/** Garantias y reclamos con la regla de cobertura (estructura 12 meses; tapiz y espuma 6). */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GarantiaService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final int SLA_DIAS = 7;

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Kpis(@EquipoPLMD_MGML_AMCS_DRJB
      long abiertos, @EquipoPLMD_MGML_AMCS_DRJB
        double pctCubiertos, @EquipoPLMD_MGML_AMCS_DRJB
          double diasPromedio, @EquipoPLMD_MGML_AMCS_DRJB
            BigDecimal costo) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ReclamoGarantiaRepository repository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ClienteRepository clienteRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProductoRepository productoRepository;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<ReclamoGarantia> listar() {
        return repository.findAllByOrderByCodigoDesc();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ReclamoGarantia reclamo(Long id) {
        return repository.findById(id).orElseThrow(() -> new NegocioException("El reclamo ya no existe."));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public long abiertos() {
        return listar().stream().filter(r -> !r.getEstado().cerrado()).count();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Cobertura.Resultado cobertura(ReclamoGarantia r) {
        return Cobertura.evaluar(r.getFalla(), r.getFechaCompra(), r.getFecha());
    }

    /** Dias desde el reclamo hasta el cierre (o hasta hoy si sigue abierto). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public long sla(ReclamoGarantia r) {
        LocalDate fin = r.getFechaCierre() == null ? AppClock.hoy() : r.getFechaCierre();
        return ChronoUnit.DAYS.between(r.getFecha(), fin);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean slaVencido(ReclamoGarantia r) {
        return !r.getEstado().cerrado() && sla(r) > SLA_DIAS;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Kpis kpis() {
        List<ReclamoGarantia> l = listar();
        long cubiertos = l.stream().filter(r -> cobertura(r).cubierto()).count();
        double dias = l.stream().filter(r -> r.getFechaCierre() != null).mapToLong(this::sla).average().orElse(0);
        BigDecimal costo = l.stream().map(r -> r.getCosto() == null ? BigDecimal.ZERO : r.getCosto()).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Kpis(abiertos(), l.isEmpty() ? 0 : cubiertos * 100.0 / l.size(), dias, costo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String siguienteCodigo() {
        return Codigos.siguiente(repository.findAll(), ReclamoGarantia::getCodigo, "GR-", 3);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public ReclamoGarantia registrar(Long clienteId, Long productoId, LocalDate fechaCompra, String falla, String descripcion) {
        if (clienteId == null) throw new NegocioException("Elige el cliente.");
        if (falla == null) throw new NegocioException("Elige el tipo de falla.");
        if (fechaCompra == null || fechaCompra.isAfter(AppClock.hoy())) throw new NegocioException("Revisa la fecha de compra.");
        return repository.save(ReclamoGarantia.builder().codigo(siguienteCodigo())
                .cliente(clienteRepository.findById(clienteId).orElseThrow())
                .producto(productoId == null ? null : productoRepository.findById(productoId).orElse(null))
                .fechaCompra(fechaCompra).falla(falla).descripcion(descripcion).fecha(AppClock.hoy())
                .estado(EstadoReclamo.ABIERTO).solucion("Por definir").build());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public ReclamoGarantia actualizar(Long id, String diagnostico, String solucion, BigDecimal costo, EstadoReclamo estado) {
        ReclamoGarantia r = reclamo(id);
        if (costo != null && costo.signum() < 0) throw new NegocioException("El costo no puede ser negativo.");
        r.setDiagnostico(diagnostico);
        r.setSolucion(solucion);
        r.setCosto(costo == null ? BigDecimal.ZERO : costo);
        if (estado != null) {
            if (estado.cerrado() && !r.getEstado().cerrado()) r.setFechaCierre(AppClock.hoy());
            if (!estado.cerrado()) r.setFechaCierre(null);
            r.setEstado(estado);
        }
        return repository.save(r);
    }
}
