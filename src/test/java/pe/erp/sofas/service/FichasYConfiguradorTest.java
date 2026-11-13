package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pe.erp.sofas.model.*;
import pe.erp.sofas.service.calculo.Explosion;
import pe.erp.sofas.service.calculo.Formula;
import pe.erp.sofas.service.calculo.Opciones;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@EquipoPLMD_MGML_AMCS_DRJB
@IntegracionTest
class FichasYConfiguradorTest {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired FichaService fichaService;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired ConfiguradorService configurador;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Autowired TelaService telaService;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void juegoCompuestoSumaLasHijasAprobadas() {
        FichaTecnica juego = fichaService.aprobada("FT-004");
        assertThat(juego.getCompuesta()).isTrue();
        Explosion.Resultado r = fichaService.explotar(juego, 1, Opciones.NINGUNA);
        double esperado = 0, mo = 0;
        Map<String, Double> porMaterial = new HashMap<>();
        for (String hijo : new String[]{"FT-001", "FT-002", "FT-003"}) {
            Explosion.Resultado h = fichaService.explotar(fichaService.aprobada(hijo));
            esperado += h.costoMateriales();
            mo += h.costoMO();
            for (Explosion.Linea l : h.lineas()) porMaterial.merge(l.material().getCodigo(), l.bruta(), Double::sum);
        }
        assertThat(r.costoMateriales()).isCloseTo(esperado, within(1e-6));
        assertThat(r.costoMO()).isCloseTo(mo, within(1e-6));
        // Consolidado por material: una sola fila por material con la suma de las hijas
        for (Explosion.PorMaterial pm : r.porMaterial()) {
            double suma = porMaterial.get(pm.material().getCodigo());
            assertThat(pm.cantidad()).isCloseTo(pm.material().isEntera() ? Math.ceil(suma - 1e-9) : suma, within(1e-6));
        }
        assertThat(r.porMaterial().stream().map(p -> p.material().getCodigo()).distinct().count()).isEqualTo(r.porMaterial().size());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void misma_tela_para_todo_el_juego_por_sustitucion() {
        FichaTecnica juego = fichaService.aprobada("FT-004");
        Explosion.Resultado r = fichaService.explotar(juego, 1, Opciones.sustituir(Map.of("MP-003", "MP-007")));
        assertThat(r.porMaterial()).noneMatch(p -> p.material().getCodigo().equals("MP-003"));
        assertThat(r.porMaterial()).anyMatch(p -> p.material().getCodigo().equals("MP-007"));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void precioDelConfiguradorSofa3CuerposEnChenille() {
        ConfigSofa c = configurador.defecto(ConfigSofa.Modelo.MODERNO);
        assertThat(c.cuerpos()).isEqualTo(3);
        assertThat(telaService.tela(c.telaId()).getTipo()).isEqualTo(TipoTela.CHENILLE);
        ConfiguradorService.Cotizacion cot = configurador.cotizar(c);
        assertThat(cot.referencia()).isEqualTo("ficha FT-001 v3");
        // precio = costo × 1.55 redondeado a la decena
        assertThat(cot.precio()).isEqualTo(Formula.redondearDecena(cot.costo() * 1.55));
        assertThat(cot.precio() % 10).isZero();
        // costo = materiales (ficha + 2 cojines decorativos) + MO + 6 % de indirectos
        assertThat(cot.costo()).isCloseTo((cot.costoMateriales() + cot.costoMO()) * 1.06, within(1e-6));
        // Sin cojines baja el precio; con chaise sube (+25 % de mano de obra y materiales de la chaise)
        double sinCojines = configurador.cotizar(c.conCojines(0, null)).precio();
        double conChaise = configurador.cotizar(c.conChaise(ConfigSofa.Chaise.DERECHA)).costo();
        assertThat(sinCojines).isLessThan(cot.precio());
        assertThat(conChaise).isGreaterThan(cot.costo());
        // Dias = horas MO / 8 + 3 (+5 si hay material critico)
        assertThat(cot.dias()).isEqualTo((int) Math.ceil(cot.horasMO() / 8 + 3) + (cot.critico() ? 5 : 0) + (cot.bajoPedido() ? 4 : 0));
        // Elegir algo bajo pedido suma 4 dias
        ConfiguradorService.Cotizacion visco = configurador.cotizar(c.conEspuma(ConfigSofa.Espuma.VISCO));
        assertThat(visco.bajoPedido()).isTrue();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Test
    void editarUnaFichaAprobadaCreaLaSiguienteVersionEnBorrador() {
        FichaTecnica v3 = fichaService.aprobada("FT-001");
        double original = v3.getLineas().get(0).getCantidad();
        FichaService.Edicion e = fichaService.editarLinea(v3.getId(), 0, l -> l.setCantidad(40d));
        assertThat(e.nuevaVersion()).isTrue();
        assertThat(e.ficha().getVersion()).isEqualTo(4);
        assertThat(e.ficha().getEstado()).isEqualTo(EstadoFicha.BORRADOR);
        assertThat(fichaService.ficha(v3.getId()).getLineas().get(0).getCantidad()).isEqualTo(original);
        // Aprobar la v4 deja obsoleta la v3
        fichaService.aprobar(e.ficha().getId());
        assertThat(fichaService.ficha(v3.getId()).getEstado()).isEqualTo(EstadoFicha.OBSOLETA);
        assertThat(fichaService.aprobada("FT-001").getVersion()).isEqualTo(4);
    }
}
