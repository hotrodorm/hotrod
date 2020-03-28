-- There are no hierarchical quesries in SAP ASE. SAP Anywhere (different edition) may have them.

create database hotrod

sp_dboption hotrod, 'select into', true

use hotrod

create table tree_node (
  id integer primary key not null,
  name varchar(20) not null,
  parent_id integer null
)

insert into tree_node (id, name, parent_id) values (1, 'root', null)

insert into tree_node (id, name, parent_id) values (2, '1a', 1)

insert into tree_node (id, name, parent_id) values (3, '1b', 1)

insert into tree_node (id, name, parent_id) values (4, '1b1', 3)

insert into tree_node (id, name, parent_id) values (5, '1b2', 3)

insert into tree_node (id, name, parent_id) values (6, '1b1a', 4)

insert into tree_node (id, name, parent_id) values (7, '1a7', 2)

