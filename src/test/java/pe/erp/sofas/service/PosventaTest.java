package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pe.erp.sofas.model.*;
import pe.erp.sofas.service.calculo.Formula;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@EquipoPLMD_MGML_AMCS_DRJB
@IntegracionTest
class PosventaTest {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired RetapizadoService retapizado;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired GarantiaService garantias;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired TelaService telas;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired ProduccionService produccion;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void cotizacionDeRetapizado() {
        Tela lino = telas.porCodigo("TL-10").orElseThrow();
        RetapizadoService.Cotizacion buena = retapizado.cotizar(2, 160, 88, 82, "Buena", "Buena", lino);
        RetapizadoService.Cotizacion hundida = retapizado.cotizar(2, 160, 88, 82, "Buena", "Hundida", lino);
        RetapizadoService.Cotizacion total = retapizado.cotizar(2, 160, 88, 82, "Requiere refuerzo", "Cambio total", lino);
        // precio = costo × 1.06 × 1.45 a la decena
        assertThat(buena.precio()).isEqualTo(Formula.redondearDecena(buena.costo() * 1.06 * 1.45));
        assertThat(buena.ficha().getCodigo()).isEqualTo("FT-002");
        // Espuma ×0 / 0.5 / 1 y madera ×0 / 0.1 / 0.3
        assertThat(buena.lineas()).noneMatch(l -> l.material().getCategoria().equals("Espuma"));
        double espHundida = hundida.lineas().stream().filter(l -> l.material().getCategoria().equals("Espuma")).mapToDouble(RetapizadoService.LineaCot::costo).sum();
        double espTotal = total.lineas().stream().filter(l -> l.material().getCategoria().equals("Espuma")).mapToDouble(RetapizadoService.LineaCot::costo).sum();
        assertThat(espTotal).isCloseTo(espHundida * 2, within(0.5));
        assertThat(total.lineas()).anyMatch(l -> l.material().getCategoria().equals("Madera"));
        // Mano de obra ((tapizado + costura) × 1.2 + acabado) a S/ 9 por hora
        assertThat(buena.costoMO()).isCloseTo(buena.horas() * 9, within(1e-6));
        assertThat(total.precio()).isGreaterThan(hundida.precio());
        assertThat(hundida.precio()).isGreaterThan(buena.precio());
        // Aviso si la tela no alcanza (lino Piedra tiene 9 m; un seccional de 3 cuerpos necesita mas)
        assertThat(retapizado.cotizar(3, 280, 95, 85, "Buena", "Buena", lino).aviso()).contains("no alcanza");
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void enTallerCreaLaOPDeRetapizadoEnCorteDeTela() {
        Retapizado r = retapizado.listar().stream().filter(x -> x.getEstado() == EstadoRetapizado.RECOJO_PROGRAMADO).findFirst().orElseThrow();
        retapizado.avanzar(r.getId());
        Retapizado despues = retapizado.retapizado(r.getId());
        assertThat(despues.getEstado()).isEqualTo(EstadoRetapizado.EN_TALLER);
        OrdenProduccion op = produccion.porCodigo(despues.getOpCodigo()).orElseThrow();
        assertThat(op.getEtapa()).isEqualTo(Etapa.CORTE_TELA);
        assertThat(op.getProductoNombre()).startsWith("Retapizado · ");
        assertThat(op.getTela().getId()).isEqualTo(r.getTela().getId());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void reclamosConCoberturaYSla() {
        ReclamoGarantia gr013 = garantias.listar().stream().filter(x -> x.getCodigo().equals("GR-013")).findFirst().orElseThrow();
        assertThat(garantias.cobertura(gr013).cubierto()).isFalse();
        ReclamoGarantia gr015 = garantias.listar().stream().filter(x -> x.getCodigo().equals("GR-015")).findFirst().orElseThrow();
        assertThat(garantias.cobertura(gr015).cubierto()).isTrue();
        assertThat(garantias.slaVencido(gr015)).isTrue();
        garantias.actualizar(gr015.getId(), "Cambio de mecanismo", "Reemplazo del mecanismo", java.math.BigDecimal.valueOf(180), EstadoReclamo.RESUELTO);
        assertThat(garantias.reclamo(gr015.getId()).getFechaCierre()).isEqualTo(pe.erp.sofas.core.AppClock.hoy());
    }
}
