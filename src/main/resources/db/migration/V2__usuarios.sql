-- Usuarios internos (agentes/vendedores) y sus roles.
CREATE TABLE usuario (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled  BIT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuario_username UNIQUE (username)
) ENGINE = InnoDB;

CREATE TABLE usuario_rol (
    usuario_id BIGINT      NOT NULL,
    rol        VARCHAR(50) NOT NULL,
    CONSTRAINT fk_usuario_rol_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE = InnoDB;
