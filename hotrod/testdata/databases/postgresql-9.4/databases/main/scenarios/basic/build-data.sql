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

insert into account (id, current_balance, name, type, active, created_on) values (1234001, 120, 'CHK1004', 'CHK', 1, '2015-12-10');
insert into account (id, current_balance, name, type, active, created_on) values (1234004, 500, 'SAV7018', 'SAV', 1, '2015-12-08');
insert into account (id, current_balance, name, type, active, created_on) values (1234005, 45, 'CHK2301', 'CHK', 0, '2015-9-1');
  
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
  
insert into vehicle (name, mileage, version_number) values ('Volkswagen', 10000, 32765);
insert into vehicle (name, mileage, version_number) values ('Ford', 25000, 15);

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



