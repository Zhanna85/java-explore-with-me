DROP TABLE IF EXISTS users, events, categories, comments, requests CASCADE;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  email varchar(254) NOT NULL UNIQUE,
  name varchar(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  annotation varchar(2000) NOT NULL,
  category_id bigint NOT NULL REFERENCES categories (id) ON DELETE RESTRICT,
  description varchar(7000) NOT NULL,
  event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  location_lat FLOAT NOT NULL,
  location_lon FLOAT NOT NULL,
  paid boolean NOT NULL DEFAULT false,
  participant_limit int NOT NULL DEFAULT 0,
  request_moderation boolean NOT NULL DEFAULT true,
  title varchar(120) NOT NULL,
  initiator_id bigint NOT NULL REFERENCES users (id),
  state varchar(255) NOT NULL DEFAULT 'PENDING',
  created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  published_on TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY UNIQUE NOT NULL,
  event_id bigint NOT NULL REFERENCES events (id),
  requester_id bigint NOT NULL REFERENCES users (id),
  status varchar(255) NOT NULL DEFAULT 'PENDING',
  created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT requests_pk PRIMARY KEY (requester_id, event_id)
);

CREATE TABLE IF NOT EXISTS compilations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  pinned boolean NOT NULL DEFAULT true,
  title varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  text varchar(2000),
  event_id bigint NOT NULL REFERENCES events (id),
  author_id bigint NOT NULL REFERENCES users (id),
  created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);