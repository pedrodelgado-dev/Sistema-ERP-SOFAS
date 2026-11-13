package pe.erp.sofas.core;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Navegacion entre modulos con historial (boton Regresar) e intenciones:
 * {@code navigator.goTo("pedidos", Intent.NEW)} abre Pedidos con el formulario de nuevo pedido.
 */
@EquipoPLMD_MGML_AMCS_DRJB
@Component
@Lazy
public class Navigator {

    /** Implementado por el shell. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public interface Host {
        @EquipoPLMD_MGML_AMCS_DRJB
        void mostrar(Modulo modulo, Intent intent, boolean registrarHistorial);

        @EquipoPLMD_MGML_AMCS_DRJB
        boolean regresar();

        @EquipoPLMD_MGML_AMCS_DRJB
        void refrescarContadores();

        @EquipoPLMD_MGML_AMCS_DRJB
        void cerrarSesion();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private Host host;

    @EquipoPLMD_MGML_AMCS_DRJB
    public void registrar(Host host) {
        this.host = host;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void goTo(String modulo) {
        goTo(modulo, Intent.NONE);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void goTo(String modulo, Intent intent) {
        if (host != null) host.mostrar(Modulo.porClave(modulo), intent, true);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean back() {
        return host != null && host.regresar();
    }

    /** Pide confirmacion y vuelve al login. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public void cerrarSesion() {
        if (host != null) host.cerrarSesion();
    }

    /** Recalcula los contadores del menu lateral y la campana tras un cambio de datos. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public void datosCambiaron() {
        if (host != null) host.refrescarContadores();
    }
}
