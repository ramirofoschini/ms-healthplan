# Deploy a producción (gratis)

Hosting: **Neon** (PostgreSQL gestionado, free, sin vencimiento) + **Render** (web service Docker, free).

## 1. Base de datos en Neon

1. Crear cuenta en https://neon.tech y un proyecto (elegí la región más cercana).
2. En *Connection Details* anotá: host, database, user, password.
3. Armá la URL JDBC (Neon **exige SSL**):
   ```
   jdbc:postgresql://<host>/<database>?sslmode=require
   ```

## 2. App en Render

**Opción A — Blueprint (usa el `render.yaml` del repo):**
1. https://render.com → **New → Blueprint** → conectá el repo de GitHub.
2. Render detecta `render.yaml` y te pide las variables marcadas como secretas:
   | Variable         | Valor                                            |
   |------------------|--------------------------------------------------|
   | `DB_URL`         | la URL JDBC de Neon (con `?sslmode=require`)      |
   | `DB_USERNAME`    | usuario de Neon                                  |
   | `DB_PASSWORD`    | password de Neon                                 |
   | `ADMIN_PASSWORD` | una contraseña fuerte para el admin              |

   (`SPRING_PROFILES_ACTIVE=prod` ya viene fijado en el blueprint.)
3. **Create** → Render compila el `Dockerfile` y levanta la app.

**Opción B — Manual:**
- New → **Web Service** → repo → Runtime: **Docker** → Branch: **develop** → Plan: **Free**.
- En *Environment*, agregá las mismas variables (incluyendo `SPRING_PROFILES_ACTIVE=prod`).

## 3. Verificar

- Render te da una URL tipo `https://ms-healthplan-xxxx.onrender.com`.
- Al arrancar, **Flyway** crea el esquema en Neon y `DataInitializer` siembra el admin.
- Entrá a `https://<tu-url>/swagger-ui/index.html` y autenticá con `admin` / la `ADMIN_PASSWORD` que cargaste.

## Notas

- **Deploy automático**: cada merge a `develop` redepliega (el CI ya mergea las ramas `feat-*` que pasan el build).
- **Cold start**: el plan free de Render duerme tras ~15 min de inactividad; la primera request lo despierta (~30s).
- **HTTPS**: lo provee Render automáticamente.
- **Secretos**: nunca van al repo; viven como variables de entorno en Render.
- **Alternativa con menor latencia desde Argentina**: Fly.io tiene región São Paulo (`gru`). El `Dockerfile` sirve igual; cambia el archivo de config (`fly.toml`) y el deploy por CLI (`flyctl`).
