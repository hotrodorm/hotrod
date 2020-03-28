-- === SQL Server 2014 ===

drop table shopper;

create table shopper (
  id int primary key not null,
  name varchar(20) not null,
  referred_by_id int,
  constraint fk1 foreign key (referred_by_id) references shopper (id)
);

insert into shopper (id, name, referred_by_id) values (1, 'Peter', null);
insert into shopper (id, name, referred_by_id) values (2, 'Anna', 1);
insert into shopper (id, name, referred_by_id) values (3, 'Mary', null);
insert into shopper (id, name, referred_by_id) values (4, 'John', 1);
insert into shopper (id, name, referred_by_id) values (5, 'Donna', 3);
insert into shopper (id, name, referred_by_id) values (6, 'Michael', 4);

with referrer (id, referred_by_id, client, original) as (
    select id, referred_by_id, name as client, name from shopper where substring(name, 1, 1) = 'M'
    union all
    select s.id, s.referred_by_id, r.client, s.name from referrer r join shopper s on s.id = r.referred_by_id
)
select * from referrer where referred_by_id is null order by client;

-- Recursive WITH clause:
--  * Must have a query name.
--  * Column aliases are optional. When not specified they are defined by the anchor member.
--  * Must use UNION ALL to separate the anchor member from the recursive member.
--  * The anchor member cannot reference the query name.
--  * The recursive member must include a single reference to the query name.


