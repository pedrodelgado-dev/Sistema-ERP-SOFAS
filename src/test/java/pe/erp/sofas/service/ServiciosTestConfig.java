package pe.erp.sofas.service;
import pe.erp.sofas.autoria.EquipoPLMD_MGML_AMCS_DRJB;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** Contexto de pruebas: solo servicios, semilla y repositorios (sin interfaz JavaFX). */
@EquipoPLMD_MGML_AMCS_DRJB
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"pe.erp.sofas.service", "pe.erp.sofas.seeder"})
@EntityScan("pe.erp.sofas.model")
@EnableJpaRepositories("pe.erp.sofas.repository")
public class ServiciosTestConfig {
}
