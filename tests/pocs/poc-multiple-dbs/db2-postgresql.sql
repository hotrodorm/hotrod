create table invoice (
  id int primary key,
  client varchar(50),
  amount decimal(12, 2)
);

insert into invoice (id, client, amount) values (1015, 'Acme', 1500);
