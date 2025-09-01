insert into application_config (config_id, config_name, config_value) values (6, 'dummy var', null);

insert into application_config (config_id, config_name, config_value) values (100, 'param1', '');

insert into application_config (config_id, config_name, config_value) values (100, 'param2', '/onevolume');

insert into application_config (config_id, config_name, config_value) values (100, 'param3', 'uploaded/one');

insert into application_config (config_id, config_name, config_value) values (100, 'param4', 'uploaded/two/');
    
insert into federal_branch (id, name) values (101, 'Virginia');
insert into federal_branch (id, name) values (102, 'Maryland');
insert into federal_branch (id, name) values (103, 'Delaware');

insert into account (id, current_balance, name, created_on) values (1234001, 120, 'CHK1004', '2015-12-10');
insert into account (id, current_balance, name, created_on) values (1234004, 500, 'SAV7018', '2015-12-08');
insert into account (id, current_balance, name, created_on) values (1234005, 45, 'CHK2301', '2015-9-1');
  
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234001, 1, 'Time 001', 100, 101);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234004, 2, 'Time 004', 150, 103);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234005, 4, 'Time 005', 160, 102);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234005, 15, 'Time 007', -110, 103);
  
insert into config_values(node, cell, name, verbatim) values(10, 100, 'prop1', 'value1');
insert into config_values(node, cell, name, verbatim) values(10, 101, 'prop2', 'value2');
insert into config_values(node, cell, name, verbatim) values(20, 100, 'prop3', 'value3');
  
insert into vehicle_type (id, description) values (10, 'Sedan');
insert into vehicle_type (id, description) values (11, 'Truck');

insert into vehicle_vin (num, reg) values ('DE123456', '2018-01-01'); 
insert into vehicle_vin (num, reg) values ('FG999777', '2018-02-07'); 

insert into vehicle (name, mileage, vtype, vin, version_number) values ('Volkswagen', 10000, 10, 'DE123456', 32765);
insert into vehicle (name, mileage, vtype, vin, version_number) values ('Ford', 25000, 11, 'FG999777', 15);

-- enum

-- make table read-write
set @read_only_employee_state_disabled = 1;
insert into employee_state (id, since, description, active) values (1, '2015-07-01', 'Enrolled', 1);
insert into employee_state (id, since, description, active) values (2, null, 'Accepted_OK', 1);
insert into employee_state (id, since, description, active) values (3, '2016-04-22', 'Pending Notification', 0);
insert into employee_state (id, since, description, active) values (4, '2017-03-05', 'Rejected, but can Reapply!', 1);
-- make table read-only (default when unset)
set @read_only_employee_state_disabled = 0;

insert into employee (id, name, state_id, hired_on) values (101, 'Peter', 2, '2017-08-01');
insert into employee (id, name, state_id, hired_on) values (102, 'Alice', 1, '2017-07-15');
insert into employee (id, name, state_id, hired_on) values (103, 'Donna', 4, '2017-08-03');






-- complex names

insert into `car#part$Price` (`part#`, `price$dollar`, `%discount`) values (123, 456, 789);
insert into `car#part$Price` (`part#`, `price$dollar`, `%discount`) values (101, 202, 303);

INSERT INTO catalog2.house (id, name) VALUES (1, 'House 1');
INSERT INTO catalog2.house (id, name) VALUES (2, 'House 2');

INSERT INTO house (address,price) VALUES ('123 Maple St', 150000);
INSERT INTO house (address,price) VALUES ('456 Oak St', 320000);
INSERT INTO house (address,price) VALUES ('789 Columbia Pike', 250000);


insert into catalog2.house (id, name) values (70, 'Main Source');
insert into catalog2.house (id, name) values (71, 'Secondary Source');
  
insert into catalog2.account_alert (raised_at, account_id, house_id) values ('2018-01-01 17:56:23', 1234001, 70);
insert into catalog2.account_alert (raised_at, account_id, house_id) values ('2018-01-02 18:12:34', 1234001, 70);
insert into catalog2.account_alert (raised_at, account_id, house_id) values ('2018-01-20 20:59:59', 1234004, 71);
insert into catalog2.account_alert (raised_at, account_id, house_id) values ('2018-01-21 08:05:12', 1234004, 71);

