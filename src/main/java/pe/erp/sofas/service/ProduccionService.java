package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.*;
import pe.erp.sofas.service.calculo.Explosion;
import pe.erp.sofas.service.calculo.Medidas;
import pe.erp.sofas.service.calculo.Opciones;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

/**
 * Ordenes de produccion y sus reglas de materiales:
 * <ul>
 *   <li>Pendiente → etapa productiva: con ficha reserva los materiales; sin ficha exige autorizacion.</li>
 *   <li>Volver a Pendiente o anular libera las reservas.</li>
 *   <li>Pasar a Listo consume lo reservado (kardex "Consumo OP") y guarda el consumo real.</li>
 *   <li>Disponible = stock − reservado (+ lo reservado por la misma OP); una reserva nunca lo deja negativo.</li>
 * </ul>
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProduccionService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final double EPS = 1e-6;

    /** Estado de un material dentro de una OP. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public record LineaMaterial(@EquipoPLMD_MGML_AMCS_DRJB
      Material material, @EquipoPLMD_MGML_AMCS_DRJB
        String componentes, @EquipoPLMD_MGML_AMCS_DRJB
          double necesario, @EquipoPLMD_MGML_AMCS_DRJB
            double reservado,
                                @EquipoPLMD_MGML_AMCS_DRJB
                                double disponible, @EquipoPLMD_MGML_AMCS_DRJB
                                  String estado, @EquipoPLMD_MGML_AMCS_DRJB
                                    double costo) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public double falta() {
            return Math.max(0, necesario - Math.max(reservado, "Disponible".equals(estado) ? necesario : reservado));
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record EstadoMateriales(@EquipoPLMD_MGML_AMCS_DRJB
      Explosion.Resultado explosion, @EquipoPLMD_MGML_AMCS_DRJB
        List<LineaMaterial> lineas) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public long faltantes() {
            return lineas.stream().filter(l -> Set.of("Parcial", "Faltante", "Faltará", "Bajo pedido").contains(l.estado())).count();
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public boolean completo() {
            return lineas.stream().allMatch(l -> "Reservado".equals(l.estado()) || "Consumido".equals(l.estado()));
        }
    }

    /** Indicador de la tarjeta del Kanban. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public record Indicador(@EquipoPLMD_MGML_AMCS_DRJB
      String texto, @EquipoPLMD_MGML_AMCS_DRJB
        String variante, @EquipoPLMD_MGML_AMCS_DRJB
          String icono) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Faltante(@EquipoPLMD_MGML_AMCS_DRJB
      Material material, @EquipoPLMD_MGML_AMCS_DRJB
        double necesario, @EquipoPLMD_MGML_AMCS_DRJB
          double reservado) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public double falta() {
            return Math.max(0, necesario - reservado);
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record ResultadoReserva(@EquipoPLMD_MGML_AMCS_DRJB
      int reservados, @EquipoPLMD_MGML_AMCS_DRJB
        int lineas, @EquipoPLMD_MGML_AMCS_DRJB
          List<Faltante> faltantes) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public String texto() {
            return reservados + " materiales reservados" + (faltantes.isEmpty() ? "" : " · faltan " + faltantes.size());
        }
    }

    /** Que hace falta para mover una OP a una etapa. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Requisito { @EquipoPLMD_MGML_AMCS_DRJB
        NINGUNO, @EquipoPLMD_MGML_AMCS_DRJB
         AUTORIZACION, @EquipoPLMD_MGML_AMCS_DRJB
          CONFIRMAR_FALTANTES, @EquipoPLMD_MGML_AMCS_DRJB
           CONFIRMAR_CONSUMO, @EquipoPLMD_MGML_AMCS_DRJB
            LIBERAR }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Evaluacion(@EquipoPLMD_MGML_AMCS_DRJB
      Requisito requisito, @EquipoPLMD_MGML_AMCS_DRJB
        List<Faltante> faltantes) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record NuevaOP(@EquipoPLMD_MGML_AMCS_DRJB
      Long productoId, @EquipoPLMD_MGML_AMCS_DRJB
        String nombrePersonalizado, @EquipoPLMD_MGML_AMCS_DRJB
          int cantidad, @EquipoPLMD_MGML_AMCS_DRJB
            Long clienteId, @EquipoPLMD_MGML_AMCS_DRJB
              Long responsableId,
                          @EquipoPLMD_MGML_AMCS_DRJB
                          Prioridad prioridad, @EquipoPLMD_MGML_AMCS_DRJB
                            LocalDate inicio, @EquipoPLMD_MGML_AMCS_DRJB
                              LocalDate entrega, @EquipoPLMD_MGML_AMCS_DRJB
                                Long telaId, @EquipoPLMD_MGML_AMCS_DRJB
                                  Medidas medidas,
                          @EquipoPLMD_MGML_AMCS_DRJB
                          Map<String, String> sustituciones, @EquipoPLMD_MGML_AMCS_DRJB
                             List<LineaExtra> extras, @EquipoPLMD_MGML_AMCS_DRJB
                               String pedidoCodigo,
                          @EquipoPLMD_MGML_AMCS_DRJB
                          String retapizadoCodigo, @EquipoPLMD_MGML_AMCS_DRJB
                            String especificacion, @EquipoPLMD_MGML_AMCS_DRJB
                              Etapa etapa, @EquipoPLMD_MGML_AMCS_DRJB
                                BigDecimal costo,
                          @EquipoPLMD_MGML_AMCS_DRJB
                          String fichaUid) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Kpis(@EquipoPLMD_MGML_AMCS_DRJB
      long activas, @EquipoPLMD_MGML_AMCS_DRJB
        long enTiempo, @EquipoPLMD_MGML_AMCS_DRJB
          long retrasadas, @EquipoPLMD_MGML_AMCS_DRJB
            long terminadasMes, @EquipoPLMD_MGML_AMCS_DRJB
              long sofasEnProceso, @EquipoPLMD_MGML_AMCS_DRJB
                long ordenesEnProceso) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final OrdenProduccionRepository repository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ReservaRepository reservaRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final MaterialRepository materialRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProductoRepository productoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ClienteRepository clienteRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final EmpleadoRepository empleadoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final TelaRepository telaRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PedidoRepository pedidoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final RetapizadoRepository retapizadoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ConsumoRealRepository consumoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final FichaService fichaService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final InventarioService inventarioService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final TelaService telaService;

    // ============================================================ consultas

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<OrdenProduccion> ordenes() {
        return repository.findAllByOrderByCodigoAsc().stream().filter(o -> !o.getAnulada()).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<OrdenProduccion> activas() {
        return ordenes().stream().filter(OrdenProduccion::isActiva).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public OrdenProduccion op(Long id) {
        return repository.findById(id).orElseThrow(() -> new NegocioException("La orden ya no existe."));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Optional<OrdenProduccion> porCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String siguienteCodigo() {
        return Codigos.siguiente(repository.findAll(), OrdenProduccion::getCodigo, "OP-", 4);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Kpis kpis() {
        List<OrdenProduccion> act = activas();
        long retr = act.stream().filter(OrdenProduccion::isRetrasada).count();
        LocalDate inicio = AppClock.hoy().withDayOfMonth(1);
        long term = ordenes().stream().filter(o -> o.getEtapa() == Etapa.LISTO && o.getFechaFin() != null
                && !o.getFechaFin().isBefore(inicio)).count();
        List<OrdenProduccion> proc = act.stream().filter(o -> o.getEtapa().productiva()).toList();
        return new Kpis(act.size(), act.size() - retr, retr, term,
                proc.stream().mapToLong(OrdenProduccion::getCantidad).sum(), proc.size());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Map<Etapa, Long> porEtapa() {
        Map<Etapa, Long> m = new EnumMap<>(Etapa.class);
        for (Etapa e : Etapa.values()) m.put(e, 0L);
        ordenes().stream().filter(o -> o.isActiva() || o.getEtapa() == Etapa.LISTO)
                .forEach(o -> m.merge(o.getEtapa(), (long) o.getCantidad(), Long::sum));
        return m;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public FichaTecnica ficha(OrdenProduccion op) {
        return fichaService.porUid(op.getFichaUid()).orElse(null);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Opciones opciones(OrdenProduccion op) {
        FichaTecnica f = ficha(op);
        Medidas m = null;
        if (op.getLargo() != null && op.getLargo() > 0 && f != null) {
            m = new Medidas(op.getLargo(), nz(op.getFondo(), f.getFondo()), nz(op.getAlto(), f.getAlto()),
                    op.getCuerpos() == null ? f.getCuerpos() : op.getCuerpos());
        }
        return new Opciones(m, op.getSustituciones(), op.getExtras(), false, 0, 0);
    }

    /** Explosion de la ficha para la OP (null si no tiene ficha). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public Explosion.Resultado requerimiento(OrdenProduccion op) {
        FichaTecnica f = ficha(op);
        if (f == null) return null;
        return fichaService.explotar(f, op.getCantidad(), opciones(op));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double reservadoPor(OrdenProduccion op, Material m) {
        return reservaRepository.findByOpIdAndMaterialId(op.getId(), m.getId()).map(Reserva::getCantidad).orElse(0d);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public EstadoMateriales estadoMateriales(OrdenProduccion op) {
        Explosion.Resultado r = requerimiento(op);
        if (r == null) return new EstadoMateriales(null, List.of());
        Map<String, Set<String>> componentes = new LinkedHashMap<>();
        for (Explosion.Linea l : r.lineas()) {
            componentes.computeIfAbsent(l.material().getCodigo(), k -> new LinkedHashSet<>()).add(l.componente().nombre());
        }
        Map<Long, Double> reservas = new HashMap<>();
        reservaRepository.findByOpId(op.getId()).forEach(x -> reservas.put(x.getMaterial().getId(), x.getCantidad()));
        List<LineaMaterial> lineas = new ArrayList<>();
        for (Explosion.PorMaterial pm : r.porMaterial()) {
            Material m = materialRepository.findById(pm.material().getId()).orElse(pm.material());
            double res = reservas.getOrDefault(m.getId(), 0d);
            double disp = Math.max(0, m.getStock() - (m.getReservado() - res));
            String estado;
            if (op.getEtapa() == Etapa.LISTO) estado = "Consumido";
            else if (res >= pm.cantidad() - EPS) estado = "Reservado";
            else if (Boolean.TRUE.equals(m.getBajoPedido()) && m.getStock() <= 0) estado = "Bajo pedido";
            else if (op.getEtapa() == Etapa.PENDIENTE) estado = disp >= pm.cantidad() - EPS ? "Disponible" : "Faltará";
            else estado = res > EPS ? "Parcial" : "Faltante";
            lineas.add(new LineaMaterial(m, String.join(", ", componentes.get(m.getCodigo())), pm.cantidad(), res, disp, estado, pm.costo()));
        }
        return new EstadoMateriales(r, lineas);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Indicador indicador(OrdenProduccion op) {
        if (op.isSinFicha()) {
            return op.getAutorizacion() != null
                    ? new Indicador("Sin ficha · autorizado", "amber", "VERIFIED_USER")
                    : new Indicador("Sin ficha aprobada", "amber", "WARNING");
        }
        if (op.getEtapa() == Etapa.LISTO) return new Indicador("Materiales consumidos", "green", "DONE_ALL");
        EstadoMateriales e = estadoMateriales(op);
        long f = e.faltantes();
        if (op.getEtapa() == Etapa.PENDIENTE && !e.completo()) {
            return f == 0 ? new Indicador("Materiales disponibles", "blue", "ALL_INBOX")
                    : new Indicador(f == 1 ? "Faltará 1 material" : "Faltarán " + f + " materiales", "red", "REPORT_PROBLEM");
        }
        return f == 0 ? new Indicador("Materiales reservados", "green", "LOCK")
                : new Indicador("Faltan " + f + " material" + (f == 1 ? "" : "es"), "red", "REPORT_PROBLEM");
    }

    /** Lo que faltaria si se reservara ahora (sin modificar nada). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Faltante> faltantesSiReserva(OrdenProduccion op) {
        Explosion.Resultado r = requerimiento(op);
        if (r == null) return List.of();
        List<Faltante> f = new ArrayList<>();
        for (Explosion.PorMaterial pm : r.porMaterial()) {
            Material m = materialRepository.findById(pm.material().getId()).orElse(pm.material());
            double res = reservadoPor(op, m);
            double disp = Math.max(0, m.getStock() - (m.getReservado() - res));
            double posible = Math.min(pm.cantidad(), disp);
            if (posible < pm.cantidad() - EPS) f.add(new Faltante(m, pm.cantidad(), posible));
        }
        return f;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Evaluacion evaluar(OrdenProduccion op, Etapa destino) {
        Etapa origen = op.getEtapa();
        if (destino == origen) return new Evaluacion(Requisito.NINGUNO, List.of());
        if (origen == Etapa.LISTO) throw new NegocioException("La orden " + op.getCodigo() + " ya está lista y su material fue consumido.");
        if (destino == Etapa.LISTO) {
            if (origen == Etapa.PENDIENTE) throw new NegocioException("Primero pasa la orden a una etapa de producción.");
            return new Evaluacion(Requisito.CONFIRMAR_CONSUMO, List.of());
        }
        if (destino == Etapa.PENDIENTE) return new Evaluacion(Requisito.LIBERAR, List.of());
        if (origen == Etapa.PENDIENTE) {
            if (op.isSinFicha()) {
                return new Evaluacion(op.getAutorizacion() == null ? Requisito.AUTORIZACION : Requisito.NINGUNO, List.of());
            }
            List<Faltante> f = faltantesSiReserva(op);
            return new Evaluacion(f.isEmpty() ? Requisito.NINGUNO : Requisito.CONFIRMAR_FALTANTES, f);
        }
        return new Evaluacion(Requisito.NINGUNO, List.of());
    }

    // ============================================================ acciones

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public OrdenProduccion crear(NuevaOP d) {
        if (d.cantidad() <= 0) throw new NegocioException("La cantidad debe ser mayor a cero.");
        if (d.entrega() != null && d.inicio() != null && d.entrega().isBefore(d.inicio())) {
            throw new NegocioException("La entrega no puede ser antes del inicio.");
        }
        Producto p = d.productoId() == null ? null : productoRepository.findById(d.productoId()).orElseThrow();
        if (p == null && (d.nombrePersonalizado() == null || d.nombrePersonalizado().isBlank())) {
            throw new NegocioException("Elige el modelo de sofá.");
        }
        Tela tela = d.telaId() == null ? null : telaRepository.findById(d.telaId()).orElseThrow();
        FichaTecnica ficha = d.fichaUid() != null ? fichaService.porUid(d.fichaUid()).orElse(null)
                : p == null ? null : fichaService.aprobadaDe(p);
        Map<String, String> sust = new HashMap<>(d.sustituciones() == null ? Map.of() : d.sustituciones());
        if (tela != null && ficha != null) {
            for (String generico : materialesTela(ficha)) sust.putIfAbsent(generico, tela.getTipo().materialCodigo());
        }
        OrdenProduccion op = OrdenProduccion.builder()
                .codigo(siguienteCodigo()).producto(p).nombrePersonalizado(d.nombrePersonalizado())
                .cantidad(d.cantidad())
                .cliente(d.clienteId() == null ? null : clienteRepository.findById(d.clienteId()).orElse(null))
                .responsable(d.responsableId() == null ? null : empleadoRepository.findById(d.responsableId()).orElse(null))
                .prioridad(d.prioridad() == null ? Prioridad.MEDIA : d.prioridad())
                .etapa(d.etapa() == null ? Etapa.PENDIENTE : d.etapa())
                .fechaInicio(d.inicio() == null ? AppClock.hoy() : d.inicio()).fechaEntrega(d.entrega())
                .fichaUid(ficha == null ? null : ficha.getUid()).tela(tela)
                .largo(d.medidas() == null ? null : (int) Math.round(d.medidas().largo()))
                .fondo(d.medidas() == null ? null : (int) Math.round(d.medidas().fondo()))
                .alto(d.medidas() == null ? null : (int) Math.round(d.medidas().alto()))
                .cuerpos(d.medidas() == null ? null : d.medidas().cuerpos())
                .sustituciones(sust).extras(new ArrayList<>(d.extras() == null ? List.of() : d.extras()))
                .pedidoCodigo(d.pedidoCodigo()).retapizadoCodigo(d.retapizadoCodigo())
                .especificacion(d.especificacion()).build();
        op = repository.save(op);
        op.setCosto(d.costo() != null ? d.costo() : costoCalculado(op));
        return repository.save(op);
    }

    /** Materiales de tela genericos de la ficha (o de sus hijas). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public Set<String> materialesTela(FichaTecnica f) {
        Set<String> s = new HashSet<>();
        List<FichaTecnica> fichas = Boolean.TRUE.equals(f.getCompuesta()) ? fichaService.hijas(f) : List.of(f);
        for (FichaTecnica x : fichas) {
            for (LineaBOM l : x.getLineas()) if (l.getMaterial().isTela()) s.add(l.getMaterial().getCodigo());
        }
        return s;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public BigDecimal costoCalculado(OrdenProduccion op) {
        Explosion.Resultado r = requerimiento(op);
        if (r != null) return BigDecimal.valueOf(r.costoEstandar()).setScale(2, RoundingMode.HALF_UP);
        if (op.getProducto() != null && op.getProducto().getCosto() != null) {
            return op.getProducto().getCosto().multiply(BigDecimal.valueOf(op.getCantidad()));
        }
        return BigDecimal.ZERO;
    }

    /** Reserva lo que haya disponible de cada material, sin dejar nunca el disponible en negativo. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public ResultadoReserva reservar(Long opId) {
        OrdenProduccion op = op(opId);
        Explosion.Resultado r = requerimiento(op);
        if (r == null) throw new NegocioException("La orden no tiene ficha aprobada: no se puede reservar material.");
        if (op.getEtapa() == Etapa.LISTO) throw new NegocioException("La orden ya consumió su material.");
        int reservados = 0;
        List<Faltante> faltantes = new ArrayList<>();
        List<Explosion.PorMaterial> req = r.porMaterial();
        Set<Long> requeridos = new HashSet<>();
        for (Explosion.PorMaterial pm : req) {
            Material m = materialRepository.findById(pm.material().getId()).orElseThrow();
            requeridos.add(m.getId());
            Reserva res = reservaRepository.findByOpIdAndMaterialId(op.getId(), m.getId()).orElse(null);
            double actual = res == null ? 0 : res.getCantidad();
            double disponible = Math.max(0, m.getStock() - (m.getReservado() - actual));
            double nuevo = Math.min(pm.cantidad(), disponible);
            double delta = nuevo - actual;
            if (Math.abs(delta) > EPS) {
                m.setReservado(Math.max(0, m.getReservado() + delta));
                materialRepository.save(m);
                if (res == null) res = Reserva.builder().op(op).material(m).cantidad(0d).build();
                res.setCantidad(nuevo);
                if (nuevo <= EPS) reservaRepository.delete(res);
                else reservaRepository.save(res);
                inventarioService.registrar(m, delta > 0 ? TipoMovimiento.RESERVA : TipoMovimiento.LIBERACION,
                        Math.abs(delta), op.getCodigo(), (delta > 0 ? "Reserva " : "Ajuste de reserva ") + op.getProductoNombre());
            }
            if (nuevo >= pm.cantidad() - EPS) reservados++;
            else faltantes.add(new Faltante(m, pm.cantidad(), nuevo));
        }
        // Reservas de materiales que la ficha ya no pide (cambio de opciones): se liberan.
        for (Reserva x : reservaRepository.findByOpId(op.getId())) {
            if (!requeridos.contains(x.getMaterial().getId())) liberarReserva(op, x);
        }
        return new ResultadoReserva(reservados, req.size(), faltantes);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public int liberar(Long opId) {
        OrdenProduccion op = op(opId);
        List<Reserva> reservas = reservaRepository.findByOpId(op.getId());
        for (Reserva r : reservas) liberarReserva(op, r);
        return reservas.size();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private void liberarReserva(OrdenProduccion op, Reserva r) {
        Material m = materialRepository.findById(r.getMaterial().getId()).orElseThrow();
        m.setReservado(Math.max(0, m.getReservado() - r.getCantidad()));
        materialRepository.save(m);
        inventarioService.registrar(m, TipoMovimiento.LIBERACION, r.getCantidad(), op.getCodigo(), "Liberación " + op.getProductoNombre());
        reservaRepository.delete(r);
    }

    /**
     * Mueve la OP de etapa aplicando las reglas. {@code autorizacion} es obligatoria si sale de Pendiente sin ficha.
     * Para pasar a Listo usar {@link #completar}.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public String mover(Long opId, Etapa destino, String autorizacion) {
        OrdenProduccion op = op(opId);
        Etapa origen = op.getEtapa();
        if (destino == origen) return null;
        if (destino == Etapa.LISTO) throw new NegocioException("Confirma el consumo para pasar a Listo.");
        if (origen == Etapa.LISTO) throw new NegocioException("La orden ya está lista.");
        String mensaje;
        if (destino == Etapa.PENDIENTE) {
            int n = liberar(opId);
            mensaje = op.getCodigo() + " volvió a Pendiente" + (n > 0 ? " · " + n + " reservas liberadas" : "");
        } else if (origen == Etapa.PENDIENTE) {
            if (op.isSinFicha()) {
                if (op.getAutorizacion() == null) {
                    if (autorizacion == null || autorizacion.isBlank()) throw new NegocioException("Se requiere autorización de Supervisor.");
                    op.setAutorizacion(autorizacion);
                }
                mensaje = op.getCodigo() + " pasó a " + destino.nombre() + " sin reservar (sin ficha · autorizado)";
            } else {
                ResultadoReserva rr = reservar(opId);
                mensaje = op.getCodigo() + " pasó a " + destino.nombre() + " · " + rr.texto();
            }
            if (op.getFechaInicio() == null || op.getFechaInicio().isAfter(AppClock.hoy())) op.setFechaInicio(AppClock.hoy());
        } else {
            mensaje = op.getCodigo() + " pasó a " + destino.nombre();
        }
        op = op(opId);
        op.setEtapa(destino);
        if (op.getAutorizacion() == null && autorizacion != null && op.isSinFicha()) op.setAutorizacion(autorizacion);
        repository.save(op);
        actualizarPedido(op);
        return mensaje;
    }

    /**
     * Pasa a Listo: descuenta del stock el consumo real (por defecto lo reservado), registra "Consumo OP"
     * en el kardex, libera el resto de la reserva y guarda el consumo real frente al estimado.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public String completar(Long opId, Map<Long, Double> reales) {
        OrdenProduccion op = op(opId);
        if (op.getEtapa() == Etapa.LISTO) throw new NegocioException("La orden ya está lista.");
        if (op.getEtapa() == Etapa.PENDIENTE) throw new NegocioException("Primero pasa la orden a una etapa de producción.");
        Explosion.Resultado r = requerimiento(op);
        int lineas = 0;
        if (r != null) {
            Map<Long, Double> estimados = new HashMap<>();
            for (Explosion.PorMaterial pm : r.porMaterial()) estimados.put(pm.material().getId(), pm.cantidad());
            Map<Long, Reserva> reservas = new HashMap<>();
            reservaRepository.findByOpId(op.getId()).forEach(x -> reservas.put(x.getMaterial().getId(), x));
            for (var e : estimados.entrySet()) {
                Material m = materialRepository.findById(e.getKey()).orElseThrow();
                Reserva res = reservas.get(m.getId());
                double reservado = res == null ? 0 : res.getCantidad();
                double real = reales != null && reales.containsKey(m.getId()) ? reales.get(m.getId()) : reservado;
                double libre = Math.max(0, m.getStock() - m.getReservado());
                real = Math.max(0, Math.min(real, reservado + libre));
                m.setReservado(Math.max(0, m.getReservado() - reservado));
                materialRepository.save(m);
                if (res != null) reservaRepository.delete(res);
                if (real > EPS) {
                    inventarioService.registrar(m, TipoMovimiento.CONSUMO_OP, real, op.getCodigo(), "Consumo " + op.getProductoNombre());
                    descontarTela(op, m, real);
                }
                consumoRepository.save(ConsumoReal.builder().opCodigo(op.getCodigo()).producto(op.getProductoNombre())
                        .fichaUid(op.getFichaUid()).responsable(op.getResponsable() == null ? "—" : op.getResponsable().getNombre())
                        .material(m).estimado(e.getValue()).realConsumido(real).fecha(AppClock.hoy()).build());
                lineas++;
            }
        }
        op = op(opId);
        op.setEtapa(Etapa.LISTO);
        op.setFechaFin(AppClock.hoy());
        repository.save(op);
        actualizarPedido(op);
        return op.getCodigo() + " lista" + (lineas > 0 ? " · consumo de " + lineas + " materiales registrado" : "");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private void descontarTela(OrdenProduccion op, Material m, double metros) {
        TipoTela tipo = TipoTela.porMaterial(m.getCodigo());
        if (tipo == null) return;
        Tela t = op.getTela() != null && op.getTela().getTipo() == tipo ? op.getTela()
                : telaService.porTipo(tipo).stream().filter(x -> !x.getDescontinuada())
                .max(Comparator.comparingDouble(Tela::getStock)).orElse(null);
        if (t != null) telaService.descontar(t, metros);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public String anular(Long opId) {
        OrdenProduccion op = op(opId);
        if (op.getEtapa() == Etapa.LISTO) throw new NegocioException("No se puede anular una orden ya lista.");
        int n = liberar(opId);
        op = op(opId);
        op.setAnulada(true);
        repository.save(op);
        if (op.getPedidoCodigo() != null) {
            pedidoRepository.findByCodigo(op.getPedidoCodigo()).ifPresent(p -> {
                p.setOpCodigo(null);
                if (p.getEstado() == EstadoPedido.EN_PRODUCCION) p.setEstado(EstadoPedido.CONFIRMADO);
                pedidoRepository.save(p);
            });
        }
        return op.getCodigo() + " anulada" + (n > 0 ? " · " + n + " reservas liberadas" : "");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public OrdenProduccion autorizar(Long opId, String texto) {
        OrdenProduccion op = op(opId);
        op.setAutorizacion(texto);
        return repository.save(op);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public OrdenProduccion actualizarDatos(Long opId, Long responsableId, Prioridad prioridad, LocalDate entrega) {
        OrdenProduccion op = op(opId);
        if (responsableId != null) op.setResponsable(empleadoRepository.findById(responsableId).orElse(null));
        if (prioridad != null) op.setPrioridad(prioridad);
        if (entrega != null) op.setFechaEntrega(entrega);
        return repository.save(op);
    }

    /** Sincroniza el pedido o retapizado vinculado con la etapa de la OP. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private void actualizarPedido(OrdenProduccion op) {
        if (op.getPedidoCodigo() != null) {
            pedidoRepository.findByCodigo(op.getPedidoCodigo()).ifPresent(p -> {
                if (op.getEtapa() == Etapa.LISTO && p.getEstado() == EstadoPedido.EN_PRODUCCION) p.setEstado(EstadoPedido.LISTO);
                else if (op.getEtapa().productiva() && p.getEstado() == EstadoPedido.CONFIRMADO) p.setEstado(EstadoPedido.EN_PRODUCCION);
                pedidoRepository.save(p);
            });
        }
        if (op.getRetapizadoCodigo() != null && op.getEtapa() == Etapa.LISTO) {
            retapizadoRepository.findByCodigo(op.getRetapizadoCodigo()).ifPresent(rt -> {
                if (rt.getEstado() == EstadoRetapizado.EN_TALLER) {
                    rt.setEstado(EstadoRetapizado.LISTO);
                    retapizadoRepository.save(rt);
                }
            });
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static double nz(Integer v, Integer def) {
        return v != null && v > 0 ? v : def == null ? 0 : def;
    }
}
