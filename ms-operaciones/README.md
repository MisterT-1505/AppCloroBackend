# ms-operaciones (Javier)

Microservicio de usuarios, alertas y acciones correctivas. Puerto **8082**.

> Este microservicio está reservado para Javier. Benjamin ya dejó el contrato
> de API y la estructura del repositorio para que puedas crearlo sin depender
> de la app.

## 1. Crear el proyecto

Desde `Backend/`:

```bash
mvn archetype:generate ...
```

Más simple: generar en <https://start.spring.io> con:

- Project: Maven
- Language: Java
- Spring Boot: 3.5.x
- Java: 21
- Group: `com.appcloro`
- Artifact: `ms-operaciones`
- Package: `com.appcloro.operaciones`
- Dependencies: Spring Web, Spring Data JPA, Validation, H2

Descomprimir el zip en `Backend/ms-operaciones` y reemplazar el
`application.properties` por un `application.yml` con:

```yaml
server:
  port: 8082

spring:
  application:
    name: ms-operaciones
  datasource:
    url: jdbc:h2:file:./data/operaciones;AUTO_SERVER=TRUE
    driver-class-name: org.h2.Driver
    username: sa
    password: ""
  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false
  h2:
    console:
      enabled: true
      path: /h2-console
```

## 2. Entidades

- `Usuario`: id, nombre, correo, contrasenaHash, rol, granjaAsignada.
- `Alerta`: id, puntoId, granja, galpon, nivelPpm, tipoDesviacion,
  nivelGravedad, fechaHora, resuelta.
- `AccionCorrectiva`: id, alertaId, puntoId, descripcion, responsable,
  fechaHora, nuevoNivelPpm, imagenUri, validadaPorSupervisor.

Roles: `OPERARIO`, `SUPERVISOR`, `JEFATURA`.

## 3. Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Login con correo y contraseña |
| GET | `/api/usuarios` | Lista de usuarios (sin contraseña) |
| GET | `/api/alertas?granja=G1&resuelta=false` | Lista de alertas |
| GET | `/api/alertas/{id}` | Una alerta |
| POST | `/api/alertas` | Crear alerta |
| PATCH | `/api/alertas/{id}/resolver` | Marcar alerta resuelta |
| GET | `/api/acciones?puntoId=G1-P1` | Lista de acciones |
| GET | `/api/acciones/{id}` | Una acción |
| POST | `/api/acciones` | Crear acción correctiva |
| PUT | `/api/acciones/{id}` | Actualizar acción |
| DELETE | `/api/acciones/{id}` | Eliminar acción |
| PATCH | `/api/acciones/{id}/validar` | Validar por supervisor |
| GET | `/api/health` | Estado del servicio |

### Login

```json
POST /api/auth/login
{
  "correo": "operario@empresa.cl",
  "contrasena": "1234"
}
```

Respuesta:

```json
{
  "id": "U1",
  "nombre": "Juan Pérez",
  "correo": "operario@empresa.cl",
  "rol": "OPERARIO",
  "granjaAsignada": "Granja Melipilla"
}
```

Usuarios semilla sugeridos:

| Correo | Contraseña | Rol | Granja |
|---|---|---|---|
| operario@empresa.cl | 1234 | OPERARIO | Granja Melipilla |
| supervisor@empresa.cl | 1234 | SUPERVISOR | Granja Melipilla |
| jefatura@empresa.cl | 1234 | JEFATURA | Todas las granjas |

> Para la demo se puede usar BCrypt (`spring-security-crypto`) para no guardar
> contraseñas en texto plano. Si se usa texto plano, dejarlo documentado en el
> README y no reutilizar esas claves fuera del proyecto.

## 4. Tareas de Javier

- [ ] Crear el proyecto Spring Boot.
- [ ] Entidades + repositorios.
- [ ] Login y usuarios semilla.
- [ ] CRUD de alertas y acciones.
- [ ] Resolver alerta y validar acción.
- [ ] Validaciones y manejo de errores.
- [ ] CORS para pruebas locales.
- [ ] Pruebas con JUnit 5 + MockMvc.
- [ ] Integrar Login, Alertas y Registro de Acción en la app.
- [ ] Pruebas de ViewModels/Repositorios de la app (MockK + coroutines-test).
- [ ] README y evidencias de Trello.

## 5. Probar

```bash
cd ms-operaciones
./mvnw spring-boot:run
```

```text
http://localhost:8082/api/alertas
http://localhost:8082/api/health
http://localhost:8082/h2-console
JDBC URL: jdbc:h2:file:./data/operaciones
Usuario: sa
Contraseña: (vacía)
```
