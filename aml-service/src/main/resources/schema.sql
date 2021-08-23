---- AML
drop schema if exists aml CASCADE;
create schema aml;

drop table if exists aml.black_list;
create table aml.black_list (
  id varchar(40) primary key,
  name  varchar(200),
  screen_type int default 0
);


---- for black list
-- SELECT id FROM black_list WHERE position('MARK' in name)>0;
-- SELECT id FROM black_list WHERE strpos(name, 'MARK') > 0;



