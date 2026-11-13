package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.erp.sofas.model.*;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Buscador global (Ctrl+K): sofas, telas, clientes, pedidos, ordenes y materiales. */
@EquipoPLMD_MGML_AMCS_DRJB
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusquedaService {

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Resultado(@EquipoPLMD_MGML_AMCS_DRJB
      String grupo, @EquipoPLMD_MGML_AMCS_DRJB
        String icono, @EquipoPLMD_MGML_AMCS_DRJB
          String titulo, @EquipoPLMD_MGML_AMCS_DRJB
            String subtitulo, @EquipoPLMD_MGML_AMCS_DRJB
              String modulo, @EquipoPLMD_MGML_AMCS_DRJB
                Object dato) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static final int MAX_POR_GRUPO = 4;

    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProductoService productoService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final TelaService telaService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ClienteService clienteService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final PedidoService pedidoService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final ProduccionService produccionService;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final InventarioService inventarioService;

    @EquipoPLMD_MGML_AMCS_DRJB
    public List<Resultado> buscar(String texto) {
        String q = normalizar(texto);
        List<Resultado> r = new ArrayList<>();
        if (q.length() < 2) return r;
        int n = 0;
        for (Producto p : productoService.listar()) {
            if (n < MAX_POR_GRUPO && coincide(q, p.getCodigo(), p.getNombre(), p.getCategoria(), p.getColor())) {
                r.add(new Resultado("Sofás", "WEEKEND", p.getNombre(), p.getCodigo() + " · " + p.getMedidas(), "modelos", p.getCodigo()));
                n++;
            }
        }
        n = 0;
        for (Tela t : telaService.telas()) {
            if (n < MAX_POR_GRUPO && coincide(q, t.getCodigo(), t.getColor(), t.getTipo().nombre(), t.getCodigoProveedor())) {
                r.add(new Resultado("Telas", "PALETTE", t.getNombre(), t.getCodigo() + " · " + InventarioService.fmt(t.getStock()) + " m · " + t.getEstado(), "telas", t.getCodigo()));
                n++;
            }
        }
        n = 0;
        for (Cliente c : clienteService.listar()) {
            if (n < MAX_POR_GRUPO && coincide(q, c.getCodigo(), c.getNombre(), c.getDocumento(), c.getTelefono())) {
                r.add(new Resultado("Clientes", "PERSON", c.getNombre(), c.getCodigo() + " · " + c.getTipoDocumento() + " " + c.getDocumento(), "clientes", c.getCodigo()));
                n++;
            }
        }
        n = 0;
        for (Pedido p : pedidoService.listar()) {
            if (n < MAX_POR_GRUPO && coincide(q, p.getCodigo(), p.getCliente().getNombre(), p.getProductoNombre())) {
                r.add(new Resultado("Pedidos", "ASSIGNMENT", p.getCodigo() + " · " + p.getProductoNombre(), p.getCliente().getNombre() + " · " + p.getEstado().nombre(), "pedidos", p.getCodigo()));
                n++;
            }
        }
        n = 0;
        for (OrdenProduccion o : produccionService.ordenes()) {
            if (n < MAX_POR_GRUPO && coincide(q, o.getCodigo(), o.getProductoNombre(), o.getClienteNombre(), o.getTelaNombre())) {
                r.add(new Resultado("Órdenes", "BUILD", o.getCodigo() + " · " + o.getProductoNombre(), o.getClienteNombre() + " · " + o.getEtapa().nombre(), "produccion", o.getCodigo()));
                n++;
            }
        }
        n = 0;
        for (Material m : inventarioService.materiales()) {
            if (n < MAX_POR_GRUPO && coincide(q, m.getCodigo(), m.getNombre(), m.getCategoria())) {
                r.add(new Resultado("Materiales", "ALL_INBOX", m.getNombre(), m.getCodigo() + " · " + InventarioService.fmt(m.getStock()) + " " + m.getUnidad() + " · " + m.getEstado(), "inventario", m.getCodigo()));
                n++;
            }
        }
        return r;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static boolean coincide(String q, String... campos) {
        for (String c : campos) if (c != null && normalizar(c).contains(q)) return true;
        return false;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String normalizar(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT).trim();
    }
}
