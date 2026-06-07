# --- Etapa de build: compila y empaqueta el jar ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Cachea dependencias: si no cambia el pom, no se vuelven a bajar
COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline
COPY src ./src
# Los tests corren en el CI; acá solo empaquetamos
RUN mvn -B -ntp clean package -DskipTests

# --- Etapa de runtime: imagen liviana solo con el JRE ---
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Informativo; la app escucha en $PORT (lo inyecta la plataforma) o 8081
EXPOSE 8081
# MaxRAMPercentage hace que la heap respete la memoria del contenedor (planes free chicos)
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
