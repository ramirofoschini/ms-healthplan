-- Enriquecimiento del plan como producto vendible.
-- active se agrega con DEFAULT para no romper filas ya existentes;
-- name y coverage_level quedan nullable por el mismo motivo.
ALTER TABLE health_plan
    ADD COLUMN name           VARCHAR(255),
    ADD COLUMN coverage_level VARCHAR(50),
    ADD COLUMN active         BIT NOT NULL DEFAULT 1;

-- Catálogo de franjas etarias (compartido por todos los planes).
CREATE TABLE age_band (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    age_from INT          NOT NULL,
    age_to   INT          NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

-- Tabla de precios: monto por (plan, franja) con vigencia.
CREATE TABLE plan_price (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    plan_id     BIGINT        NOT NULL,
    age_band_id BIGINT        NOT NULL,
    amount      DECIMAL(12,2) NOT NULL,
    valid_from  DATE          NOT NULL,
    valid_to    DATE,
    PRIMARY KEY (id),
    CONSTRAINT fk_plan_price_plan     FOREIGN KEY (plan_id)     REFERENCES health_plan (id),
    CONSTRAINT fk_plan_price_age_band FOREIGN KEY (age_band_id) REFERENCES age_band (id)
) ENGINE = InnoDB;

-- Franjas etarias estándar.
INSERT INTO age_band (name, age_from, age_to) VALUES
    ('0-18',  0,  18),
    ('19-25', 19, 25),
    ('26-35', 26, 35),
    ('36-45', 36, 45),
    ('46-55', 46, 55),
    ('56-65', 56, 65),
    ('66+',   66, 120);
