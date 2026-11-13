# Identificación de autoría incorporada al código ejecutable

Este proyecto conserva los nombres originales de clases, métodos y variables empleados por JavaFX, Spring Boot, JPA, y sus archivos de configuración, para evitar la rotura de enlaces.

La identificación del equipo no depende únicamente de cabeceras o comentarios:

- `@EquipoPLMD_MGML_AMCS_DRJB` es una **anotación Java real**, aplicada sobre clases, métodos, constructores y campos (variables miembro). Se conserva en el `.class` con retención **RUNTIME** y puede consultarse mediante reflexión.
- `RegistroAutoriaPLMD_MGML_AMCS_DRJB` contiene constantes `public static final String` cuyos nombres identifican a PLMD, MGML, AMCS y DRJB y cuyos valores contienen los **nombres completos**. Tiene métodos ejecutables para devolver los autores y la firma del sistema.
- Las clases de entrada `Launcher`, `MainApp` y `SofasApplication` contienen el campo `IDENTIDAD_DEL_CODIGO_PLMD_MGML_AMCS_DRJB`; `Launcher` incluye el método `obtenerFirmaDeAutoriaPLMD_MGML_AMCS_DRJB()`.
- Los identificadores de autoría forman parte de anotaciones, constantes y métodos Java; no se utilizan cabeceras ni pies repetitivos de autoría.

## Autores

| Iniciales | Nombre completo |
|---|---|
| PLMD | Pedro Luis Mamani Delgado |
| MGML | Maribel Gloria Mamani Lopez |
| AMCS | Alessandro Mijail Calcina Sanchez |
| DRJB | Danny Rodrigo Jove Benites |

## Ejemplo real en las clases

```java
@EquipoPLMD_MGML_AMCS_DRJB
public class Launcher {
    @EquipoPLMD_MGML_AMCS_DRJB
    public static final String IDENTIDAD_DEL_CODIGO_PLMD_MGML_AMCS_DRJB =
            RegistroAutoriaPLMD_MGML_AMCS_DRJB.IDENTIFICADOR_PLMD_MGML_AMCS_DRJB;

    @EquipoPLMD_MGML_AMCS_DRJB
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}
```

En el `.class`, por ejemplo, se puede consultar la marca de autoría de `Launcher.class`:

```java
var sello = Launcher.class.getAnnotation(EquipoPLMD_MGML_AMCS_DRJB.class);
System.out.println(sello.equipo());
System.out.println(java.util.Arrays.toString(sello.nombres()));
```

**Nota:** una marca de autoría identifica el trabajo atribuido al equipo, pero no reemplaza el historial de Git, licencias, acuerdos de contribución o las pruebas necesarias para acreditar derechos de propiedad intelectual.

**Pruebas:** se verificó la sintaxis de los 264 archivos Java y se compiló de forma aislada el registro de autoría y la anotación. No se verificó una compilación integral de Spring Boot / JavaFX por falta de todas las dependencias externas en el entorno de edición. El proyecto mantiene `pom.xml` con Java 21 y Lombok configurado para procesamiento de anotaciones.
