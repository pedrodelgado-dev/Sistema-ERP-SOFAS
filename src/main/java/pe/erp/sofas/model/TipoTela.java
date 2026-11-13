package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

/**
 * Tipos de tela del muestrario con sus caracteristicas comunes a todos los colores
 * (precio por metro, recargo al cliente, ciclos Martindale y material de inventario vinculado).
 */
@EquipoPLMD_MGML_AMCS_DRJB
public enum TipoTela {
    @EquipoPLMD_MGML_AMCS_DRJB
    CHENILLE("Chenille", 28, 0, 30000, true, false, false, "MP-003", "PR-03", 8),
    @EquipoPLMD_MGML_AMCS_DRJB
    LINO("Lino", 34, 8, 20000, false, false, false, "MP-006", "PR-02", 8),
    @EquipoPLMD_MGML_AMCS_DRJB
    CUERO("Cuero sintético", 35, 12, 100000, true, true, true, "MP-004", "PR-03", 40),
    @EquipoPLMD_MGML_AMCS_DRJB
    TERCIOPELO("Terciopelo", 42, 15, 40000, false, false, false, "MP-007", "PR-02", 25),
    @EquipoPLMD_MGML_AMCS_DRJB
    BOUCLE("Bouclé", 48, 20, 35000, false, false, false, "MP-008", "PR-02", 8),
    @EquipoPLMD_MGML_AMCS_DRJB
    MICROFIBRA("Microfibra antimascotas", 32, 6, 60000, true, true, true, "MP-005", "PR-03", 8);

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String nombre;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final double costoMetro;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final double recargoPct;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final int martindale;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final boolean antimanchas;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final boolean mascotas;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final boolean impermeable;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String materialCodigo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String proveedorCodigo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final double brillo;

    @EquipoPLMD_MGML_AMCS_DRJB
    TipoTela(String nombre, double costoMetro, double recargoPct, int martindale, boolean antimanchas,
             boolean mascotas, boolean impermeable, String materialCodigo, String proveedorCodigo, double brillo) {
        this.nombre = nombre;
        this.costoMetro = costoMetro;
        this.recargoPct = recargoPct;
        this.martindale = martindale;
        this.antimanchas = antimanchas;
        this.mascotas = mascotas;
        this.impermeable = impermeable;
        this.materialCodigo = materialCodigo;
        this.proveedorCodigo = proveedorCodigo;
        this.brillo = brillo;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String nombre() {
        return nombre;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public double costoMetro() {
        return costoMetro;
    }

    /** Recargo al precio de venta del sofa, en %. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public double recargoPct() {
        return recargoPct;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public int martindale() {
        return martindale;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean antimanchas() {
        return antimanchas;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean mascotas() {
        return mascotas;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean impermeable() {
        return impermeable;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String materialCodigo() {
        return materialCodigo;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String proveedorCodigo() {
        return proveedorCodigo;
    }

    /** specularPower del material 3D (cuero 40, terciopelo 25, resto 8). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public double brillo() {
        return brillo;
    }

    /** El terciopelo no se puede girar en el plano de corte por el sentido del pelo. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean permiteGiro() {
        return this != TERCIOPELO;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static TipoTela porMaterial(String codigoMaterial) {
        for (TipoTela t : values()) {
            if (t.materialCodigo.equals(codigoMaterial)) return t;
        }
        return null;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
