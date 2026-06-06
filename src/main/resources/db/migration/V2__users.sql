-- Usuarios internos (agentes/vendedores) y sus roles.
CREATE TABLE user_account (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled  BIT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_user_account_username UNIQUE (username)
) ENGINE = InnoDB;

CREATE TABLE user_account_role (
    user_account_id BIGINT      NOT NULL,
    `role`          VARCHAR(50) NOT NULL,
    CONSTRAINT fk_user_account_role_user FOREIGN KEY (user_account_id) REFERENCES user_account (id)
) ENGINE = InnoDB;
