drop table if exists invoice;

create table invoice (
  id int primary key not null,
  client varchar(50),
  amount int
);

insert into invoice (id, client, amount) values (101, 'Acme', 78);
insert into invoice (id, client, amount) values (102, 'Indus', 1680);
insert into invoice (id, client, amount) values (103, 'Lotus Inc', 450);

