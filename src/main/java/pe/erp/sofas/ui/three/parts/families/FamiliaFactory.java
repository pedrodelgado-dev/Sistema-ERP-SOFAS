package pe.erp.sofas.ui.three.parts.families;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.scene.paint.PhongMaterial;
import pe.erp.sofas.model.ConfigSofa;
import pe.erp.sofas.ui.three.parts.SofaPart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory para construir familias de sofás.
 * Dado una familia, configuración y materiales, retorna la lista de partes que componen el sofá.
 *
 * Patrón: Strategy + Factory. Cada familia tiene su propio constructor (ej: ModernoFamily).
 */
@EquipoPLMD_MGML_AMCS_DRJB
public class FamiliaFactory {

    @EquipoPLMD_MGML_AMCS_DRJB
    private final Map<SofaFamilia, FamiliaConstructor> constructores = new HashMap<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    public FamiliaFactory() {
        // Registrar cada familia con su constructor
        constructores.put(SofaFamilia.MODERNO, new ModernoFamily());
        constructores.put(SofaFamilia.SECCIONAL_L_CHAISE, new SeccionalLChaiseFamily());
        constructores.put(SofaFamilia.SECCIONAL_L_ESQUINERO, new SeccionalLEsquineroFamily());
        constructores.put(SofaFamilia.SECCIONAL_U, new SeccionalUFamily());
        constructores.put(SofaFamilia.MODULAR, new ModularFamily());
        constructores.put(SofaFamilia.CHESTERFIELD, new ChesterfieldFamily());
        constructores.put(SofaFamilia.ESCANDINAVO, new EscandinavoFamily());
        constructores.put(SofaFamilia.SOFA_CAMA, new SofaCamaFamily());
        constructores.put(SofaFamilia.RECLINABLE, new ReclinableFamily());
        constructores.put(SofaFamilia.RETRO, new RetroFamily());
        constructores.put(SofaFamilia.CLOUD, new CloudFamily());
        constructores.put(SofaFamilia.TUXEDO, new TuxedoFamily());
        constructores.put(SofaFamilia.SILLON, new SillonFamily());
    }

    /**
     * Construye la lista de partes para una familia.
     *
     * @param familia la familia (Moderno, L, etc)
     * @param config configuración del sofá (cuerpos, chaise, brazos, etc)
     * @param materialTela material de la tela principal
     * @param materialRespaldo material del respaldo (si es diferente)
     * @return List<SofaPart> ordenadas para ensamblar
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    public List<SofaPart> construir(SofaFamilia familia, ConfigSofa config,
                                   PhongMaterial materialTela, PhongMaterial materialRespaldo) {
        FamiliaConstructor constructor = constructores.get(familia);
        if (constructor == null) {
            throw new IllegalArgumentException("Familia no registrada: " + familia);
        }
        return constructor.construir(config, materialTela, materialRespaldo);
    }

    /**
     * Interface que deben implementar todas las familias.
     */
    @EquipoPLMD_MGML_AMCS_DRJB
    public interface FamiliaConstructor {
        /**
         * Construye la lista de partes para esta familia.
         */
        @EquipoPLMD_MGML_AMCS_DRJB
        List<SofaPart> construir(ConfigSofa config, PhongMaterial materialTela, PhongMaterial materialRespaldo);
    }
}
