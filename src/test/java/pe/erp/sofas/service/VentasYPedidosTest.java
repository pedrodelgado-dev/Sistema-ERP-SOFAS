package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.model.*;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@EquipoPLMD_MGML_AMCS_DRJB
@IntegracionTest
class VentasYPedidosTest {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired VentaService ventas;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired ProductoService productos;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired ClienteService clientes;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired PedidoService pedidos;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired ConfiguradorService configurador;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired ProduccionService produccion;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void cobrarDescuentaStockYNumeraLaBoleta() {
        Producto p = productos.porCodigo("SF-001").orElseThrow();
        int stock = p.getStock();
        int numero = ventas.siguienteNumero(Venta.Comprobante.BOLETA);
        Venta v = ventas.cobrar(new VentaService.Cobro(null, Venta.Comprobante.BOLETA,
                List.of(new VentaService.Linea(p.getId(), 1, null)), BigDecimal.ZERO, "Efectivo", new BigDecimal("2500"), null, null, null));
        assertThat(v.getSerie()).isEqualTo("B001");
        assertThat(v.getNumero()).isEqualTo(numero);
        assertThat(v.getVuelto()).isEqualByComparingTo("10.00");
        assertThat(productos.porCodigo("SF-001").orElseThrow().getStock()).isEqualTo(stock - 1);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void facturaExigeRucYMixtoDebeSumarElTotal() {
        Producto p = productos.porCodigo("SF-010").orElseThrow();
        Cliente persona = clientes.listar().stream().filter(c -> !c.tieneRuc()).findFirst().orElseThrow();
        assertThatThrownBy(() -> ventas.cobrar(new VentaService.Cobro(persona.getId(), Venta.Comprobante.FACTURA,
                List.of(new VentaService.Linea(p.getId(), 1, null)), null, "Tarjeta", null, null, null, null)))
                .isInstanceOf(NegocioException.class).hasMessageContaining("RUC");
        assertThatThrownBy(() -> ventas.cobrar(new VentaService.Cobro(null, Venta.Comprobante.BOLETA,
                List.of(new VentaService.Linea(p.getId(), 1, null)), null, "Mixto", null, "Yape", new BigDecimal("100"), new BigDecimal("100"))))
                .isInstanceOf(NegocioException.class);
        Venta ok = ventas.cobrar(new VentaService.Cobro(null, Venta.Comprobante.BOLETA,
                List.of(new VentaService.Linea(p.getId(), 1, null)), null, "Mixto", null, "Yape", new BigDecimal("160"), new BigDecimal("100")));
        assertThat(ok.getMetodoPago()).startsWith("Mixto");
        Producto sinStock = productos.porCodigo("SF-007").orElseThrow();
        assertThatThrownBy(() -> ventas.cobrar(new VentaService.Cobro(null, Venta.Comprobante.BOLETA,
                List.of(new VentaService.Linea(sinStock.getId(), 1, null)), null, "Tarjeta", null, null, null, null)))
                .isInstanceOf(NegocioException.class).hasMessageContaining("Stock insuficiente");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void pedidoNecesitaElCincuentaPorCientoParaConfirmarYGenerarOP() {
        Pedido p = pedidos.listar().stream().filter(x -> x.getCodigo().equals("PE-0249")).findFirst().orElseThrow();
        assertThatThrownBy(() -> pedidos.avanzarA(p.getId(), EstadoPedido.CONFIRMADO)).isInstanceOf(NegocioException.class);
        assertThatThrownBy(() -> pedidos.generarOP(p.getId(), null)).isInstanceOf(NegocioException.class);
        Pedido conAdelanto = pedidos.registrarAdelanto(p.getId(), p.getPrecio().multiply(new BigDecimal("0.5")), "Yape");
        assertThat(conAdelanto.getEstado()).isEqualTo(EstadoPedido.CONFIRMADO);
        OrdenProduccion op = pedidos.generarOP(p.getId(), null);
        assertThat(op.getFichaUid()).isEqualTo("FT-009v1");
        assertThat(pedidos.pedido(p.getId()).getEstado()).isEqualTo(EstadoPedido.EN_PRODUCCION);
        assertThat(pedidos.pedido(p.getId()).getOpCodigo()).isEqualTo(op.getCodigo());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void crearPedidoDesdeElConfiguradorConSuOP() {
        Cliente c = clientes.listar().get(0);
        ConfigSofa cfg = configurador.defecto(ConfigSofa.Modelo.CHESTERFIELD);
        ConfiguradorService.Cotizacion cot = configurador.cotizar(cfg);
        Pedido p = configurador.crearPedido(cfg, c.getId(), null, BigDecimal.valueOf(cot.precio() / 2), "Efectivo", null, true, null);
        assertThat(p.getTipo()).isEqualTo(Pedido.Tipo.PERSONALIZADO);
        assertThat(p.getPrecio()).isEqualByComparingTo(BigDecimal.valueOf(cot.precio()));
        assertThat(p.getOpCodigo()).isNotNull();
        OrdenProduccion op = produccion.porCodigo(p.getOpCodigo()).orElseThrow();
        assertThat(op.getFichaUid()).isEqualTo("FT-001v3");
        assertThat(op.getExtras()).anyMatch(x -> x.getMaterialCodigo().equals("MP-016") && x.getCantidad() == 48);
        assertThat(op.getSustituciones()).containsEntry("MP-003", "MP-004");
        assertThat(ConfigSofa.leer(p.getConfiguracion())).isEqualTo(cfg);
    }
}
