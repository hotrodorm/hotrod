create table account (
  id int identity primary key not null,
  name varchar(20) not null,
  type varchar(3) not null,
  balance int not null
);

insert into account (name, type, balance) values
  ('1010', 'CHK', 100),
  ('2055', 'SAV', 200),
  ('1072', 'CHK', 500);
  
-- To test a sequence the IDENTITY needs to be disabled

create table account (
  id int primary key not null,
  name varchar(20) not null,
  type varchar(3) not null,
  balance int not null
);
  
create sequence seq_account start with 1000;


