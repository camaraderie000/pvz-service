CREATE TABLE users (
                       id            uuid PRIMARY KEY,
                       email         text NOT NULL UNIQUE,
                       password_hash text NOT NULL,
                       role          text NOT NULL CHECK (role IN ('employee', 'moderator'))
);

CREATE TABLE pvz (
                     id                uuid PRIMARY KEY,
                     registration_date timestamptz NOT NULL DEFAULT now(),
                     city              text NOT NULL CHECK (city IN ('Москва', 'Санкт-Петербург', 'Казань'))
);

CREATE TABLE receptions (
                            id        uuid PRIMARY KEY,
                            date_time timestamptz NOT NULL DEFAULT now(),
                            pvz_id    uuid NOT NULL REFERENCES pvz (id),
                            status    text NOT NULL CHECK (status IN ('in_progress', 'close'))
);

CREATE UNIQUE INDEX one_open_reception ON receptions (pvz_id) WHERE status = 'in_progress';

CREATE TABLE products (
                          id           uuid PRIMARY KEY,
                          seq          bigint GENERATED ALWAYS AS IDENTITY,
                          date_time    timestamptz NOT NULL DEFAULT now(),
                          type         text NOT NULL CHECK (type IN ('электроника', 'одежда', 'обувь')),
                          reception_id uuid NOT NULL REFERENCES receptions (id)
);
