-- Titulares / prospectos.
CREATE TABLE customer (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    document_type   VARCHAR(30)  NOT NULL,
    document_number VARCHAR(30)  NOT NULL,
    birth_date      DATE         NOT NULL,
    email           VARCHAR(255),
    phone           VARCHAR(50),
    PRIMARY KEY (id),
    CONSTRAINT uk_customer_document UNIQUE (document_type, document_number)
) ENGINE = InnoDB;

-- Integrantes del grupo familiar (dependen del cliente titular).
CREATE TABLE dependent (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    customer_id  BIGINT       NOT NULL,
    relationship VARCHAR(30)  NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    birth_date   DATE         NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_dependent_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
) ENGINE = InnoDB;
