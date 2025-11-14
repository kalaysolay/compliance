CREATE TABLE users (
  id             UUID PRIMARY KEY,
  username       VARCHAR(64) UNIQUE NOT NULL,
  password_hash  VARCHAR(255)       NOT NULL,
  full_name      VARCHAR(128)       NOT NULL,
  role           VARCHAR(16)        NOT NULL CHECK (role IN ('USER','OFFICER')),
  enabled        BOOLEAN            NOT NULL DEFAULT TRUE,
  created_at     TIMESTAMPTZ        NOT NULL DEFAULT now()
);

CREATE TABLE issues (
  id               UUID PRIMARY KEY,
  document_number  VARCHAR(32) UNIQUE NOT NULL,
  creator_id       UUID NOT NULL REFERENCES users(id),
  issue_type       VARCHAR(16) NOT NULL CHECK (issue_type IN ('GIFT','CONFLICT','OTHER')),
  description      VARCHAR(2000) NOT NULL,
  event_date       DATE NOT NULL,
  status           VARCHAR(8) NOT NULL CHECK (status IN ('NEW','DONE')) DEFAULT 'NEW',
  approx_damage    NUMERIC(14,2),
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE gifts (
  id               UUID PRIMARY KEY,
  document_number  VARCHAR(32) UNIQUE NOT NULL,
  creator_id       UUID NOT NULL REFERENCES users(id),
  counterparty     VARCHAR(255) NOT NULL,
  estimated_value  NUMERIC(14,2) NOT NULL CHECK (estimated_value >= 0),
  currency         VARCHAR(3) NOT NULL DEFAULT 'KZT',
  justification    VARCHAR(1000) NOT NULL,
  event_date       DATE NOT NULL,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_issues_creator ON issues(creator_id);
CREATE INDEX idx_gifts_creator ON gifts(creator_id);
CREATE INDEX idx_issues_event_date ON issues(event_date);
CREATE INDEX idx_gifts_event_date ON gifts(event_date);

CREATE SEQUENCE seq_issues START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_gifts START WITH 1 INCREMENT BY 1;
