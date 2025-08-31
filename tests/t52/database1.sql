drop table if exists account;

create table account (
  id int primary key not null,
  owner varchar(50),
  balance int
);

insert into account (id, owner, balance) values (1, 'Tom', 500);
insert into account (id, owner, balance) values (2, 'Kim', 450);
insert into account (id, owner, balance) values (3, 'Sue', 830);
