package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.NotificacionEstadoRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Alertas calculadas sobre los datos (stock critico, ordenes retrasadas, entregas de manana, material agotado
 * y ordenes bloqueadas). Lo leido y lo descartado se guarda por clave.
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificacionService {

    /** tipo: danger · warn · info · success. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public record Notificacion(@EquipoPLMD_MGML_AMCS_DRJB
      String clave, @EquipoPLMD_MGML_AMCS_DRJB
        String tipo, @EquipoPLMD_MGML_AMCS_DRJB
          String icono, @EquipoPLMD_MGML_AMCS_DRJB
            String titulo, @EquipoPLMD_MGML_AMCS_DRJB
              String detalle, @EquipoPLMD_MGML_AMCS_DRJB
                String hace,
                               @EquipoPLMD_MGML_AMCS_DRJB
                               String modulo, @EquipoPLMD_MGML_AMCS_DRJB
                                 Object dato) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private final InventarioService inventarioService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final TelaService telaService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProduccionService produccionService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PedidoService pedidoService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final NotificacionEstadoRepository estadoRepository;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Notificacion> todas() {
        List<Notificacion> l = new ArrayList<>();
        List<String> criticos = new ArrayList<>();
        inventarioService.materiales().stream().filter(m -> "Crítico".equals(m.getEstado()))
                .forEach(m -> criticos.add(m.getNombre().toLowerCase(Locale.ROOT)));
        telaService.activas().stream().filter(t -> "Stock bajo".equals(t.getEstado()))
                .forEach(t -> criticos.add("tela " + t.getColor()));
        if (!criticos.isEmpty()) {
            l.add(new Notificacion("stock:" + String.join(",", criticos), "danger", "ALL_INBOX",
                    "Stock crítico: " + unir(criticos), "Por debajo del mínimo; revisa el requerimiento de materiales.",
                    "Hace 20 min", "mrp", null));
        }
        for (OrdenProduccion op : produccionService.activas()) {
            if (!op.isRetrasada()) continue;
            String cliente = op.getCliente() == null ? "stock" : primerosNombres(op.getCliente().getNombre());
            l.add(new Notificacion("retraso:" + op.getCodigo() + ":" + op.getFechaEntrega(), "danger", "ALARM",
                    "Orden retrasada: " + modeloCorto(op.getProductoNombre()) + " de " + cliente + ", vencida el " + AppClock.corta(op.getFechaEntrega()),
                    op.getCodigo() + " · sigue en " + op.getEtapa().nombre(),
                    "Hace 1 h", "produccion", op.getCodigo()));
        }
        var manana = AppClock.hoy().plusDays(1);
        for (Pedido p : pedidoService.listar()) {
            if (p.getEstado().cerrado() || !manana.equals(p.getFechaEntrega())) continue;
            l.add(new Notificacion("entrega:" + p.getCodigo(), "info", "LOCAL_SHIPPING",
                    "Entrega mañana: " + p.getCantidad() + (p.getCantidad() == 1 ? " sofá" : " sofás") + " para " + nombreCorto(p.getCliente().getNombre()),
                    p.getCodigo() + " · " + p.getProductoNombre() + " · " + p.getEstado().nombre(), "Hace 2 h", "pedidos", p.getCodigo()));
        }
        List<Material> agotados = inventarioService.materiales().stream()
                .filter(m -> "Agotado".equals(m.getEstado()) && !m.getBajoPedido()).toList();
        if (!agotados.isEmpty()) {
            l.add(new Notificacion("agotado:" + agotados.stream().map(Material::getCodigo).collect(Collectors.joining(",")), "warn", "REMOVE_SHOPPING_CART",
                    "Material agotado: " + unir(agotados.stream().map(m -> m.getNombre().toLowerCase(Locale.ROOT)).toList()),
                    "Sin disponible para nuevas órdenes. Genera la orden de compra.", "Hace 3 h", "inventario",
                    agotados.get(0).getCodigo()));
        }
        List<OrdenProduccion> bloqueadas = bloqueadas();
        if (!bloqueadas.isEmpty()) {
            l.add(new Notificacion("bloqueadas:" + bloqueadas.stream().map(OrdenProduccion::getCodigo).collect(Collectors.joining(",")), "warn", "BLOCK",
                    bloqueadas.size() + (bloqueadas.size() == 1 ? " orden bloqueada" : " órdenes bloqueadas") + " por falta de material",
                    bloqueadas.stream().map(OrdenProduccion::getCodigo).collect(Collectors.joining(", ")), "Hoy", "produccion", null));
        }
        Set<String> descartadas = new HashSet<>();
        estadoRepository.findAll().forEach(e -> {
            if (Boolean.TRUE.equals(e.getDescartada())) descartadas.add(e.getClave());
        });
        l.removeIf(n -> descartadas.contains(n.clave()));
        return l;
    }

    /** OP en produccion con materiales parciales o faltantes. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public List<OrdenProduccion> bloqueadas() {
        return produccionService.activas().stream().filter(o -> o.getEtapa().productiva() && !o.isSinFicha())
                .filter(o -> produccionService.estadoMateriales(o).faltantes() > 0).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean leida(Notificacion n) {
        return estadoRepository.findById(n.clave()).map(e -> Boolean.TRUE.equals(e.getLeida())).orElse(false);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public long noLeidas() {
        return todas().stream().filter(n -> !leida(n)).count();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public void marcarLeida(Notificacion n) {
        var e = estadoRepository.findById(n.clave()).orElse(new NotificacionEstado(n.clave(), false, false));
        e.setLeida(true);
        estadoRepository.save(e);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public void marcarTodasLeidas() {
        todas().forEach(this::marcarLeida);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public void descartar(Notificacion n) {
        var e = estadoRepository.findById(n.clave()).orElse(new NotificacionEstado(n.clave(), true, false));
        e.setDescartada(true);
        estadoRepository.save(e);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static String unir(List<String> l) {
        if (l.size() == 1) return l.get(0);
        return String.join(", ", l.subList(0, l.size() - 1)) + " y " + l.get(l.size() - 1);
    }

    /** "Reclinable 3 cuerpos" → "reclinable". */
    @EquipoPLMD_MGML_AMCS_DRJB
    private static String modeloCorto(String n) {
        String s = n.split(" · ")[0].replaceAll("\\s+\\d+ cuerpos?$", "");
        return s.toLowerCase(Locale.ROOT);
    }

    /** "Carmen Flores Pari" → "Carmen Flores". */
    @EquipoPLMD_MGML_AMCS_DRJB
    private static String primerosNombres(String n) {
        String[] p = n.split(" ");
        return p.length >= 2 ? p[0] + " " + p[1] : n;
    }

    /** "Hotel Royal Inn Juliaca S.A.C." → "el Hotel Royal Inn". */
    @EquipoPLMD_MGML_AMCS_DRJB
    private static String nombreCorto(String n) {
        if (n.startsWith("Hotel")) return "el " + n.replace(" Juliaca", "").replace(" S.A.C.", "");
        return primerosNombres(n);
    }
}
