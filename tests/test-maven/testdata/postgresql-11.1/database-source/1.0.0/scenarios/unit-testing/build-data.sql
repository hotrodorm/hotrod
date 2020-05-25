insert into APPLICATION_CONFIG (config_id, config_name, config_value)
  values (6, 'dummy var', null);

insert into APPLICATION_CONFIG 
  values ((select max(config_id+1) from APPLICATION_CONFIG), 
    'param1', '');

insert into APPLICATION_CONFIG 
  values ((select max(config_id+1) from APPLICATION_CONFIG), 
    'param2', '/onevolume');

insert into APPLICATION_CONFIG 
  values ((select max(config_id+1) from APPLICATION_CONFIG), 
    'param3', 'uploaded/one');

insert into APPLICATION_CONFIG 
  values ((select max(config_id+1) from APPLICATION_CONFIG), 
    'param4', 'uploaded/two/');
    
insert into federal_branch (id, name) values (101, 'Virginia');
insert into federal_branch (id, name) values (102, 'Maryland');
insert into federal_branch (id, name) values (103, 'Delaware');

insert into account (id, current_balance, name, type, active, created_on) values (1234001, 120, 'CHK1004', 'CHK', 1, current_timestamp - interval '15 day');
insert into account (id, current_balance, name, type, active, created_on) values (1234004, 500, 'SAV7018', 'SAV', 1, current_timestamp - interval '7 day');
insert into account (id, current_balance, name, type, active, created_on) values (1234005, 45, 'CHK2301', 'CHK', 0, current_timestamp - interval '45 day');
  
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234001, 100, 'Time 001', 100, 101);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234004, 101, 'Time 004', 150, 103);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234005, 102, 'Time 005', 160, 102);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234005, 115, 'Time 007', -110, 103);
  
insert into config_values(node, cell, name, verbatim) values(10, 100, 'prop1', 'value1');
insert into config_values(node, cell, name, verbatim) values(10, 101, 'prop2', 'value2');
insert into config_values(node, cell, name, verbatim) values(20, 100, 'prop3', 'value3');
  
insert into vehicle_type (id, description) values (10, 'Sedan');
insert into vehicle_type (id, description) values (11, 'Truck');

insert into vehicle (name, mileage, vtype, version_number) values ('Volkswagen', 10000, 10, 32765);
insert into vehicle (name, mileage, vtype, version_number) values ('Ford', 25000, 11, 15);

insert into properties (application, name, prop_value) values ('app01', 'key1', 'value1');

insert into state_branch (id, name) values (101, 'Fairfax');

insert into client (id, national_id, name,              prop_name, referrer_id, friend_id, group_account_id, branch_id)
 values            (12, 10,          'Peter Cantropus', 'key1',    12,          null,        1234001,          101);

-- enum

alter table employee_state disable trigger employee_state_read_only;
insert into employee_state (id, description) values (1, 'Enrolled');
insert into employee_state (id, description) values (2, 'Accepted_OK');
insert into employee_state (id, description) values (3, 'Pending Notification');
insert into employee_state (id, description) values (4, 'Rejected, but can Reapply!');
alter table employee_state enable trigger employee_state_read_only;

insert into employee (id, name, state_id, hired_on) values (101, 'Peter', 2, '2017-08-01');
insert into employee (id, name, state_id, hired_on) values (102, 'Alice', 1, '2017-07-15');
insert into employee (id, name, state_id, hired_on) values (103, 'Donna', 4, '2017-08-03');

-- complex names

insert into "car#part$Price" ("part#", "price$dollar", "%discount") values (123, 456, 789);
insert into "car#part$Price" ("part#", "price$dollar", "%discount") values (101, 202, 303);

INSERT INTO schema2.house (id,name) VALUES (1,'House 1');
INSERT INTO schema2.house (id,name) VALUES (2,'House 2');

INSERT INTO house (address,price) VALUES ('123 Maple St', 150000);
INSERT INTO house (address,price) VALUES ('456 Oak St', 320000);
INSERT INTO house (address,price) VALUES ('789 Columbia Pike', 250000);


insert into schema2.house (id, name) values (70, 'Main Source');
insert into schema2.house (id, name) values (71, 'Secondary Source');
  
insert into schema2.account_alert (raised_at, account_id, house_id) values ('2018-01-01 17:56:23', 1234001, 70);
insert into schema2.account_alert (raised_at, account_id, house_id) values ('2018-01-02 18:12:34', 1234001, 70);
insert into schema2.account_alert (raised_at, account_id, house_id) values ('2018-01-20 20:59:59', 1234004, 71);
insert into schema2.account_alert (raised_at, account_id, house_id) values ('2018-01-21 08:05:12', 1234004, 71);

insert into types_binary (bol1, bin1) values (true, '\x3135');
insert into types_binary (bol1, bin1) values (false, '\xd0ff');
insert into types_binary (bol1, bin1) values (true, '\x9974');
insert into types_binary (bol1, bin1) values (false, '\x3135');

insert into types_other (uui1) values ('33bb9554-c616-42e6-a9c6-88d3bba4221c');
insert into types_other (uui1) values ('5af75c52-cb8e-44fb-93c8-1d46da518ee6');

insert into product (id, name, "Name") values (123, 'keyboard', '104-key Keyboard');
insert into product (id, name, "Name") values (456, 'monitor', '27-inch monitor');

insert into item (id, description, price, created_on, active, icon, store_code) values (1, 'Sweater', 123.45, '2018-01-21 08:05:12', true, null, '11bb9554-c616-42e6-a9c6-88d3bba4221c'); 
insert into item (id, description, price, created_on, active, icon, store_code) values (2, 'Pants', 789.01, '2018-02-22 12:34:56', false, null, '22779554-c616-42e6-a9c6-88d3bba4221c'); 
insert into item (id, description, price, created_on, active, icon, store_code) values (3, 'Jacker', 123.45, '2018-03-21 03:03:03', true, null, '33bb9554-c616-42e6-a9c6-88d3bba4221c'); 
insert into item (id, description, price, created_on, active, icon, store_code) values (4, 'Hat', 123.45, '2018-04-21 04:04:04', true, null, '44bb9554-c616-42e6-a9c6-88d3bba4221c'); 


insert into event (id, name, status) values (1, 'Recorded', 1);
insert into event (id, name, status) values (2, 'Deleted', 0);
insert into event (id, name, status) values (3, 'Confirmed', 1);
insert into event (id, name, status) values (4, 'Assigned', 1);
insert into event (id, name, status) values (5, 'Fixed', 1);


