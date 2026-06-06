-- Esquema inicial: tabla de planes de obra social.
-- Refleja el estado ya existente (creado antes por ddl-auto=update). En bases
-- nuevas (CI / producción) Flyway la aplica; en la base local existente queda
-- cubierta por el baseline (spring.flyway.baseline-on-migrate=true).
CREATE TABLE health_plan (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    clinics       VARCHAR(255),
    comments      VARCHAR(255),
    document_path VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE = InnoDB;
