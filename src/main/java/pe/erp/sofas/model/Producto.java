package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Modelo de sofa del catalogo. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Producto {

    @EquipoPLMD_MGML_AMCS_DRJB
    public static final List<String> CATEGORIAS = List.of(
            "Sofá", "Juego de sala", "Seccional", "Sillón", "Sofá cama", "Chesterfield", "Reclinable", "Puff");

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private String nombre;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, length = 30)
    private String categoria;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Forma forma;

    /** Material de tapiz por defecto ("Chenille"). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String material;

    /** Color por defecto ("Gris perla"). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String color;

    /** Hex del color por defecto, para el render 3D. */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String colorHex;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Integer largo = 0;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Integer fondo = 0;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Integer alto = 0;
    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private Integer cuerpos = 3;

    /** Texto libre que reemplaza a L × F × A (juegos: "3 + 2 + 1 cuerpos"). */
    @EquipoPLMD_MGML_AMCS_DRJB
    private String medidasTexto;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(precision = 12, scale = 2)
    private BigDecimal costo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Integer stock = 0;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 600)
    private String descripcion;

    /** Rutas de imagen: principal + 4 miniaturas separadas por "|" (vacio = sin foto). */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 1500)
    private String imagenes;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    /** 0 → A pedido; 1–2 → Stock bajo; 3 o mas → Disponible. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public String getEstado() {
        return estadoPara(stock);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static String estadoPara(int stock) {
        if (stock <= 0) return "A pedido";
        if (stock <= 2) return "Stock bajo";
        return "Disponible";
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getMedidas() {
        if (medidasTexto != null && !medidasTexto.isBlank()) return medidasTexto;
        return largo + " × " + fondo + " × " + alto + " cm";
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public BigDecimal getMargen() {
        if (costo == null || precio == null || precio.signum() == 0) return BigDecimal.ZERO;
        return precio.subtract(costo).multiply(BigDecimal.valueOf(100)).divide(precio, 1, java.math.RoundingMode.HALF_UP);
    }

    /** Lista de 5 posiciones (principal + 4); null donde no hay imagen. */
    @EquipoPLMD_MGML_AMCS_DRJB
    public List<String> getListaImagenes() {
        List<String> l = new ArrayList<>(Arrays.asList(new String[5]));
        if (imagenes != null && !imagenes.isEmpty()) {
            String[] p = imagenes.split("\\|", -1);
            for (int i = 0; i < Math.min(5, p.length); i++) l.set(i, p[i].isBlank() ? null : p[i]);
        }
        return l;
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void setImagen(int indice, String ruta) {
        List<String> l = getListaImagenes();
        l.set(indice, ruta);
        imagenes = String.join("|", l.stream().map(s -> s == null ? "" : s).toList());
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public String getImagenPrincipal() {
        return getListaImagenes().get(0);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    @Override
    public String toString() {
        return nombre;
    }
}
