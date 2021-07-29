----
DROP TABLE IF EXISTS temp;

CREATE TABLE temp (
  id  VARCHAR(20)  PRIMARY KEY,
  hold_mark VARCHAR(100),
  status INT DEFAULT 0
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
