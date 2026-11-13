package pe.erp.sofas.service.calculo;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import pe.erp.sofas.model.*;

import java.util.*;

/**
 * Explosion de una ficha tecnica (seccion 6): neta = base × factor(dep) × cantidad;
 * bruta = neta × (1 + merma %); las unidades enteras se redondean hacia arriba.
 * Las fichas compuestas suman sus hijas aprobadas y consolidan por material.
 */
@EquipoPLMD_MGML_AMCS_DRJB
public final class Explosion {

    /** Linea calculada para un material dentro de un componente. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public record Linea(@EquipoPLMD_MGML_AMCS_DRJB
      Material material, @EquipoPLMD_MGML_AMCS_DRJB
        Componente componente, @EquipoPLMD_MGML_AMCS_DRJB
          double base, @EquipoPLMD_MGML_AMCS_DRJB
            double factor, @EquipoPLMD_MGML_AMCS_DRJB
              double neta,
                        @EquipoPLMD_MGML_AMCS_DRJB
                        double mermaPct, @EquipoPLMD_MGML_AMCS_DRJB
                          double bruta, @EquipoPLMD_MGML_AMCS_DRJB
                            double costo, @EquipoPLMD_MGML_AMCS_DRJB
                              boolean opcional, @EquipoPLMD_MGML_AMCS_DRJB
                                boolean extra,
                        @EquipoPLMD_MGML_AMCS_DRJB
                        String origen) {
    }

    /** Requerimiento consolidado de un material. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public record PorMaterial(@EquipoPLMD_MGML_AMCS_DRJB
      Material material, @EquipoPLMD_MGML_AMCS_DRJB
        double cantidad, @EquipoPLMD_MGML_AMCS_DRJB
          double costo) {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public record Resultado(@EquipoPLMD_MGML_AMCS_DRJB
      FichaTecnica ficha, @EquipoPLMD_MGML_AMCS_DRJB
        int cantidad, @EquipoPLMD_MGML_AMCS_DRJB
          Medidas medidas, @EquipoPLMD_MGML_AMCS_DRJB
            List<Linea> lineas,
                            @EquipoPLMD_MGML_AMCS_DRJB
                            double costoMateriales, @EquipoPLMD_MGML_AMCS_DRJB
                              double horasMO, @EquipoPLMD_MGML_AMCS_DRJB
                                double costoMO, @EquipoPLMD_MGML_AMCS_DRJB
                                  double indirectosPct,
                            @EquipoPLMD_MGML_AMCS_DRJB
                            double indirectos) {

        @EquipoPLMD_MGML_AMCS_DRJB
        public double costoEstandar() {
            return costoMateriales + costoMO + indirectos;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public double costoUnitario() {
            return cantidad == 0 ? 0 : costoEstandar() / cantidad;
        }

        /** Consolidado por material (orden de aparicion). */
        @EquipoPLMD_MGML_AMCS_DRJB
        public List<PorMaterial> porMaterial() {
            Map<String, double[]> acc = new LinkedHashMap<>();
            Map<String, Material> mats = new HashMap<>();
            for (Linea l : lineas) {
                String c = l.material().getCodigo();
                mats.put(c, l.material());
                double[] a = acc.computeIfAbsent(c, k -> new double[2]);
                a[0] += l.bruta();
                a[1] += l.costo();
            }
            List<PorMaterial> r = new ArrayList<>();
            acc.forEach((c, a) -> {
                Material m = mats.get(c);
                double cant = m.isEntera() ? Math.ceil(a[0] - 1e-9) : a[0];
                r.add(new PorMaterial(m, cant, a[1]));
            });
            return r;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public Map<Componente, Double> costoPorComponente() {
            Map<Componente, Double> m = new EnumMap<>(Componente.class);
            for (Linea l : lineas) m.merge(l.componente(), l.costo(), Double::sum);
            return m;
        }
    }

    /** Destino de sustitucion que quita la linea (p. ej. el zocalo reemplaza a las patas). */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String QUITAR = "-";

    @EquipoPLMD_MGML_AMCS_DRJB
    private Explosion() {
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static Resultado explotar(FichaTecnica ficha, int cantidad, Opciones op, Catalogo cat) {
        if (op == null) op = Opciones.NINGUNA;
        if (Boolean.TRUE.equals(ficha.getCompuesta())) return compuesta(ficha, cantidad, op, cat);

        Medidas base = Medidas.de(ficha);
        Medidas pedidas = op.medidas() == null ? base : op.medidas();
        List<Linea> lineas = new ArrayList<>();
        for (LineaBOM l : ficha.getLineas()) {
            if (Boolean.TRUE.equals(l.getOpcional()) && !op.incluirOpcionales()) continue;
            if (QUITAR.equals(op.sust().get(l.getMaterial().getCodigo()))) continue;
            Material m = sustituir(l.getMaterial(), op, cat);
            double factor = Formula.factor(l.getDependencia(), pedidas, base);
            double neta = l.getCantidad() * factor * cantidad;
            double bruta = neta * (1 + nz(l.getMermaPct()) / 100.0);
            if (m.isEntera()) bruta = Math.ceil(bruta - 1e-9);
            lineas.add(new Linea(m, l.getComponente(), l.getCantidad(), factor, neta, nz(l.getMermaPct()), bruta,
                    bruta * m.getCosto(), Boolean.TRUE.equals(l.getOpcional()), false, ficha.getEtiqueta()));
        }
        for (LineaExtra e : op.ext()) {
            Material m = cat.material(e.getMaterialCodigo());
            if (m == null) continue;
            double cant = e.getCantidad() * cantidad;
            if (m.isEntera()) cant = Math.ceil(cant - 1e-9);
            Componente comp = componente(e.getComponente());
            lineas.add(new Linea(m, comp, e.getCantidad(), 1, cant, 0, cant, cant * m.getCosto(), false, true, "Extra"));
        }
        double materiales = lineas.stream().mapToDouble(Linea::costo).sum();
        double horas = ficha.getHorasMO() * Formula.factorManoObra(pedidas, base) * cantidad;
        double mo = costoManoObra(ficha) * Formula.factorManoObra(pedidas, base) * cantidad;
        mo = mo * (1 + op.recargoManoObraPct() / 100.0) + op.manoObraFija() * cantidad;
        horas = horas * (1 + op.recargoManoObraPct() / 100.0);
        double pct = nz(ficha.getIndirectosPct());
        return new Resultado(ficha, cantidad, pedidas, lineas, materiales, horas, mo, pct, (materiales + mo) * pct / 100.0);
    }

    /** Juego 3-2-1: explota cada hija aprobada con las mismas sustituciones (misma tela para todo el juego). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private static Resultado compuesta(FichaTecnica ficha, int cantidad, Opciones op, Catalogo cat) {
        List<Linea> lineas = new ArrayList<>();
        double horas = 0, mo = 0;
        Opciones hijaOp = op.conMedidas(null);
        Opciones sinExtras = new Opciones(null, hijaOp.sust(), List.of(), hijaOp.incluirOpcionales(), 0, 0);
        for (String codigo : ficha.getHijos()) {
            FichaTecnica hija = cat.fichaAprobada(codigo);
            if (hija == null) continue;
            Resultado r = explotar(hija, cantidad, sinExtras, cat);
            lineas.addAll(r.lineas());
            horas += r.horasMO();
            mo += r.costoMO();
        }
        for (LineaExtra e : op.ext()) {
            Material m = cat.material(e.getMaterialCodigo());
            if (m == null) continue;
            double cant = e.getCantidad() * cantidad;
            if (m.isEntera()) cant = Math.ceil(cant - 1e-9);
            lineas.add(new Linea(m, componente(e.getComponente()), e.getCantidad(), 1, cant, 0, cant, cant * m.getCosto(), false, true, "Extra"));
        }
        mo = mo * (1 + op.recargoManoObraPct() / 100.0) + op.manoObraFija() * cantidad;
        double materiales = lineas.stream().mapToDouble(Linea::costo).sum();
        double pct = nz(ficha.getIndirectosPct());
        return new Resultado(ficha, cantidad, null, lineas, materiales, horas, mo, pct, (materiales + mo) * pct / 100.0);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static double costoManoObra(FichaTecnica f) {
        return f.getOperaciones().stream().mapToDouble(OperacionMO::getCosto).sum();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static Material sustituir(Material original, Opciones op, Catalogo cat) {
        String destino = op.sust().get(original.getCodigo());
        if (destino == null || destino.equals(original.getCodigo())) return original;
        Material m = cat.material(destino);
        return m == null ? original : m;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static Componente componente(String nombre) {
        if (nombre == null) return Componente.ENSAMBLAJE;
        try {
            return Componente.valueOf(nombre);
        } catch (IllegalArgumentException e) {
            return Componente.ENSAMBLAJE;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static double nz(Double d) {
        return d == null ? 0 : d;
    }
}
