-- Titulares / prospectos.
CREATE TABLE cliente (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(255) NOT NULL,
    apellido         VARCHAR(255) NOT NULL,
    tipo_documento   VARCHAR(30)  NOT NULL,
    numero_documento VARCHAR(30)  NOT NULL,
    fecha_nacimiento DATE         NOT NULL,
    email            VARCHAR(255),
    telefono         VARCHAR(50),
    PRIMARY KEY (id),
    CONSTRAINT uk_cliente_documento UNIQUE (tipo_documento, numero_documento)
) ENGINE = InnoDB;

-- Integrantes del grupo familiar (dependen del cliente titular).
CREATE TABLE integrante (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    cliente_id       BIGINT       NOT NULL,
    parentesco       VARCHAR(30)  NOT NULL,
    nombre           VARCHAR(255) NOT NULL,
    apellido         VARCHAR(255) NOT NULL,
    fecha_nacimiento DATE         NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_integrante_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (id)
) ENGINE = InnoDB;
