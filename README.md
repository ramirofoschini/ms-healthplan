# ms-healthplan

Herramienta interna para **tasar y vender planes de obra social**. Permite a los agentes/vendedores gestionar planes y sus precios, cargar clientes con su grupo familiar y **simular cotizaciones** según la edad de cada integrante.

> Proyecto en evolución: nació como un CRUD de ejemplo y se está refactorizando, de a poco, hacia una arquitectura escalable con buenas prácticas.

## Stack

- **Java 17** · **Spring Boot 3.0.2**
- Spring Web (REST)
- Spring Data JPA + **PostgreSQL**
- **Flyway** (versionado del esquema)
- Spring Security (HTTP Basic, usuarios en BD con roles, BCrypt)
- springdoc-openapi (Swagger UI)
- Lombok · Maven

## Requisitos

- JDK 17 o superior
- PostgreSQL corriendo en `localhost:5432` (o usá el `docker-compose.yml` incluido: `docker compose up -d`)
- Maven (o el wrapper del IDE)

## Configuración

La aplicación lee la configuración de variables de entorno, con *defaults* pensados solo para desarrollo local:

| Variable        | Default                                    | Descripción                       |
|-----------------|--------------------------------------------|-----------------------------------|
| `DB_URL`        | `jdbc:postgresql://localhost:5432/health_plan` | URL de la base de datos       |
| `DB_USERNAME`   | `postgres`                                 | Usuario de la base                |
| `DB_PASSWORD`   | `1234`                                     | Contraseña de la base             |
| `ADMIN_USER`    | `admin`                                    | Usuario admin inicial             |
| `ADMIN_PASSWORD`| `1234`                                     | Contraseña del admin inicial      |

> En producción definí estas variables por entorno. **No se versionan credenciales.**

## Base de datos

1. Levantar PostgreSQL. La forma más rápida es el compose incluido (crea la base `health_plan` con usuario `postgres`/`1234`):
   ```bash
   docker compose up -d
   ```
   O, si tenés PostgreSQL instalado, crear la base vacía:
   ```sql
   CREATE DATABASE health_plan;
   ```
2. Al arrancar, **Flyway** aplica las migraciones de `src/main/resources/db/migration` (`V1`, `V2`, …) y `DataInitializer` siembra el usuario admin si la tabla está vacía. Hibernate corre en modo `validate` (no modifica el esquema).

## Cómo correr

```bash
mvn spring-boot:run
```

La app levanta en **`http://localhost:8081`**.

## Autenticación

Autenticación **HTTP Basic**. Usuario inicial: `admin` / `1234` (configurable por entorno).
Roles disponibles: `AGENT`, `SUPERVISOR`, `ADMIN`.

## Documentación de la API

- **Swagger UI:** http://localhost:8081/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8081/v3/api-docs

Swagger documenta únicamente los endpoints bajo `/api/**` y expone un botón *Authorize* para probar con HTTP Basic.

## Endpoints principales

| Método      | Ruta                                   | Descripción                          | Acceso              |
|-------------|----------------------------------------|--------------------------------------|---------------------|
| `GET`       | `/api/v1/age-bands`                    | Lista las franjas etarias            | autenticado         |
| `GET`       | `/api/v1/plans/{planId}/prices`        | Precios de un plan                   | autenticado         |
| `POST`      | `/api/v1/plans/{planId}/prices`        | Carga un precio                      | `ADMIN`/`SUPERVISOR`|
| `POST`/`GET`| `/api/v1/customers`                    | Alta y listado de clientes           | autenticado         |
| `GET`       | `/api/v1/customers/{id}`               | Cliente con su grupo familiar        | autenticado         |
| `POST`      | `/api/v1/customers/{id}/dependents`    | Suma un integrante                   | autenticado         |
| `POST`      | `/api/v1/quotes/simulate`              | Simula una cotización                | autenticado         |
| `GET`       | `/api/v1/health-plans`                 | Lista / busca planes                 | autenticado         |
| `POST`/`PUT`/`DELETE` | `/api/v1/health-plans[/{id}]`| ABM de planes                        | `ADMIN`/`SUPERVISOR`|

(Ver Swagger para el detalle completo de parámetros y schemas.)

### Ejemplo: simular una cotización

```http
POST /api/v1/quotes/simulate
Content-Type: application/json

{ "planId": 1, "customerId": 1 }
```

Devuelve el desglose por integrante (titular + grupo familiar) y el total.

## Arquitectura

```
controller -> service -> repository -> model
  + dto        (contrato de la API, separado de las entidades)
  + exception  (manejo centralizado de errores con @RestControllerAdvice)
```

El esquema se versiona con Flyway. Los errores se traducen a códigos HTTP correctos (404, 400, 409, 422) con un cuerpo `ErrorResponse` uniforme.

## CI/CD

GitHub Actions (`.github/workflows/auto-merge-feat.yml`): cada push a una rama `feat-*` compila con Maven contra un PostgreSQL de servicio y, si el build pasa, abre/mergea un Pull Request a `develop`.

## Roadmap

- [x] **Etapa 0 — Cimientos**: logging, manejo de errores, Flyway, seguridad con roles
- [x] **Etapa 1 — Producto**: planes + precios por franja etaria
- [x] **Etapa 2 — Clientes** y grupo familiar
- [x] **Etapa 3 — Motor de tasación** (simulación de cotización)
- [ ] **Etapa 4 — Cotización persistida** (estados, vigencia, cartera del agente)
- [ ] **Etapa 5 — Solicitud de contratación**
