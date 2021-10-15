--liquibase formatted sql

--changeset author:morozkin-ai:create-number-table
CREATE TABLE t_number (
    id          BIGSERIAL PRIMARY KEY,
    value       TEXT UNIQUE NOT NULL
);
--rollback DROP TABLE t_number