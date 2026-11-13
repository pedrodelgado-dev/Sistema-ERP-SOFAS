package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.ConsumoReal;
import pe.erp.sofas.model.FichaTecnica;
import pe.erp.sofas.repository.ConsumoRealRepository;

import java.util.*;
import java.util.stream.Collectors;

/** Consumo real frente al estimado de la ficha: desvios, sobremerma y ajuste de fichas. */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsumoService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Ranking(@EquipoPLMD_MGML_AMCS_DRJB
      String nombre, @EquipoPLMD_MGML_AMCS_DRJB
        double desvioPct, @EquipoPLMD_MGML_AMCS_DRJB
          double valor, @EquipoPLMD_MGML_AMCS_DRJB
            int ordenes) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Kpis(@EquipoPLMD_MGML_AMCS_DRJB
      double desvioPromedioPct, @EquipoPLMD_MGML_AMCS_DRJB
        double costoSobremerma, @EquipoPLMD_MGML_AMCS_DRJB
          long ordenes, @EquipoPLMD_MGML_AMCS_DRJB
            String materialMayorDesvio,
                       @EquipoPLMD_MGML_AMCS_DRJB
                       double desvioMaterialPct) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ConsumoRealRepository repository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final FichaService fichaService;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<ConsumoReal> registros() {
        return repository.findAllByOrderByFechaDescIdDesc();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<ConsumoReal> filtrar(String op, String producto) {
        return registros().stream()
                .filter(c -> op == null || op.equals(c.getOpCodigo()))
                .filter(c -> producto == null || producto.equals(c.getProducto()))
                .toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public ConsumoReal editarReal(Long id, double real) {
        if (real < 0) throw new NegocioException("El consumo real no puede ser negativo.");
        ConsumoReal c = repository.findById(id).orElseThrow();
        c.setRealConsumido(real);
        return repository.save(c);
    }

    /** Desvio valorizado: Σ (real − estimado) × costo / Σ estimado × costo. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static double desvioPct(Collection<ConsumoReal> l) {
        double est = l.stream().mapToDouble(c -> c.getEstimado() * c.getMaterial().getCosto()).sum();
        double real = l.stream().mapToDouble(c -> c.getRealConsumido() * c.getMaterial().getCosto()).sum();
        return est == 0 ? 0 : (real - est) * 100 / est;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Kpis kpis(List<ConsumoReal> l) {
        double sobremerma = l.stream().filter(c -> c.getDiferencia() > 0).mapToDouble(ConsumoReal::getDiferenciaValor).sum();
        Map<String, List<ConsumoReal>> porMat = l.stream().collect(Collectors.groupingBy(c -> c.getMaterial().getNombre()));
        var peor = porMat.entrySet().stream().max(Comparator.comparingDouble(e -> desvioPct(e.getValue())));
        return new Kpis(desvioPct(l), sobremerma, l.stream().map(ConsumoReal::getOpCodigo).distinct().count(),
                peor.map(Map.Entry::getKey).orElse("—"), peor.map(e -> desvioPct(e.getValue())).orElse(0d));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Ranking> porTrabajador(List<ConsumoReal> l) {
        return ranking(l, ConsumoReal::getResponsable);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Ranking> porProducto(List<ConsumoReal> l) {
        return ranking(l, ConsumoReal::getProducto);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private List<Ranking> ranking(List<ConsumoReal> l, java.util.function.Function<ConsumoReal, String> clave) {
        Map<String, List<ConsumoReal>> g = l.stream().collect(Collectors.groupingBy(clave, LinkedHashMap::new, Collectors.toList()));
        List<Ranking> r = new ArrayList<>();
        g.forEach((k, v) -> r.add(new Ranking(k, desvioPct(v), v.stream().mapToDouble(ConsumoReal::getDiferenciaValor).sum(),
                (int) v.stream().map(ConsumoReal::getOpCodigo).distinct().count())));
        r.sort(Comparator.comparingDouble(Ranking::desvioPct).reversed());
        return r;
    }

    /**
     * Crea una nueva version en Borrador de la ficha con las cantidades × (real / estimado) promedio de las
     * OP registradas con cualquier version de esa ficha.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public FichaTecnica ajustarFicha(String codigoFicha, List<ConsumoReal> base) {
        FichaTecnica f = fichaService.aprobada(codigoFicha);
        if (f == null) throw new NegocioException("No hay ficha aprobada " + codigoFicha + " para ajustar.");
        Map<String, double[]> acc = new HashMap<>();
        for (ConsumoReal c : base) {
            if (c.getFichaUid() == null || !c.getFichaUid().startsWith(codigoFicha + "v")) continue;
            double[] a = acc.computeIfAbsent(c.getMaterial().getCodigo(), k -> new double[2]);
            a[0] += c.getRealConsumido();
            a[1] += c.getEstimado();
        }
        if (acc.isEmpty()) throw new NegocioException("No hay consumos registrados con la ficha " + codigoFicha + ".");
        Map<String, Double> factores = new HashMap<>();
        acc.forEach((k, a) -> {
            if (a[1] > 0) factores.put(k, a[0] / a[1]);
        });
        return fichaService.ajustarConFactores(f, factores);
    }

    /** Codigo de ficha (FT-001) de los registros filtrados, si todos son de la misma. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public Optional<String> fichaComun(List<ConsumoReal> l) {
        Set<String> codigos = l.stream().map(ConsumoReal::getFichaUid).filter(Objects::nonNull)
                .map(u -> u.substring(0, u.indexOf('v'))).collect(Collectors.toSet());
        return codigos.size() == 1 ? Optional.of(codigos.iterator().next()) : Optional.empty();
    }
}
