
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
--  * Creates the extra column level: shows the level of the node.
--  * The first select is NOT recursive and defines the root(s) of the tree.
--  * The second select is recursive and populate the remaining levels of the tree.
--  * Cannot create views using hierarchical queries.

with node (id, parent_id, name, level) as (
  select id, parent_id, name, 1 from tree_node where parent_id is null
  union all
  select t.id, t.parent_id, t.name, a.level + 1
    from node a join tree_node t on t.parent_id = a.id
)
select id, parent_id, name, level from node 

-- 2. Enhanced hierarchical select. Note:
--  * Adds the extra column breadcrumbs: sorts the rows and show the tree in a clearer way.
--  * Returns the root ancestor of every row.
  
with node (id, parent_id, name, level, breadcrumbs, root_id) as (
  select id, parent_id, name, 1, to_char(id, '0000'), id from tree_node where parent_id is null
  union all
  select t.id, t.parent_id, t.name, a.level + 1, a.breadcrumbs || '.' || to_char(t.id, '0000'), a.root_id
    from node a join tree_node t on t.parent_id = a.id
)
select repeat('.  ', level - 1) || name, id, parent_id, name, level, breadcrumbs, root_id from node;
