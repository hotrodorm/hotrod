-- Limitations:
-- ============

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

-- 1. Basic hierarchical select. Note:
--  * Creates the extra column level: shows the level of the node.
--  * The first select is NOT recursive and defines the root(s) of the tree.
--  * The second select is recursive and populate the remaining levels of the tree.

with recursive node (id, parent_id, name, level) as (
  select id, parent_id, name, 1 from tree_node where parent_id is null
  union
  select t.id, t.parent_id, t.name, node.level + 1
    from node join tree_node t on t.parent_id = node.id
)
select id, parent_id, name, level from node 

-- 2. Enhanced hierarchical select. Note:
--  * Adds the extra column breadcrumbs: sorts the rows and show the tree in a clearer way.
--  * Returns the root ancestor of every row.
  
with recursive node (id, parent_id, name, level, breadcrumbs, root_id) as (
  select id, parent_id, name, 1, rpad(lpad(id, 4, '0'), 100), id + 0 from tree_node where parent_id is null
  union
  select t.id, t.parent_id, t.name, node.level + 1, left(trim(node.breadcrumbs) || '.' || lpad(t.id, 4, '0'), 100), node.root_id
    from node join tree_node t on t.parent_id = node.id
)
select repeat('.  ', level - 1) || name, id, parent_id, name, level, breadcrumbs, root_id from node order by breadcrumbs
