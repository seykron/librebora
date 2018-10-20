# borak

Servicios web para acceder al Boletín Oficial de la República Argentina (BORA).

## Punto de entrada

Ejecutar la clase ```net.borak.Application```

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
