package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "carrito_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class CarritoItem {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "configuracion_id")
    private ConfiguracionGuardada configuracion;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false)
    private Integer cantidad;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAnadido = LocalDateTime.now();

    @EquipoPLMD_MGML_AMCS_DRJB
    private String notas;

    @EquipoPLMD_MGML_AMCS_DRJB
    public CarritoItem copiaConCantidad(Integer nuevaCantidad) {
        return toBuilder()
            .id(null)
            .cantidad(nuevaCantidad)
            .build();
    }
}
