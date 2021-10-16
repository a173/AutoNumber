CREATE SEQUENCE hibernate_sequence START 100;

CREATE TABLE t_number (
      id          BIGSERIAL PRIMARY KEY,
      value       VARCHAR(6) NOT NULL,
      region      VARCHAR(3) NOT NULL
);

INSERT INTO t_number (value, region)
VALUES
       ('Х001АХ', '116'),
       ('Х051РХ', '116');