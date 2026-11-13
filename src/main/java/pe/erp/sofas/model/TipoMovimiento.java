package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/** Tipos de movimiento del kardex. Reserva, Liberacion y Retazo no alteran el saldo fisico. */
@EquipoPLMD_MGML_AMCS_DRJB
public enum TipoMovimiento {
    @EquipoPLMD_MGML_AMCS_DRJB
    ENTRADA("Entrada", "MOVE_TO_INBOX", "green", 1),
    @EquipoPLMD_MGML_AMCS_DRJB
    SALIDA("Salida", "UNARCHIVE", "amber", -1),
    @EquipoPLMD_MGML_AMCS_DRJB
    AJUSTE("Ajuste", "TUNE", "blue", 0),
    @EquipoPLMD_MGML_AMCS_DRJB
    RESERVA("Reserva", "LOCK", "blue", 0),
    @EquipoPLMD_MGML_AMCS_DRJB
    LIBERACION("Liberación", "LOCK_OPEN", "gray", 0),
    @EquipoPLMD_MGML_AMCS_DRJB
    CONSUMO_OP("Consumo OP", "BUILD", "red", -1),
    @EquipoPLMD_MGML_AMCS_DRJB
    RETAZO("Retazo", "CONTENT_CUT", "wood", 0);

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String icono;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String variante;
    /** +1 suma, -1 resta, 0 no cambia el saldo (el ajuste usa el signo de la cantidad). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private final int signo;

    @EquipoPLMD_MGML_AMCS_DRJB
    TipoMovimiento(String nombre, String icono, String variante, int signo) {
        this.nombre = nombre;
        this.icono = icono;
        this.variante = variante;
        this.signo = signo;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String icono() {
        return icono;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String variante() {
        return variante;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public int signo() {
        return signo;
    }

    /** true si el movimiento cambia el stock fisico. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean afectaSaldo() {
        return this == ENTRADA || this == SALIDA || this == AJUSTE || this == CONSUMO_OP;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
