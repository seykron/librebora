# borak

Servicios web para acceder al Boletín Oficial de la República Argentina (BORA).

## Punto de entrada

Ejecutar la clase ```net.borak.Application```.

Esta clase crea el application server y lo asocia al contexto de Spring.

El contexto de Spring se crea utilizando el DSL de Spring 5. Las definiciones de beans están en el paquete
```net.borak.config```.

Los servicios web los maneja spring-webflux. La configuración de webflux se inicializa con los parámetros 
predeterminados utilizando la clase ```net.borak.config.ApplicationConfig```.

## Endpoints

### Listar una sección

```
/bora/sections/{nombre-seccion}
```

Las nombres de secciones son los siguientes: primera, segunda, tercera, cuarta.

### Obtener un expediente específico de una sección

```
/bora/sections/{nombre-seccion}/{id-de-expediente}
```
