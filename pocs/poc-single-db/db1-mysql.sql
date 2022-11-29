create table ingredient (
  id int primary key,
  name varchar(50),
  in_use boolean
);

insert into ingredient (id, name, in_use) values (123, 'Cabbage', true);
