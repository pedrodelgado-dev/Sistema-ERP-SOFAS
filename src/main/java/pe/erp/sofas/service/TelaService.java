package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.*;

import java.util.*;

/**
 * Muestrario de telas. Regla: el stock del material de inventario de cada tipo de tela es la suma de los
 * metros de sus colores no descontinuados.
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TelaService {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final TelaRepository telaRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final MaterialRepository materialRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final MovimientoRepository movimientoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProductoRepository productoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PedidoRepository pedidoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final OrdenProduccionRepository opRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final InventarioService inventarioService;

    /** Usos de una tela en el catalogo, pedidos y produccion. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public record Usos(@EquipoPLMD_MGML_AMCS_DRJB
      List<Producto> productos, @EquipoPLMD_MGML_AMCS_DRJB
        List<Pedido> pedidos, @EquipoPLMD_MGML_AMCS_DRJB
          List<OrdenProduccion> ordenes) {
        @EquipoPLMD_MGML_AMCS_DRJB
        public int total() {
            return productos.size() + pedidos.size() + ordenes.size();
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Tela> telas() {
        return telaRepository.findAllByOrderByCodigoAsc();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Tela> activas() {
        return telas().stream().filter(t -> !t.getDescontinuada()).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Tela> porTipo(TipoTela tipo) {
        return telas().stream().filter(t -> t.getTipo() == tipo).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Tela tela(Long id) {
        return telaRepository.findById(id).orElseThrow(() -> new NegocioException("La tela ya no existe."));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Optional<Tela> porCodigo(String codigo) {
        return telaRepository.findByCodigo(codigo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double metrosEnStock() {
        return activas().stream().mapToDouble(Tela::getStock).sum();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double valorMuestrario() {
        return activas().stream().mapToDouble(t -> t.getStock() * t.getTipo().costoMetro()).sum();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public long conAlerta() {
        return activas().stream().filter(t -> !"Disponible".equals(t.getEstado())).count();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String siguienteCodigo() {
        return Codigos.siguiente(telaRepository.findAll(), Tela::getCodigo, "TL-", 2);
    }

    /** Ajusta el stock del material de inventario a la suma de los colores activos. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public void sincronizar(TipoTela tipo) {
        materialRepository.findByCodigo(tipo.materialCodigo()).ifPresent(m -> {
            double suma = telaRepository.findByTipo(tipo).stream().filter(t -> !t.getDescontinuada()).mapToDouble(Tela::getStock).sum();
            m.setStock(suma);
            materialRepository.save(m);
        });
    }

    /** Entrada de rollo: suma al color y al material, y registra la Entrada en el kardex. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Tela entradaRollo(Long telaId, double metros, String referencia) {
        if (metros <= 0) throw new NegocioException("Ingresa los metros recibidos.");
        Tela t = tela(telaId);
        if (t.getDescontinuada()) throw new NegocioException("El color está descontinuado. Reactívalo para recibir rollos.");
        t.setStock(t.getStock() + metros);
        telaRepository.save(t);
        Material m = inventarioService.material(t.getTipo().materialCodigo());
        inventarioService.registrar(m, TipoMovimiento.ENTRADA, metros, referencia == null || referencia.isBlank() ? "ROLLO" : referencia,
                "Rollo " + t.getColor() + " " + t.getCodigo());
        return t;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Tela registrar(TipoTela tipo, String color, String hex, String codigoProveedor, double stock, double minimo) {
        if (color == null || color.isBlank()) throw new NegocioException("Escribe el nombre del color.");
        if (stock < 0 || minimo < 0) throw new NegocioException("Los metros no pueden ser negativos.");
        boolean existe = porTipo(tipo).stream().anyMatch(t -> t.getColor().equalsIgnoreCase(color.trim()));
        if (existe) throw new NegocioException("Ya existe el color " + color.trim() + " en " + tipo.nombre() + ".");
        Tela t = telaRepository.save(Tela.builder().codigo(siguienteCodigo()).tipo(tipo).color(color.trim())
                .hex(hex).codigoProveedor(codigoProveedor).stock(0d).minimo(minimo).build());
        if (stock > 0) entradaRollo(t.getId(), stock, "INI-" + t.getCodigo());
        return t;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Tela alternarDescontinuada(Long telaId) {
        Tela t = tela(telaId);
        Material m = inventarioService.material(t.getTipo().materialCodigo());
        boolean descontinuar = !t.getDescontinuada();
        if (descontinuar && t.getStock() > 0 && m.getStock() - t.getStock() < m.getReservado() - 1e-9) {
            throw new NegocioException("No se puede descontinuar: sus metros están reservados para producción.");
        }
        t.setDescontinuada(descontinuar);
        telaRepository.save(t);
        if (t.getStock() > 0) {
            // El material deja de contar (o vuelve a contar) los metros del color.
            movimientoRepository.save(Movimiento.builder().material(m)
                    .fecha(java.time.LocalDateTime.of(pe.erp.sofas.core.AppClock.hoy(), pe.erp.sofas.core.AppClock.HORA))
                    .tipo(TipoMovimiento.AJUSTE).cantidad(descontinuar ? -t.getStock() : t.getStock())
                    .referencia(t.getCodigo()).concepto((descontinuar ? "Descontinuado " : "Reactivado ") + t.getColor())
                    .saldo(m.getStock() + (descontinuar ? -t.getStock() : t.getStock())).usuario("sistema").build());
        }
        sincronizar(t.getTipo());
        return t;
    }

    /** Descuenta metros de un color (consumo de una OP o venta de retazo) y mantiene la suma del material. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public void descontar(Tela t, double metros) {
        Tela fresca = tela(t.getId());
        fresca.setStock(Math.max(0, fresca.getStock() - metros));
        telaRepository.save(fresca);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Usos usos(Tela t) {
        String nombre = t.getColor().toLowerCase(Locale.ROOT);
        List<Producto> productos = productoRepository.findAllByOrderByCodigoAsc().stream()
                .filter(p -> p.getColor() != null && p.getColor().toLowerCase(Locale.ROOT).equals(nombre)
                        && p.getMaterial() != null && t.getTipo().nombre().toLowerCase(Locale.ROOT).startsWith(p.getMaterial().toLowerCase(Locale.ROOT).split(" ")[0]))
                .toList();
        List<Pedido> pedidos = pedidoRepository.findAllByOrderByCodigoDesc().stream()
                .filter(p -> p.getTela() != null && p.getTela().getId().equals(t.getId())).toList();
        List<OrdenProduccion> ops = opRepository.findAllByOrderByCodigoAsc().stream()
                .filter(o -> o.getTela() != null && o.getTela().getId().equals(t.getId()) && !o.getAnulada()).toList();
        return new Usos(productos, pedidos, ops);
    }

    /** Disponibilidad textual para el configurador. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public String notaDisponibilidad(Tela t, double metrosNecesarios) {
        if (t.getDescontinuada()) return "Color descontinuado";
        if (t.getStock() <= 0) return "Agotado · reposición en 7 días";
        if (t.getStock() < metrosNecesarios) return "Solo " + InventarioService.fmt(t.getStock()) + " m · faltan " + InventarioService.fmt(metrosNecesarios - t.getStock()) + " m";
        return InventarioService.fmt(t.getStock()) + " m disponibles";
    }
}
