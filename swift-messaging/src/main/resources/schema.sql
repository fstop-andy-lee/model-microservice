DROP TABLE IF EXISTS master;
CREATE TABLE master (
  id  VARCHAR(40)  PRIMARY KEY,
  hold_mark VARCHAR(100),
  balance decimal(18,2) ,
  status INT DEFAULT 0,
  cif VARCHAR(12) DEFAULT NULL
);
----
DROP TABLE IF EXISTS swift_message;
CREATE TABLE swift_message (
  id  VARCHAR(40)  PRIMARY KEY,
  status int default 0,
  msg VARCHAR DEFAULT NULL
);