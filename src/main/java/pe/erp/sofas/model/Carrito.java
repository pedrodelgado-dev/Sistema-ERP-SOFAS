package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "carrito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Carrito {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("fechaAnadido ASC")
    private List<CarritoItem> items = new ArrayList<>();

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private LocalDateTime fechaModificacion = LocalDateTime.now();

    @EquipoPLMD_MGML_AMCS_DRJB
    @PreUpdate
    void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void agregarItem(ConfiguracionGuardada config, Integer cantidad) {
        CarritoItem item = CarritoItem.builder()
            .carrito(this)
            .configuracion(config)
            .cantidad(cantidad)
            .build();
        items.add(item);
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void removerItem(Long itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void actualizarCantidad(Long itemId, Integer nuevaCantidad) {
        items.stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst()
            .ifPresent(item -> item.setCantidad(nuevaCantidad));
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public void vaciar() {
        items.clear();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Integer obtenerCantidadTotal() {
        return items.stream()
            .mapToInt(CarritoItem::getCantidad)
            .sum();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public Integer obtenerCantidadItems() {
        return items.size();
    }
}
