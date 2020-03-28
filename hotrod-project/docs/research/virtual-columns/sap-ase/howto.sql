-- There are no hierarchical quesries in SAP ASE. SAP Anywhere (different edition) may have them.

create database hotrod

sp_dboption hotrod, 'select into', true

use hotrod

create table tree_node (
  id integer primary key not null,
  name varchar(20) not null,
  uname compute upper(name), -- non-materialized virtual column
  parent_id integer null
)

insert into tree_node (id, name, parent_id) values (1, 'root', null)

insert into tree_node (id, name, parent_id) values (2, '1a', 1)

insert into tree_node (id, name, parent_id) values (3, '1b', 1)

insert into tree_node (id, name, parent_id) values (4, '1b1', 3)

insert into tree_node (id, name, parent_id) values (5, '1b2', 3)

insert into tree_node (id, name, parent_id) values (6, '1b1a', 4)

insert into tree_node (id, name, parent_id) values (7, '1a7', 2)

-- Virtual (computed) columns cannot be included on INSERTs. The following SQL fails:
--   insert into tree_node (id, name, parent_id, uname) values (2, '1a', 1, 'Peter')
--     Error: Cannot directly insert into or update computed column 'uname'.
--     SQLState:  ZZZZZ
--     ErrorCode: 11067

-- Virtual (computed) columns cannot be updated. The following SQL fails:
--   update tree_node set uname = 'Johnny' where id = 3
--     Error: Cannot directly insert into or update computed column 'uname'.
--     SQLState:  ZZZZZ
--     ErrorCode: 11067

-- Index on virtual columns
-- To create an index on a virtual column, the column must be materailized

create table tree_node (
  id integer primary key not null,
  name varchar(20) not null,
  uname compute upper(name) materialized, -- materialized virtual column
  parent_id integer null
)

create index ix_tn4 on tree_node (uname)
