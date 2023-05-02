create table branch (
  id number(6) primary key not null,
  "NaMe" varchar2(20)
);

insert into branch (id, "NaMe") values (101, 'South');
insert into branch (id, "NaMe") values (102, 'East');

create table invoice (
  id number(6) primary key not null,
  amount number(6),
  branch_id number(6) references branch (id)
);

insert into invoice (id, amount, branch_id) values (10, 1500, 101);
insert into invoice (id, amount, branch_id) values (11, 2500, 101);
insert into invoice (id, amount, branch_id) values (12, 4000, 102);



