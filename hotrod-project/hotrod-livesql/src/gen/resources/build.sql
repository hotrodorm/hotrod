create table account (
  id int,
  balance int,
  type char(3),
  active int
);
  
insert into account (id, balance, type, active) values
  (1, 100, 'SAV', 1),
  (2, 200, 'CHK', 1),
  (3, 300, 'SAV', 0),
  (4, 400, 'CHK', 0);
  