package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import java.util.prefs.Preferences;

/** Preferencias locales del usuario (java.util.prefs). */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Prefs {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String TEMA_OSCURO = "temaOscuro";
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String DENSIDAD = "densidad";
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String MENU_CONTRAIDO = "menuContraido";
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String RECORDAR = "recordarSesion";
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String ULTIMO_USUARIO = "ultimoUsuario";
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String VISTA_MODELOS = "vistaModelos";
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String VISTA_TABLERO = "vistaTablero";

    @EquipoPLMD_MGML_AMCS_DRJB
    private static final Preferences NODO = Preferences.userRoot().node("pe/innovasur/sofas");

    @EquipoPLMD_MGML_AMCS_DRJB
    private Prefs() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static boolean bool(String clave, boolean defecto) {
        return NODO.getBoolean(clave, defecto);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static void setBool(String clave, boolean valor) {
        NODO.putBoolean(clave, valor);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String str(String clave, String defecto) {
        return NODO.get(clave, defecto);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static void setStr(String clave, String valor) {
        NODO.put(clave, valor);
    }
}
