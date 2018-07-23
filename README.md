# Call Center - Dispatcher

Solución que intenta simular un despachador de llamadas en un Call center, para lo cual se implementaron features de Java 8 para resolver temas de Concurrencia, rendimiento y legibilidad de codigo:

CompletableFuture : Administra la concurrencia de las llamadas entrantes, el cual tiene características únicas para predecir el resultado de las solicitudes y de esta manera poder liberar los empleados despues de atendida una solicitud.


## Extras/Plus

Se implemento método sincronizado a nivel de clase para garantizar que más de una llamada no sea atendida por un mismo empleado en un mismo instante.

En caso que no existan empleados disponibles, el despachador realiza un intento de llamado, enviando nuevamente la solicitud en un nuevo hilo después de dos segundos de espera, esta solicitud competirá con las llamadas que encuentren en espera.


## Prerequisites

Java 8 y Maven son necesarios para ejecutar las pruebas unitarias.

## Compiling

```bash
mvn clean install
```

## Author

Alexander Guerrero
alexg98@gmail.com
