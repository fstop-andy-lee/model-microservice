drop table if exists black_list;
create table black_list (
  id varchar(40) primary key,
  name  varchar(200),
  screen_type int default 0
);


---- for black list
-- SELECT id FROM black_list WHERE position('MARK' in name)>0;
-- SELECT id FROM black_list WHERE strpos(name, 'MARK') > 0;



