DROP TABLE IF EXISTS master;

CREATE TABLE master (
  id  VARCHAR(20)  PRIMARY KEY,
  hold_mark VARCHAR(100),
  balance decimal(18,2) ,
  status INT DEFAULT 0,
  cif VARCHAR(12) DEFAULT NULL
);

INSERT INTO master (id, hold_mark, balance, status, cif) VALUES
  ('10-110-1234', '', 0, 0, '20871948'),
  ('10-110-4567', '', 0, 0, '70745361'),
  ('10-110-7777', '', 0, 0, '54317103');
  
----
DROP TABLE IF EXISTS temp;

CREATE TABLE temp (
  id  VARCHAR(20)  PRIMARY KEY,
  hold_mark VARCHAR(100),
  status INT DEFAULT 0
);

----
DROP TABLE IF EXISTS journal;

CREATE TABLE journal (
  id  VARCHAR(100),
  seq INT,
  balance decimal(18,2) ,
  status INT DEFAULT 0,
  PRIMARY KEY (id, seq)
);  
  
----
DROP TABLE IF EXISTS service_log0;
CREATE TABLE service_log0 (
  id  VARCHAR(100),
  seq INT,
  ts TIMESTAMP,
  status INT,
  input VARCHAR(2000),
  output VARCHAR(2000),
  before VARCHAR(2000),
  after VARCHAR(2000),
  PRIMARY KEY (id, seq)
);
----
DROP TABLE IF EXISTS service_log1;
CREATE TABLE service_log1 (
  id  VARCHAR(100),
  seq INT,
  ts TIMESTAMP,
  status INT,
  input VARCHAR(2000),
  output VARCHAR(2000),
  before VARCHAR(2000),
  after VARCHAR(2000),
  PRIMARY KEY (id, seq)
);
----
DROP TABLE IF EXISTS service_log2;
CREATE TABLE service_log2 (
  id  VARCHAR(100),
  seq INT,
  ts TIMESTAMP,
  status INT,
  input VARCHAR(2000),
  output VARCHAR(2000),
  before VARCHAR(2000),
  after VARCHAR(2000),
  PRIMARY KEY (id, seq)
);
