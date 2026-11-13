package pe.erp.sofas.service.calculo;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Tipo de plano de corte y su formato de material. */
@EquipoPLMD_MGML_AMCS_DRJB
public enum TipoCorte {
    @EquipoPLMD_MGML_AMCS_DRJB
    ESPUMA("Espuma", 200, 100, "plancha 200 × 100 cm"),
    @EquipoPLMD_MGML_AMCS_DRJB
    TELA("Tela", 140, 0, "rollo de 140 cm de ancho"),
    @EquipoPLMD_MGML_AMCS_DRJB
    MADERA("Madera", 300, 0, "tabla de 300 cm · kerf 5 mm"),
    @EquipoPLMD_MGML_AMCS_DRJB
    TABLERO("Tablero", 244, 122, "plancha de triplay 244 × 122 cm");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final double ancho;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final double alto;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String formato;

    @EquipoPLMD_MGML_AMCS_DRJB
    TipoCorte(String nombre, double ancho, double alto, String formato) {
        this.nombre = nombre;
        this.ancho = ancho;
        this.alto = alto;
        this.formato = formato;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double ancho() {
        return ancho;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double alto() {
        return alto;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String formato() {
        return formato;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static TipoCorte paraCategoria(String categoriaMaterial, String codigo) {
        if (pe.erp.sofas.model.TipoTela.porMaterial(codigo) != null) return TELA;
        return switch (categoriaMaterial) {
            case "Espuma" -> ESPUMA;
            case "Madera" -> MADERA;
            case "Tableros" -> TABLERO;
            default -> null;
        };
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
