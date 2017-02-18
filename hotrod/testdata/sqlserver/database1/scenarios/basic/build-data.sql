    
insert into federal_branch (id, name) values (101, 'Virginia');
insert into federal_branch (id, name) values (102, 'Maryland');
insert into federal_branch (id, name) values (103, 'Delaware');
insert into transaction2 (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234001, 1, 'Time 001', 100, 101);
insert into transaction2 (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234004, 2, 'Time 004', 150, 103);
insert into transaction2 (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234005, 4, 'Time 005', 160, 102);
  
insert into account (id, current_balance, name, created_on) values (1234001, 120, 'CHK1004', '2015-12-10');
insert into account (id, current_balance, name, created_on) values (1234004, 500, 'SAV7018', '2015-12-08');
insert into account (id, current_balance, name, created_on) values (1234005, 45, 'CHK2301', '2015-9-1');
   
-- =====
-- Types
-- =====

insert into types_other (id) values (1);
update types_other set hie1 = hierarchyid::Parse('/2/1/') where id = 1;
update types_other set uni1 = 'A972C577-DFB0-064E-1189-0154C99310DAAC12' where id = 1;
update types_other set xml1 = '<abc/>' where id = 1;
update types_other set geog1 = geography::Point(47.65100, -122.34900, 4326) where id = 1;
update types_other set geom1 = geometry::STGeomFromText('LINESTRING (100 100, 20 180, 180 180)', 0) where id = 1;
