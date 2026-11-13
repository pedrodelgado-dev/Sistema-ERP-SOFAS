package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import java.util.LinkedHashMap;
import java.util.Map;

/** Configuracion de un sofa en el configurador 3D (y para dibujarlo con SofaBuilder). */
@EquipoPLMD_MGML_AMCS_DRJB
public record ConfigSofa(@EquipoPLMD_MGML_AMCS_DRJB
  Modelo modelo, @EquipoPLMD_MGML_AMCS_DRJB
    int cuerpos, @EquipoPLMD_MGML_AMCS_DRJB
      Chaise chaise, @EquipoPLMD_MGML_AMCS_DRJB
        int profundidad, @EquipoPLMD_MGML_AMCS_DRJB
          Long telaId, @EquipoPLMD_MGML_AMCS_DRJB
            Brazo brazo,
                         @EquipoPLMD_MGML_AMCS_DRJB
                         Patas patas, @EquipoPLMD_MGML_AMCS_DRJB
                           AcabadoMadera acabado, @EquipoPLMD_MGML_AMCS_DRJB
                             Espuma espuma, @EquipoPLMD_MGML_AMCS_DRJB
                               int cojines, @EquipoPLMD_MGML_AMCS_DRJB
                                 Long telaCojinesId,
                         @EquipoPLMD_MGML_AMCS_DRJB
                         Mecanismo mecanismo) {

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Modelo {
        @EquipoPLMD_MGML_AMCS_DRJB
        MODERNO("Sofá Moderno", "SF-001", Forma.MODERNO),
        @EquipoPLMD_MGML_AMCS_DRJB
        SECCIONAL_L("Seccional L", "SF-005", Forma.SECCIONAL_L),
        @EquipoPLMD_MGML_AMCS_DRJB
        SILLON("Sillón", "SF-003", Forma.SILLON),
        @EquipoPLMD_MGML_AMCS_DRJB
        CHESTERFIELD("Chesterfield", "SF-006", Forma.CHESTERFIELD),
        @EquipoPLMD_MGML_AMCS_DRJB
        SOFA_CAMA("Sofá cama", "SF-007", Forma.SOFA_CAMA),
        @EquipoPLMD_MGML_AMCS_DRJB
        ESCANDINAVO("Escandinavo", "SF-009", Forma.ESCANDINAVO);

        @EquipoPLMD_MGML_AMCS_DRJB
        private final String nombre;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final String producto;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final Forma forma;

        @EquipoPLMD_MGML_AMCS_DRJB
        Modelo(String nombre, String producto, Forma forma) {
            this.nombre = nombre;
            this.producto = producto;
            this.forma = forma;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String nombre() {
            return nombre;
        }

        /** Codigo del modelo del catalogo del que toma el precio "desde". */
        @EquipoPLMD_MGML_AMCS_DRJB
        public String producto() {
            return producto;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public Forma forma() {
            return forma;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Chaise {
        @EquipoPLMD_MGML_AMCS_DRJB
        NINGUNA("Sin chaise"), @EquipoPLMD_MGML_AMCS_DRJB
          IZQUIERDA("Izquierda"), @EquipoPLMD_MGML_AMCS_DRJB
           DERECHA("Derecha");

        @EquipoPLMD_MGML_AMCS_DRJB
        private final String nombre;

        @EquipoPLMD_MGML_AMCS_DRJB
        Chaise(String nombre) {
            this.nombre = nombre;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String nombre() {
            return nombre;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Brazo {
        @EquipoPLMD_MGML_AMCS_DRJB
        RECTO("Recto"), @EquipoPLMD_MGML_AMCS_DRJB
         REDONDEADO("Redondeado"), @EquipoPLMD_MGML_AMCS_DRJB
          DELGADO("Delgado");

        @EquipoPLMD_MGML_AMCS_DRJB
        private final String nombre;

        @EquipoPLMD_MGML_AMCS_DRJB
        Brazo(String nombre) {
            this.nombre = nombre;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String nombre() {
            return nombre;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Patas {
        @EquipoPLMD_MGML_AMCS_DRJB
        MADERA("Madera cónica", null, 0, false),
        @EquipoPLMD_MGML_AMCS_DRJB
        METAL_NEGRO("Metal negro", "MP-022", 14, false),
        @EquipoPLMD_MGML_AMCS_DRJB
        METAL_DORADO("Metal dorado", "MP-023", 20, false),
        @EquipoPLMD_MGML_AMCS_DRJB
        ZOCALO("Zócalo oculto", "MP-035", 0, true);

        @EquipoPLMD_MGML_AMCS_DRJB
        private final String nombre;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final String material;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final double recargo;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final boolean bajoPedido;

        @EquipoPLMD_MGML_AMCS_DRJB
        Patas(String nombre, String material, double recargo, boolean bajoPedido) {
            this.nombre = nombre;
            this.material = material;
            this.recargo = recargo;
            this.bajoPedido = bajoPedido;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String nombre() {
            return nombre;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String material() {
            return material;
        }

        /** Diferencia de precio por pata (+S/ 14 c/u, +S/ 20 c/u). */
        @EquipoPLMD_MGML_AMCS_DRJB
        public double recargo() {
            return recargo;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public boolean bajoPedido() {
            return bajoPedido;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum AcabadoMadera {
        @EquipoPLMD_MGML_AMCS_DRJB
        NATURAL("Natural", "#C69A6D"), @EquipoPLMD_MGML_AMCS_DRJB
          NOGAL("Nogal", "#7A4E2B"), @EquipoPLMD_MGML_AMCS_DRJB
            WENGUE("Wengué", "#3B2A20");

        @EquipoPLMD_MGML_AMCS_DRJB
        private final String nombre;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final String hex;

        @EquipoPLMD_MGML_AMCS_DRJB
        AcabadoMadera(String nombre, String hex) {
            this.nombre = nombre;
            this.hex = hex;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String nombre() {
            return nombre;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String hex() {
            return hex;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Espuma {
        @EquipoPLMD_MGML_AMCS_DRJB
        D23("Espuma D23 estándar", null, false),
        @EquipoPLMD_MGML_AMCS_DRJB
        D30("D30 alta densidad", "MP-020", false),
        @EquipoPLMD_MGML_AMCS_DRJB
        VISCO("Viscoelástica", "MP-034", true);

        @EquipoPLMD_MGML_AMCS_DRJB
        private final String nombre;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final String material;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final boolean bajoPedido;

        @EquipoPLMD_MGML_AMCS_DRJB
        Espuma(String nombre, String material, boolean bajoPedido) {
            this.nombre = nombre;
            this.material = material;
            this.bajoPedido = bajoPedido;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String nombre() {
            return nombre;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String material() {
            return material;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public boolean bajoPedido() {
            return bajoPedido;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public enum Mecanismo {
        @EquipoPLMD_MGML_AMCS_DRJB
        FIJO("Fijo", false),
        @EquipoPLMD_MGML_AMCS_DRJB
        RECLINABLE_MANUAL("Reclinable manual", false),
        @EquipoPLMD_MGML_AMCS_DRJB
        RECLINABLE_ELECTRICO("Reclinable eléctrico", true),
        @EquipoPLMD_MGML_AMCS_DRJB
        CAMA("Cama plegable", false);

        @EquipoPLMD_MGML_AMCS_DRJB
        private final String nombre;
        @EquipoPLMD_MGML_AMCS_DRJB
        private final boolean bajoPedido;

        @EquipoPLMD_MGML_AMCS_DRJB
        Mecanismo(String nombre, boolean bajoPedido) {
            this.nombre = nombre;
            this.bajoPedido = bajoPedido;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public String nombre() {
            return nombre;
        }

        @EquipoPLMD_MGML_AMCS_DRJB
        public boolean bajoPedido() {
            return bajoPedido;
        }
    }

    /** Largo segun los cuerpos: 95 / 160 / 210 / 260 cm. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public static int largoPara(int cuerpos) {
        return switch (cuerpos) {
            case 1 -> 95;
            case 2 -> 160;
            case 3 -> 210;
            default -> 260;
        };
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public int largo() {
        return largoPara(cuerpos);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public int alto() {
        return modelo == Modelo.CHESTERFIELD ? 78 : 85;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public boolean conChaise() {
        return chaise != null && chaise != Chaise.NINGUNA;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa con(Modelo m) {
        return new ConfigSofa(m, cuerpos, chaise, profundidad, telaId, brazo, patas, acabado, espuma, cojines, telaCojinesId, mecanismo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa conCuerpos(int n) {
        return new ConfigSofa(modelo, n, chaise, profundidad, telaId, brazo, patas, acabado, espuma, cojines, telaCojinesId, mecanismo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa conChaise(Chaise c) {
        return new ConfigSofa(modelo, cuerpos, c, profundidad, telaId, brazo, patas, acabado, espuma, cojines, telaCojinesId, mecanismo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa conProfundidad(int p) {
        return new ConfigSofa(modelo, cuerpos, chaise, p, telaId, brazo, patas, acabado, espuma, cojines, telaCojinesId, mecanismo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa conTela(Long t) {
        return new ConfigSofa(modelo, cuerpos, chaise, profundidad, t, brazo, patas, acabado, espuma, cojines, telaCojinesId, mecanismo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa conBrazo(Brazo b) {
        return new ConfigSofa(modelo, cuerpos, chaise, profundidad, telaId, b, patas, acabado, espuma, cojines, telaCojinesId, mecanismo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa conPatas(Patas p) {
        return new ConfigSofa(modelo, cuerpos, chaise, profundidad, telaId, brazo, p, acabado, espuma, cojines, telaCojinesId, mecanismo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa conAcabado(AcabadoMadera a) {
        return new ConfigSofa(modelo, cuerpos, chaise, profundidad, telaId, brazo, patas, a, espuma, cojines, telaCojinesId, mecanismo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa conEspuma(Espuma e) {
        return new ConfigSofa(modelo, cuerpos, chaise, profundidad, telaId, brazo, patas, acabado, e, cojines, telaCojinesId, mecanismo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa conCojines(int n, Long tela) {
        return new ConfigSofa(modelo, cuerpos, chaise, profundidad, telaId, brazo, patas, acabado, espuma, n, tela, mecanismo);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa conMecanismo(Mecanismo m) {
        return new ConfigSofa(modelo, cuerpos, chaise, profundidad, telaId, brazo, patas, acabado, espuma, cojines, telaCojinesId, m);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String serializar() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("modelo", modelo.name());
        m.put("cuerpos", String.valueOf(cuerpos));
        m.put("chaise", chaise.name());
        m.put("prof", String.valueOf(profundidad));
        m.put("tela", telaId == null ? "" : telaId.toString());
        m.put("brazo", brazo.name());
        m.put("patas", patas.name());
        m.put("acabado", acabado.name());
        m.put("espuma", espuma.name());
        m.put("cojines", String.valueOf(cojines));
        m.put("telaCojines", telaCojinesId == null ? "" : telaCojinesId.toString());
        m.put("mec", mecanismo.name());
        StringBuilder sb = new StringBuilder();
        m.forEach((k, v) -> sb.append(k).append('=').append(v).append(';'));
        return sb.toString();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static ConfigSofa leer(String s) {
        if (s == null || s.isBlank()) return null;
        Map<String, String> m = new LinkedHashMap<>();
        for (String par : s.split(";")) {
            int i = par.indexOf('=');
            if (i > 0) m.put(par.substring(0, i), par.substring(i + 1));
        }
        try {
            return new ConfigSofa(Modelo.valueOf(m.get("modelo")), Integer.parseInt(m.get("cuerpos")),
                    Chaise.valueOf(m.get("chaise")), Integer.parseInt(m.get("prof")), id(m.get("tela")),
                    Brazo.valueOf(m.get("brazo")), Patas.valueOf(m.get("patas")), AcabadoMadera.valueOf(m.get("acabado")),
                    Espuma.valueOf(m.get("espuma")), Integer.parseInt(m.get("cojines")), id(m.get("telaCojines")),
                    Mecanismo.valueOf(m.get("mec")));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    private static Long id(String s) {
        return s == null || s.isBlank() ? null : Long.valueOf(s);
    }
}
