--liquibase formatted sql

--changeset author:morozkin-ai:create-number-table
CREATE TABLE t_number (
    id          BIGSERIAL PRIMARY KEY,
    value       VARCHAR(6) NOT NULL,
    region      VARCHAR(3) NOT NULL
);
--rollback DROP TABLE t_number