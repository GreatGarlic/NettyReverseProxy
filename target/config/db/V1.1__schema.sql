CREATE TABLE frontend_port_info (
  id   integer,
  port integer not null,
  primary key (id)
);

CREATE TABLE "backend_server_info" (
  "id"   integer,
  "ip"   TEXT(20) NOT NULL,
  "port" integer  NOT NULL,
  PRIMARY KEY ("id")
);