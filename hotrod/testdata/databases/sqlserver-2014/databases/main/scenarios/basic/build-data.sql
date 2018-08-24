    
insert into federal_branch (id, name) values (101, 'Virginia');
insert into federal_branch (id, name) values (102, 'Maryland');
insert into federal_branch (id, name) values (103, 'Delaware');
insert into transaction2 (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234001, 1, 'Time 001', 100, 101);
insert into transaction2 (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234004, 2, 'Time 004', 150, 103);
insert into transaction2 (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234005, 4, 'Time 005', 160, 102);
  
insert into account (current_balance, name, created_on) values (120, 'CHK1004', '2015-12-10');
insert into account (current_balance, name, created_on) values (500, 'SAV7018', '2015-12-08');
insert into account (current_balance, name, created_on) values (45, 'CHK2301', '2015-9-1');
   
-- =====
-- Types
-- =====

insert into types_other (id) values (1);
update types_other set hie1 = hierarchyid::Parse('/2/1/') where id = 1;
update types_other set uni1 = 'A972C577-DFB0-064E-1189-0154C99310DAAC12' where id = 1;
update types_other set xml1 = '<abc/>' where id = 1;
update types_other set geog1 = geography::Point(47.65100, -122.34900, 4326) where id = 1;
update types_other set geom1 = geometry::STGeomFromText('LINESTRING (100 100, 20 180, 180 180)', 0) where id = 1;



insert into "&General"."<Stock$"."&Price%" (id, value) values (101, 1234.56);
insert into "&General"."<Stock$"."&Price%" (id, value) values (102, 1773.45);

insert into "car#part$Price" ("part#", "price$dollar", "%discount") values (123, 456, 789);
insert into "car#part$Price" ("part#", "price$dollar", "%discount") values (101, 202, 303);

INSERT INTO schema2.house (id,name) VALUES (1,'House 1');
INSERT INTO schema2.house (id,name) VALUES (2,'House 2');

INSERT INTO house (address,price) VALUES ('123 Maple St', 150000);
INSERT INTO house (address,price) VALUES ('456 Oak St', 320000);
INSERT INTO house (address,price) VALUES ('789 Columbia Pike', 250000);


insert into schema2.house (id, name) values (70, 'Main Source');
insert into schema2.house (id, name) values (71, 'Secondary Source');
  
insert into schema2.account_alert (raised_at, account_id, house_id) values ('2018-01-01 17:56:23', 1, 70);
insert into schema2.account_alert (raised_at, account_id, house_id) values ('2018-01-02 18:12:34', 1, 70);
insert into schema2.account_alert (raised_at, account_id, house_id) values ('2018-01-20 20:59:59', 2, 71);
insert into schema2.account_alert (raised_at, account_id, house_id) values ('2018-01-21 08:05:12', 3, 71);

insert into "&General".dbo.atelier (id, name) values (123, 'Atelier 1');
insert into "&General".dbo.atelier (id, name) values (456, 'Atelier 2');

disable trigger employee_state_read_only on employee_state; 
insert into employee_state (id, description) values (1, 'Enrolled');
insert into employee_state (id, description) values (2, 'Accepted_OK');
insert into employee_state (id, description) values (3, 'Pending Notification');
insert into employee_state (id, description) values (4, 'Rejected, but can Reapply!');
enable trigger employee_state_read_only on employee_state;












