# ConversorMonedas

Pequeña aplicación de consola en Java que consulta una API pública de tipos de cambio para convertir montos entre monedas (USD, EUR, PEN) y muestra la tasa y el resultado.

Características principales:
- Menú interactivo en consola con opciones fijas (USD, EUR, PEN).
- Lee la API key desde la variable de entorno `EXCHANGE_API_KEY` o desde un archivo `config.properties` (directorio de trabajo o classpath).
- Muestra la tasa de conversión (`conversion_rate`) y el resultado final (`conversion_result`) formateados con dos decimales.
- Manejo básico de errores y mensajes claros para problemas de red o respuesta inválida.

Requisitos
- Java 23 (el `pom.xml` especifica `maven.compiler.source` y `target` = 23).
- Maven 3.x para compilar y empaquetar.
- (Si falta) la librería Gson para parseo JSON (vea la sección "Dependencia Gson" abajo).

Compilar
Desde el run de tu IDE o desde PowerShell, navega al directorio del proyecto.



Ejecución
Tienes dos formas de proporcionar la API key (elige una):

1) Variable de entorno (temporal en la sesión de PowerShell):

```powershell
$env:EXCHANGE_API_KEY = 'TU_API_KEY_AQUI'
# Ejecutar la clase Principal desde el directorio del proyecto
# O si ya usas mvn package, puedes ejecutar las clases compiladas
java -cp target/classes;"path\to\gson.jar" Principal
```

Nota: si prefieres establecer la variable de entorno de forma persistente en Windows usa `setx EXCHANGE_API_KEY "TU_API_KEY_AQUI"` (abre una nueva sesión para que tome efecto).

2) Archivo `config.properties` en el directorio de trabajo (o en `src/main/resources` si prefieres empaquetarlo):

Crea un archivo llamado `config.properties` con el contenido:

```
api.key=TU_API_KEY_AQUI
```

y colócalo en la carpeta donde ejecutas la aplicación (o en `src/main/resources` si quieres que esté en el classpath durante la ejecución empaquetada).

Ejemplo de ejecución (sesión esperada)
- Ejecutas, eliges opción 3 (Dólar a Soles).
- Te pedirá el monto: `250`.
Salida esperada (ejemplo):

```
Tasa de conversión (USD->PEN): 3.363
El valor 250.00 [USD] corresponde al valor final de =>>> 840.75 [PEN].
```

Notas y solución de problemas
- Si al compilar obtienes errores por falta de Gson, revisa la sección siguiente y agrega la dependencia al `pom.xml`.
- Si recibes el error: "API key no encontrada...", asegúrate de tener la variable `EXCHANGE_API_KEY` definida o el archivo `config.properties` con `api.key=...` en el directorio de ejecución.
- Si la API responde con un código distinto a 200, el programa mostrará el código HTTP y el cuerpo recibido.

Dependencia Gson (opcional)
Si al ejecutar la compilación/maven te falta Gson, agrega esto dentro de `<project>` en tu `pom.xml` (en la sección `<dependencies>`):

```xml
<dependencies>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
</dependencies>
```

Después ejecuta `mvn package` de nuevo.

Mejoras recomendadas
- Reusar una instancia de `HttpClient` en `ConsultarConversion` para evitar crear una nueva por petición.
- Historial de Conversiones: Agrega la capacidad de rastrear y mostrar el historial de las últimas conversiones realizadas, brindando a los usuarios una visión completa de sus actividades.
- Soporte para Más Monedas: Amplía la lista de monedas disponibles para la elección, permitiendo a los usuarios convertir entre una variedad aún mayor de opciones monetarias.
- Registros con Marca de Tiempo: Utiliza las funciones de la biblioteca java.time para crear registros que registren las conversiones realizadas, incluyendo información sobre qué monedas se convirtieron y en qué momento.
- Agregar pruebas unitarias (mockear la llamada HTTP) para validar el parsing del JSON.
- Integrar otras APIs públicas de tipos de cambio para ofrecer más opciones y comparar tasas.


