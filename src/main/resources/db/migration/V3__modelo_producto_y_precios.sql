-- Enriquecimiento del plan como producto vendible.
-- activo se agrega con DEFAULT para no romper filas ya existentes (base local);
-- nombre y nivel_cobertura quedan nullable para el mismo motivo.
ALTER TABLE health_plan
    ADD COLUMN nombre          VARCHAR(255),
    ADD COLUMN nivel_cobertura VARCHAR(50),
    ADD COLUMN activo          BIT NOT NULL DEFAULT 1;

-- Catálogo de franjas etarias (compartido por todos los planes).
CREATE TABLE franja_etaria (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    nombre     VARCHAR(100) NOT NULL,
    edad_desde INT          NOT NULL,
    edad_hasta INT          NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

-- Tabla de precios: monto por (plan, franja) con vigencia.
CREATE TABLE precio_plan (
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    plan_id        BIGINT        NOT NULL,
    franja_id      BIGINT        NOT NULL,
    monto          DECIMAL(12,2) NOT NULL,
    vigencia_desde DATE          NOT NULL,
    vigencia_hasta DATE,
    PRIMARY KEY (id),
    CONSTRAINT fk_precio_plan_plan   FOREIGN KEY (plan_id)   REFERENCES health_plan (id),
    CONSTRAINT fk_precio_plan_franja FOREIGN KEY (franja_id) REFERENCES franja_etaria (id)
) ENGINE = InnoDB;

-- Franjas etarias estándar.
INSERT INTO franja_etaria (nombre, edad_desde, edad_hasta) VALUES
    ('0-18',  0,  18),
    ('19-25', 19, 25),
    ('26-35', 26, 35),
    ('36-45', 36, 45),
    ('46-55', 46, 55),
    ('56-65', 56, 65),
    ('66+',   66, 120);
