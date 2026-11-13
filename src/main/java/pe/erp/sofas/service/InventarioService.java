package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.core.AppClock;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.*;
import pe.erp.sofas.repository.MaterialRepository;
import pe.erp.sofas.repository.MovimientoRepository;
import pe.erp.sofas.repository.TelaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/** Materiales del almacen y kardex. Reserva, Liberacion y Retazo no alteran el saldo fisico. */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventarioService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final List<String> UNIDADES = List.of("pt", "plancha", "m", "unidad", "caja", "galón", "kg", "juego", "par");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final MaterialRepository materialRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final MovimientoRepository movimientoRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final TelaRepository telaRepository;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final UsuarioActual usuario;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Material> materiales() {
        return materialRepository.findAllByOrderByCodigoAsc();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Material material(Long id) {
        return materialRepository.findById(id).orElseThrow(() -> new NegocioException("El material ya no existe."));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Material material(String codigo) {
        return materialRepository.findByCodigo(codigo).orElseThrow(() -> new NegocioException("No existe el material " + codigo + "."));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Movimiento> movimientos() {
        return movimientoRepository.findAllByOrderByFechaDescIdDesc();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Movimiento> kardex(Long materialId) {
        return movimientoRepository.findByMaterialIdOrderByFechaAscIdAsc(materialId);
    }

    /** Materiales bajo el minimo (por disponible), del mas urgente al menos urgente. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Material> bajoMinimo() {
        return materiales().stream().filter(Material::isBajoMinimo)
                .sorted(Comparator.comparingDouble(m -> m.getDisponible() / Math.max(0.01, m.getMinimo())))
                .toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Material> criticos() {
        return materiales().stream().filter(Material::isCritico).toList();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double valorTotal() {
        return materiales().stream().mapToDouble(Material::getValor).sum();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public long movimientosDelMes() {
        LocalDate inicio = AppClock.hoy().withDayOfMonth(1);
        return movimientoRepository.findAll().stream().filter(m -> !m.getFecha().toLocalDate().isBefore(inicio)).count();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String siguienteCodigo() {
        return Codigos.siguiente(materialRepository.findAll(), Material::getCodigo, "MP-", 3);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Material guardarMaterial(Material material) {
        if (material.getNombre() == null || material.getNombre().isBlank()) throw new NegocioException("El nombre del material es obligatorio.");
        if (material.getCosto() == null || material.getCosto() < 0) throw new NegocioException("Ingresa un costo unitario válido.");
        if (material.getStock() < 0 || material.getMinimo() < 0) throw new NegocioException("Las cantidades no pueden ser negativas.");
        boolean nuevo = material.getId() == null;
        if (nuevo) material.setCodigo(siguienteCodigo());
        double inicial = material.getStock();
        if (nuevo) material.setStock(0d);
        Material g = materialRepository.save(material);
        if (nuevo && inicial > 0) registrar(g, TipoMovimiento.ENTRADA, inicial, "INI-" + g.getCodigo(), "Inventario inicial");
        return g;
    }

    /**
     * Entrada, salida o ajuste manual. En las telas los metros se aplican a un color del muestrario
     * (el stock del material es siempre la suma de sus colores).
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Movimiento movimientoManual(Long materialId, TipoMovimiento tipo, double cantidad, Long telaId,
                                       String referencia, String motivo) {
        if (tipo != TipoMovimiento.ENTRADA && tipo != TipoMovimiento.SALIDA && tipo != TipoMovimiento.AJUSTE) {
            throw new NegocioException("Movimiento no permitido.");
        }
        if (tipo == TipoMovimiento.AJUSTE ? cantidad == 0 : cantidad <= 0) throw new NegocioException("La cantidad debe ser mayor a cero.");
        Material m = material(materialId);
        if (m.isTela()) {
            if (telaId == null) throw new NegocioException("Elige el color de la tela.");
            Tela t = telaRepository.findById(telaId).orElseThrow();
            double efecto = tipo == TipoMovimiento.SALIDA ? -cantidad : cantidad;
            if (t.getStock() + efecto < -1e-9) throw new NegocioException("El color " + t.getColor() + " solo tiene " + fmt(t.getStock()) + " m.");
            t.setStock(t.getStock() + efecto);
            telaRepository.save(t);
            motivo = (motivo == null || motivo.isBlank() ? tipo.nombre() : motivo) + " · " + t.getColor() + " " + t.getCodigo();
        }
        return registrar(m, tipo, cantidad, referencia, motivo);
    }

    /**
     * Registra un movimiento y actualiza el saldo fisico. Una salida o consumo nunca deja el stock en negativo
     * ni por debajo de lo reservado por otras ordenes (salvo el propio consumo de la OP).
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Transactional
    public Movimiento registrar(Material material, TipoMovimiento tipo, double cantidad, String referencia, String concepto) {
        double saldo = material.getStock();
        double efecto = switch (tipo) {
            case ENTRADA -> cantidad;
            case SALIDA, CONSUMO_OP -> -cantidad;
            case AJUSTE -> cantidad;
            default -> 0;
        };
        if (saldo + efecto < -1e-9) {
            throw new NegocioException("Stock insuficiente: hay " + fmt(saldo) + " " + material.getUnidad() + " de " + material.getNombre() + ".");
        }
        if (tipo == TipoMovimiento.SALIDA && saldo + efecto < material.getReservado() - 1e-9) {
            throw new NegocioException("No se puede sacar material reservado para producción: disponible "
                    + fmt(material.getDisponible()) + " " + material.getUnidad() + ".");
        }
        material.setStock(Math.max(0, saldo + efecto));
        materialRepository.save(material);
        return movimientoRepository.save(Movimiento.builder()
                .material(material).fecha(LocalDateTime.of(AppClock.hoy(), AppClock.HORA)).tipo(tipo)
                .cantidad(cantidad).saldo(material.getStock())
                .referencia(referencia == null || referencia.isBlank() ? "—" : referencia.trim())
                .concepto(concepto == null || concepto.isBlank() ? tipo.nombre() : concepto.trim())
                .usuario(usuario.username()).build());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String fmt(double n) {
        return n == Math.floor(n) ? String.valueOf((long) n) : String.format(java.util.Locale.US, "%.2f", n);
    }
}
