-- === DB2 10.5 ===

-- ================ Using CTEs ========  
  
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
    select id, referred_by_id, name as client, name from shopper where substr(name, 1, 1) = 'M'
    union all
    select s.id, s.referred_by_id, r.client, s.name from referrer r, shopper s where s.id = r.referred_by_id
)
select * from referrer where referred_by_id is null order by client;

-- Recursive WITH clause:
--  * Must have a query name.
--  * Must have Column aliases.
--  * Must use UNION ALL to separate the anchor member from the recursive member.
--  * The anchor member cannot reference the query name.
--  * The recursive member must include a single reference to the query name. However, this reference 
--    cannot take the form of an explicit join; write an old-fashioned join using FROM/WHERE. 
  
  
-- ====== Old fashioned hierarchical queries ===

-- By default DB2 has them disabled. If you try a hierarchical query you'll receive an error like:
--     Error: DB2 SQL Error: SQLCODE=-104, SQLSTATE=42601, SQLERRMC=id;1
--     connect by prior;<space>,
-- To list current DB2 settings:
--     db2set -all
-- To enable hierarchical queries:
--     db2set -g DB2_COMPATIBILITY_VECTOR=8

create table tree_node (
  id integer primary key not null,
  name varchar(20) not null,
  parent_id integer
);

insert into tree_node (id, name, parent_id) values (1, 'root', null);
insert into tree_node (id, name, parent_id) values (2, '1a', 1);
insert into tree_node (id, name, parent_id) values (3, '1b', 1);
insert into tree_node (id, name, parent_id) values (4, '1b1', 3);
insert into tree_node (id, name, parent_id) values (5, '1b2', 3);
insert into tree_node (id, name, parent_id) values (6, '1b1a', 4);
insert into tree_node (id, name, parent_id) values (7, '1a7', 2);

-- 1. Basic hierarchical select. Note:
--  * uses the pseudo column 'level'
--  * 'start with <condition>' equivalent to 'where'; select the first level rows
--  * 'connect by prior' establishes the relation between the current (parent) level and next (children) one

select id, name, parent_id, level 
  from tree_node
  start with id = 1
  connect by prior id = parent_id

-- 2. Enhanced hierarchical select. Note:
--  * For every row in the hierarchy, the 'connect_by_root <columns-expression>' operator 
--    returns the expression for the row's root ancestor.
--  * The 'sys_connect_by_path(<column-expressio>, <prefix>)' function return a varchar with 
--    the path from root.
--  * The 'order siblings by <columns>' specify how to order same level rows.
  
select id, name, parent_id, level, 
    connect_by_root (id || '-' || name),
    sys_connect_by_path(name, ' > ')
  from tree_node
  start with id in (2, 3)
  connect by prior id = parent_id
  order siblings by name desc

  
  
  