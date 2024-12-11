create table account (
  id int identity primary key not null,
  name varchar(20) not null,
  type varchar(3) not null,
  balance int not null
)

insert into account (name, type, balance) values ('1010', 'CHK', 100)

insert into account (name, type, balance) values ('2055', 'SAV', 200)

insert into account (name, type, balance) values ('1072', 'CHK', 500)
  
-- Sybase ASE 16 does not implement sequences
