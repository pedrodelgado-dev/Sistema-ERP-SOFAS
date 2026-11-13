package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import java.util.Arrays;

/** Modulos del ERP con su seccion del menu, icono, descripcion y vista FXML. */
@EquipoPLMD_MGML_AMCS_DRJB
public enum Modulo {
    @EquipoPLMD_MGML_AMCS_DRJB
    DASHBOARD("dashboard", "Dashboard", null, "DASHBOARD",
            "Resumen de ventas, producción y pedidos de SISTEMA ERP SOFAS al día de hoy."),
    @EquipoPLMD_MGML_AMCS_DRJB
    MODELOS("modelos", "Modelos de sofá", "CATÁLOGO", "WEEKEND",
            "Catálogo de modelos con medidas, precios, costo estándar y stock."),
    @EquipoPLMD_MGML_AMCS_DRJB
    TELAS("telas", "Telas y colores", "CATÁLOGO", "PALETTE",
            "Muestrario de telas por tipo, colores, metros en stock y calculadora de tela."),
    @EquipoPLMD_MGML_AMCS_DRJB
    CONFIGURADOR("configurador", "Configurador 3D", "CATÁLOGO", "MDOAL_3D_ROTATION",
            "Diseña el sofá del cliente en 3D con precio y plazo en tiempo real."),
    @EquipoPLMD_MGML_AMCS_DRJB
    COMPARADOR("comparador", "Comparar sofás", "CATÁLOGO", "VIEW_DAY",
            "Compara hasta 3 configuraciones lado a lado con rotación sincronizada."),
    @EquipoPLMD_MGML_AMCS_DRJB
    CONSTRUCTOR("constructor", "Constructor modular", "CATÁLOGO", "BUILD",
            "Arma sofás pieza por pieza desde paleta de módulos."),
    @EquipoPLMD_MGML_AMCS_DRJB
    SALA("sala", "Ver en mi sala", null, "MEETING_ROOM",
            "Ubica los sofás en la planta de la sala del cliente y revisa la circulación."),
    @EquipoPLMD_MGML_AMCS_DRJB
    PRODUCCION("produccion", "Órdenes de producción", "PRODUCCIÓN", "BUILD",
            "Tablero del taller por etapa con reservas y consumo de materiales."),
    @EquipoPLMD_MGML_AMCS_DRJB
    FICHAS("fichas", "Fichas técnicas", "PRODUCCIÓN", "DESCRIPTION",
            "Lista de materiales, mano de obra y costo estándar por versión."),
    @EquipoPLMD_MGML_AMCS_DRJB
    MRP("mrp", "Requerimiento de materiales", "PRODUCCIÓN", "FACT_CHECK",
            "Materiales requeridos por las órdenes y pedidos, faltantes y compras en tránsito."),
    @EquipoPLMD_MGML_AMCS_DRJB
    CORTE("corte", "Hojas de corte", "PRODUCCIÓN", "CONTENT_CUT",
            "Planos de corte optimizados de espuma, tela, madera y tableros."),
    @EquipoPLMD_MGML_AMCS_DRJB
    CONSUMO("consumo", "Consumo y mermas", "PRODUCCIÓN", "ANALYTICS",
            "Consumo real frente al estimado por la ficha, desvíos y sobremerma."),
    @EquipoPLMD_MGML_AMCS_DRJB
    PEDIDOS("pedidos", "Pedidos a medida", "COMERCIAL", "ASSIGNMENT",
            "Pedidos estándar y personalizados, adelantos, órdenes y entregas."),
    @EquipoPLMD_MGML_AMCS_DRJB
    CARRITO("carrito", "Carrito de compras", "COMERCIAL", "SHOPPING_CART",
            "Carrito con configuraciones guardadas, cálculo de totales y generación de órdenes."),
    @EquipoPLMD_MGML_AMCS_DRJB
    VENTAS("ventas", "Ventas (POS)", "COMERCIAL", "POINT_OF_SALE",
            "Punto de venta de la tienda con boleta o factura de prueba."),
    @EquipoPLMD_MGML_AMCS_DRJB
    CLIENTES("clientes", "Clientes", "COMERCIAL", "PEOPLE",
            "Cartera de clientes, compras, pedidos y saldos."),
    @EquipoPLMD_MGML_AMCS_DRJB
    POSVENTA("posventa", "Retapizado y garantías", "COMERCIAL", "BUILD_CIRCLE",
            "Retapizado de sofás de clientes y reclamos de garantía."),
    @EquipoPLMD_MGML_AMCS_DRJB
    INVENTARIO("inventario", "Inventario", "ADMINISTRACIÓN", "ALL_INBOX",
            "Materiales del taller, reservas, kardex y alertas de stock."),
    @EquipoPLMD_MGML_AMCS_DRJB
    PROVEEDORES("proveedores", "Proveedores", "ADMINISTRACIÓN", "LOCAL_SHIPPING",
            "Empresas que abastecen madera, espumas, telas e insumos."),
    @EquipoPLMD_MGML_AMCS_DRJB
    COMPRAS("compras", "Compras", "ADMINISTRACIÓN", "SHOPPING_CART",
            "Órdenes de compra y recepción en almacén."),
    @EquipoPLMD_MGML_AMCS_DRJB
    PERSONAL("personal", "Personal", "ADMINISTRACIÓN", "ENGINEERING",
            "Equipo del taller y la tienda, productividad y pago a destajo."),
    @EquipoPLMD_MGML_AMCS_DRJB
    FINANZAS("finanzas", "Finanzas", "ADMINISTRACIÓN", "ACCOUNT_BALANCE_WALLET",
            "Ingresos, egresos, cuentas por cobrar y por pagar y rentabilidad."),
    @EquipoPLMD_MGML_AMCS_DRJB
    REPORTES("reportes", "Reportes", "ADMINISTRACIÓN", "INSERT_CHART",
            "Reportes con vista previa y exportación a PDF y Excel."),
    @EquipoPLMD_MGML_AMCS_DRJB
    CONFIGURACION("configuracion", "Configuración", "SISTEMA", "SETTINGS",
            "Empresa, usuarios y roles, parámetros de producción y preferencias.");

    @EquipoPLMD_MGML_AMCS_DRJB
    private final String clave;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String titulo;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String seccion;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String icono;
    @EquipoPLMD_MGML_AMCS_DRJB
    private final String descripcion;

    @EquipoPLMD_MGML_AMCS_DRJB
    Modulo(String clave, String titulo, String seccion, String icono, String descripcion) {
        this.clave = clave;
        this.titulo = titulo;
        this.seccion = seccion;
        this.icono = icono;
        this.descripcion = descripcion;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String clave() {
        return clave;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String titulo() {
        return titulo;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String seccion() {
        return seccion;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String icono() {
        return icono;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String descripcion() {
        return descripcion;
    }

    /** "Ver en mi sala" no aparece en el menu: se abre desde el Configurador. Comparador y Constructor tampoco aparecen. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean enMenu() {
        return this != SALA && this != COMPARADOR && this != CONSTRUCTOR;
    }

    /** Las 5 vistas de Produccion comparten la barra de pestanas. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean esProduccion() {
        return this == PRODUCCION || this == FICHAS || this == MRP || this == CORTE || this == CONSUMO;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String fxml() {
        return "/fxml/" + clave + ".fxml";
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Modulo porClave(String clave) {
        return Arrays.stream(values()).filter(m -> m.clave.equals(clave)).findFirst().orElse(DASHBOARD);
    }
}
