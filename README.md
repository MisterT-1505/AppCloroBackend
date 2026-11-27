# Backend App Cloro

Microservicios Spring Boot para la aplicación móvil de monitoreo de cloro en granjas avícolas.

## Integrantes

- Benjamin (MisterT-1505): `ms-mediciones` + integración de la app + Open-Meteo + APK.
- Javier: `ms-operaciones` + integración de login/alertas/acciones + pruebas.

## Estado actual

| Módulo | Estado |
|---|---|
| `ms-mediciones` | Terminado y probado (10 pruebas automáticas) |
| `ms-operaciones` | Pendiente, lo desarrolla Javier (ver `ms-operaciones/README.md`) |
| App Android | Conectada a `ms-mediciones` y a Open-Meteo; login/alertas aún con datos locales hasta que Javier entregue `ms-operaciones` |

## Estructura

```
Backend/
├── ms-mediciones/   -> puerto 8081 (Benjamin)
│   ├── granjas
│   ├── puntos de medición
│   └── mediciones de cloro
├── ms-operaciones/  -> puerto 8082 (Javier)
│   ├── usuarios / login
│   ├── alertas
│   └── acciones correctivas
└── README.md
```

Cada microservicio tiene su propia base de datos H2 en archivo y no comparte
tablas con el otro.

## Requisitos

- JDK 21
- Maven Wrapper incluido (`mvnw` / `mvnw.cmd`)
- No se necesita instalar Maven ni MySQL.

## Ejecutar ms-mediciones

```bash
cd ms-mediciones
./mvnw spring-boot:run
```

En Windows:

```powershell
cd ms-mediciones
.\mvnw.cmd spring-boot:run
```

Probar:

```text
http://localhost:8081/api/health
http://localhost:8081/api/granjas
http://localhost:8081/api/puntos
http://localhost:8081/api/mediciones
```

Consola H2:

```text
http://localhost:8081/h2-console
JDBC URL: jdbc:h2:file:./data/mediciones
Usuario: sa
Contraseña: (vacía)
```

## Pruebas de ms-mediciones

```powershell
cd ms-mediciones
.\mvnw.cmd test
```

Incluye:

- `CloroCalculatorTest`: reglas de Normal / Advertencia / Crítico.
- `MsMedicionesApplicationTests`: carga del contexto Spring.
- `MedicionApiIntegrationTest`: catálogo, últimas por punto, simulación,
  creación de medición y error 404.

## Ejecutar ms-operaciones

Pendiente: lo desarrolla Javier. Ver `ms-operaciones/README.md`.

## Endpoints de ms-mediciones

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/granjas` | Lista las granjas |
| GET | `/api/granjas/{granjaId}/puntos` | Puntos de una granja (acepta ID o nombre) |
| GET | `/api/puntos?granja=G1` | Lista de puntos (filtro opcional, acepta ID o nombre) |
| GET | `/api/puntos/{puntoId}` | Detalle de un punto |
| GET | `/api/mediciones?granja=G1&puntoId=G1-P1` | Historial con filtros (acepta ID o nombre) |
| GET | `/api/mediciones/ultimas?granja=G1` | Última medición por punto (acepta ID o nombre) |
| GET | `/api/mediciones/punto/{puntoId}` | Historial de un punto |
| GET | `/api/mediciones/{id}` | Una medición |
| POST | `/api/mediciones` | Crear medición |
| PUT | `/api/mediciones/{id}` | Actualizar medición |
| DELETE | `/api/mediciones/{id}` | Eliminar medición |
| POST | `/api/mediciones/simular?granja=G1` | Simular lectura de sensores (acepta ID o nombre) |
| GET | `/api/health` | Estado del servicio |

### Detalles importantes

- El parámetro `granja` acepta el ID (`G1`) o el nombre (`Granja Melipilla`),
  porque la app trabaja con nombres y las pruebas con IDs.
- En `POST /api/mediciones` el backend toma la granja y el galpón desde el
  punto (`puntoId`), calcula el `estado` y actualiza el punto. Los campos
  `granja` y `galpon` del body son opcionales.
- Si `fechaHora` viene vacía se usa la hora actual del servidor.
- Los errores se devuelven como JSON con `timestamp`, `status`, `error` y
  `message`.

### Ejemplo de creación

```json
POST /api/mediciones
{
  "puntoId": "G1-P1",
  "nivelCloroPpm": 3.75,
  "fechaHora": "2026-11-26T10:15:00"
}
```

La respuesta incluye el `id`, el `estado` calculado y la fecha en ISO-8601:

```json
{
  "id": 13,
  "puntoId": "G1-P1",
  "granja": "Granja Melipilla",
  "galpon": "Galpón 1",
  "nivelCloroPpm": 3.75,
  "estado": "Normal",
  "fechaHora": "2026-11-26T10:15:00"
}
```

## Regla de estados

- Normal: 3.00 a 5.00 ppm
- Advertencia: 2.00 a <3.00 ppm y >5.00 a 6.00 ppm
- Crítico: <2.00 ppm o >6.00 ppm

## Datos semilla

- Granja Melipilla (G1), 4 galpones
- Granja Paine (G2), 4 galpones
- Granja Talagante (G3), 4 galpones

Total: 12 puntos de medición, cada uno con su primera medición.

## Base de datos

H2 en archivo:

- `ms-mediciones/data/mediciones.mv.db`

La carpeta `data/` está en `.gitignore`; se crea sola al iniciar el
microservicio. Si se necesita reiniciar los datos, detener el servicio y
borrar esa carpeta.

## Notas

- `AUTO_SERVER=TRUE` permite abrir la consola H2 mientras el servicio está
  corriendo.
- Para producción se recomienda cambiar H2 por MySQL/PostgreSQL y usar HTTPS.
- La app usa HTTP local (`usesCleartextTraffic`) solo para pruebas; el
  release final debería apuntar a HTTPS.
