CREATE SEQUENCE hibernate_sequence START 100;

CREATE TABLE t_number (
      id          BIGSERIAL PRIMARY KEY,
      value       TEXT UNIQUE NOT NULL
);

INSERT INTO t_number (value)
VALUES
       ('Х001АХ 116 RUS'),
       ('Х051РХ 116 RUS');