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
