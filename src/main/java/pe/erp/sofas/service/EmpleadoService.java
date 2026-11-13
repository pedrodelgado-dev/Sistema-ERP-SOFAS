package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.ConsumoRealRepository;
import pe.erp.sofas.repository.EmpleadoRepository;
import pe.erp.sofas.repository.OrdenProduccionRepository;

import java.time.LocalDate;
import java.util.*;

@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmpleadoService {

    /** Productividad del mes: sofas terminados por la persona y monto a destajo de su etapa. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public record Productividad(@EquipoPLMD_MGML_AMCS_DRJB
      Empleado empleado, @EquipoPLMD_MGML_AMCS_DRJB
        int sofasTerminados, @EquipoPLMD_MGML_AMCS_DRJB
          int sofasEnEtapa, @EquipoPLMD_MGML_AMCS_DRJB
            double tarifa, @EquipoPLMD_MGML_AMCS_DRJB
              double destajo) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final EmpleadoRepository repository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final OrdenProduccionRepository opRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ConsumoRealRepository consumoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ParametroService parametros;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Empleado> listar() {
        return repository.findAllByOrderByCodigoAsc();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Empleado> taller() {
        return listar().stream().filter(e -> e.isTaller() && !"Inactivo".equals(e.getEstado())).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Empleado empleado(Long id) {
        return repository.findById(id).orElseThrow(() -> new NegocioException("El empleado ya no existe."));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Empleado guardar(Empleado e) {
        if (e.getNombre() == null || e.getNombre().isBlank()) throw new NegocioException("El nombre es obligatorio.");
        if (e.getDni() != null && !e.getDni().isBlank() && !e.getDni().matches("\\d{8}")) throw new NegocioException("El DNI debe tener 8 dígitos.");
        if (e.getId() == null) e.setCodigo(Codigos.siguiente(repository.findAll(), Empleado::getCodigo, "E-", 2));
        return repository.save(e);
    }

    /**
     * Sofas terminados del mes por persona (OP en Listo de las que fue responsable) y, para el destajo,
     * sofas que pasaron por su etapa este mes (OP en curso que ya superaron su etapa + terminadas).
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Productividad> productividad() {
        LocalDate inicio = AppClock.hoy().withDayOfMonth(1);
        List<OrdenProduccion> ops = opRepository.findAll().stream().filter(o -> !o.getAnulada()).toList();
        List<Productividad> r = new ArrayList<>();
        for (Empleado e : taller()) {
            int terminados = ops.stream().filter(o -> o.getEtapa() == Etapa.LISTO && o.getFechaFin() != null
                            && !o.getFechaFin().isBefore(inicio) && o.getResponsable() != null && o.getResponsable().getId().equals(e.getId()))
                    .mapToInt(OrdenProduccion::getCantidad).sum();
            int enEtapa = 0;
            if (e.getEtapa() != null) {
                enEtapa = ops.stream().filter(o -> o.getEtapa().ordinal() > e.getEtapa().ordinal()
                                && (o.getEtapa() != Etapa.LISTO || (o.getFechaFin() != null && !o.getFechaFin().isBefore(inicio))))
                        .mapToInt(OrdenProduccion::getCantidad).sum();
                // Historico del mes (OP ya cerradas por consumo) para dar una base realista.
                enEtapa += historicoMes(e);
            }
            double tarifa = e.getEtapa() == null ? 0 : parametros.tarifaDestajo(e.getEtapa());
            r.add(new Productividad(e, terminados + historicoTerminados(e), enEtapa, tarifa, tarifa * enEtapa));
        }
        return r;
    }

    /** Ordenes cerradas este mes (registradas en consumo real): pasaron por todas las etapas. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private int historicoMes(Empleado e) {
        LocalDate inicio = AppClock.hoy().withDayOfMonth(1);
        return (int) consumoRepository.findAll().stream().filter(c -> c.getFecha() != null && !c.getFecha().isBefore(inicio))
                .map(ConsumoReal::getOpCodigo).distinct().count();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private int historicoTerminados(Empleado e) {
        LocalDate inicio = AppClock.hoy().withDayOfMonth(1);
        return (int) consumoRepository.findAll().stream().filter(c -> c.getFecha() != null && !c.getFecha().isBefore(inicio)
                        && e.getNombre().equals(c.getResponsable())).map(ConsumoReal::getOpCodigo).distinct().count();
    }
}
