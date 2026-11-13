package pe.erp.sofas.model;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@EquipoPLMD_MGML_AMCS_DRJB
@Entity
@Table(name = "configuracion_guardada")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class ConfiguracionGuardada {

    @EquipoPLMD_MGML_AMCS_DRJB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(nullable = false, length = 100)
    private String nombre;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConfigSofa.Modelo modelo;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer cuerpos;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    private ConfigSofa.Chaise chaise;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer profundidad;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tela_id")
    private Tela tela;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    private ConfigSofa.Brazo brazo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    private ConfigSofa.Patas patas;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    private ConfigSofa.AcabadoMadera acabado;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    private ConfigSofa.Espuma espuma;

    @EquipoPLMD_MGML_AMCS_DRJB
    private Integer cojines;

    @EquipoPLMD_MGML_AMCS_DRJB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tela_cojines_id")
    private Tela telaCojines;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Enumerated(EnumType.STRING)
    private ConfigSofa.Mecanismo mecanismo;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false)
    private Boolean esFavorito = false;

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @EquipoPLMD_MGML_AMCS_DRJB
    @Builder.Default
    private LocalDateTime fechaModificacion = LocalDateTime.now();

    @EquipoPLMD_MGML_AMCS_DRJB
    @Column(length = 50)
    private String etiqueta;

    @EquipoPLMD_MGML_AMCS_DRJB
    @PreUpdate
    void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public ConfigSofa toConfigSofa() {
        return new ConfigSofa(
            modelo,
            cuerpos != null ? cuerpos : 1,
            chaise != null ? chaise : ConfigSofa.Chaise.NINGUNA,
            profundidad != null ? profundidad : 92,
            tela != null ? tela.getId() : null,
            brazo != null ? brazo : ConfigSofa.Brazo.RECTO,
            patas != null ? patas : ConfigSofa.Patas.MADERA,
            acabado != null ? acabado : ConfigSofa.AcabadoMadera.NATURAL,
            espuma != null ? espuma : ConfigSofa.Espuma.D23,
            cojines != null ? cojines : 1,
            telaCojines != null ? telaCojines.getId() : null,
            mecanismo != null ? mecanismo : ConfigSofa.Mecanismo.FIJO
        );
    }

    @EquipoPLMD_MGML_AMCS_DRJB
    public static ConfiguracionGuardada fromConfigSofa(ConfigSofa config, Usuario usuario, String nombre, String descripcion) {
        return ConfiguracionGuardada.builder()
            .usuario(usuario)
            .nombre(nombre)
            .descripcion(descripcion)
            .modelo(config.modelo())
            .cuerpos(config.cuerpos())
            .chaise(config.chaise())
            .profundidad(config.profundidad())
            .brazo(config.brazo())
            .patas(config.patas())
            .acabado(config.acabado())
            .espuma(config.espuma())
            .cojines(config.cojines())
            .mecanismo(config.mecanismo())
            .build();
    }
}
