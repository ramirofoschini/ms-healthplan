# Deploy a producción (gratis)

Hosting: **Neon** (PostgreSQL gestionado, free, sin vencimiento) + **Render** (web service Docker, free).

## Estrategia de ramas

- **`develop`** = desarrollo. Las ramas `feat-*` se mergean acá automáticamente cuando pasan el CI.
- **`main`** = producción. Se actualiza **manualmente** promoviendo `develop`, y el deploy en Render también es **manual**.

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
- New → **Web Service** → repo → Runtime: **Docker** → Branch: **main** → Plan: **Free** → Auto-Deploy: **No**.
- En *Environment*, agregá las mismas variables (incluyendo `SPRING_PROFILES_ACTIVE=prod`).

## 3. Verificar

- Render te da una URL tipo `https://ms-healthplan-xxxx.onrender.com`.
- Al arrancar, **Flyway** crea el esquema en Neon y `DataInitializer` siembra el admin.
- Entrá a `https://<tu-url>/swagger-ui/index.html` y autenticá con `admin` / la `ADMIN_PASSWORD` que cargaste.

## Promover a producción

Cuando `develop` esté listo para publicar, promovelo a `main` y dispará el deploy:

```bash
git checkout main
git merge --ff-only develop   # develop siempre va adelantado, así que es fast-forward
git push origin main
```

Después, en el panel de Render: **Manual Deploy → Deploy latest commit**. (El blueprint tiene `autoDeploy: false`, así que pushear a `main` no despliega solo.)

## Notas

- **Deploy manual**: `main` no se redepliega solo; vos disparás el deploy desde Render cuando querés publicar.
- **Cold start**: el plan free de Render duerme tras ~15 min de inactividad; la primera request lo despierta (~30s).
- **HTTPS**: lo provee Render automáticamente.
- **Secretos**: nunca van al repo; viven como variables de entorno en Render.
- **Alternativa con menor latencia desde Argentina**: Fly.io tiene región São Paulo (`gru`). El `Dockerfile` sirve igual; cambia el archivo de config (`fly.toml`) y el deploy por CLI (`flyctl`).
