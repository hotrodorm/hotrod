create table account (
  id int primary key not null,
  balance int not null
);

insert into account (id, balance) values
  (123, 100),
  (456, 200);
