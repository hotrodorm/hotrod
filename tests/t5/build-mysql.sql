create table account (
  id int primary key not null AUTO_INCREMENT,
  name varchar(20) not null,
  type varchar(3) not null,
  balance int not null
);

insert into account (id, name, type, balance) values
  (123, '1010', 'CHK', 100),
  (456, '2055', 'SAV', 200),
  (789, '1072', 'CHK', 500);
