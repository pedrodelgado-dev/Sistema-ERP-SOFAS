package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Pago de un cliente: adelanto o saldo de un pedido, o pago de un retapizado. */
@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Pago {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    /** Pedido o retapizado al que se aplica. */
    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 12)
    private String referencia;

    @EquipoPLMD_MGML_AMCS_DRJB
    private LocalDate fecha;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String metodo;

    @EquipoPLMD_MGML_AMCS_DRJB
    private String concepto;
}
