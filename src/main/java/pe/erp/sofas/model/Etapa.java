package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Las 11 etapas del taller de sofas, en orden. El avance de una orden es indice / 10. */
@EquipoPLMD_MGML_AMCS_DRJB
public enum Etapa {
    @EquipoPLMD_MGML_AMCS_DRJB
    PENDIENTE("Pendiente", "#94A3B8", "SCHEDULE"),
    @EquipoPLMD_MGML_AMCS_DRJB
    CORTE_MADERA("Corte de madera", "#C69A6D", "CARPENTER"),
    @EquipoPLMD_MGML_AMCS_DRJB
    ARMADO("Armado de estructura", "#A47148", "HANDYMAN"),
    @EquipoPLMD_MGML_AMCS_DRJB
    CINCHADO("Cinchado / resortes", "#8B5CF6", "GRID_ON"),
    @EquipoPLMD_MGML_AMCS_DRJB
    ESPUMADO("Espumado", "#0EA5E9", "LAYERS"),
    @EquipoPLMD_MGML_AMCS_DRJB
    CORTE_TELA("Corte de tela", "#EC4899", "CONTENT_CUT"),
    @EquipoPLMD_MGML_AMCS_DRJB
    COSTURA("Costura", "#F59E0B", "GESTURE"),
    @EquipoPLMD_MGML_AMCS_DRJB
    TAPIZADO("Tapizado", "#2563EB", "WEEKEND"),
    @EquipoPLMD_MGML_AMCS_DRJB
    ACABADO("Acabado y patas", "#14B8A6", "BRUSH"),
    @EquipoPLMD_MGML_AMCS_DRJB
    CONTROL_CALIDAD("Control de calidad", "#64748B", "FACT_CHECK"),
    @EquipoPLMD_MGML_AMCS_DRJB
    LISTO("Listo", "#16A34A", "CHECK_CIRCLE");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String color;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String icono;

    @EquipoPLMD_MGML_AMCS_DRJB
    Etapa(String nombre, String color, String icono) {
        this.nombre = nombre;
        this.color = color;
        this.icono = icono;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String color() {
        return color;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String icono() {
        return icono;
    }

    /** 0..10 */
    @EquipoPLMD_MGML_AMCS_DRJB
    public int paso() {
        return ordinal();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double avance() {
        return ordinal() / 10.0;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean productiva() {
        return this != PENDIENTE && this != LISTO;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Etapa siguiente() {
        return this == LISTO ? LISTO : values()[ordinal() + 1];
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Etapa anterior() {
        return this == PENDIENTE ? PENDIENTE : values()[ordinal() - 1];
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
