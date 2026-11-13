package pe.erp.sofas.ui.components;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import javafx.concurrent.Task;
import pe.erp.sofas.core.NegocioException;
import pe.erp.sofas.core.Overlays;
import pe.erp.sofas.core.Toast;

import java.util.function.Consumer;
import java.util.function.Supplier;

/** Ejecuta trabajos pesados (PDF, Excel, MRP, optimizacion de corte) en un Task con indicador de progreso. */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Tareas {

    @EquipoPLMD_MGML_AMCS_DRJB
    private Tareas() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static <T> void ejecutar(Overlays overlays, Toast toast, String mensaje, Supplier<T> trabajo, Consumer<T> alTerminar) {
        Overlays.Handle espera = overlays.cargando(mensaje);
        Task<T> t = new Task<>() {
            @EquipoPLMD_MGML_AMCS_DRJB
            @Override
            protected T call() {
                return trabajo.get();
            }
        };
        t.setOnSucceeded(e -> {
            espera.cerrar();
            alTerminar.accept(t.getValue());
        });
        t.setOnFailed(e -> {
            espera.cerrar();
            Throwable ex = t.getException();
            while (ex != null && !(ex instanceof NegocioException) && ex.getCause() != null) ex = ex.getCause();
            toast.error(ex instanceof NegocioException ? ex.getMessage() : "No se pudo completar: " + (ex == null ? "error" : ex.getMessage()));
            if (!(ex instanceof NegocioException) && t.getException() != null) {
                org.slf4j.LoggerFactory.getLogger(Tareas.class).error("Tarea fallida", t.getException());
            }
        });
        Thread hilo = new Thread(t, "innova-tarea");
        hilo.setDaemon(true);
        hilo.start();
    }
}
