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
  
insert into vehicle (name, mileage, version_number) values ('Volkswagen', 10000, 32765);
insert into vehicle (name, mileage, version_number) values ('Ford', 25000, 15);

set @read_only_employee_state_disabled = 1;
insert into employee_state (id, description) values (1, 'Enrolled');
insert into employee_state (id, description) values (2, 'Accepted_OK');
insert into employee_state (id, description) values (3, 'Pending Notification');
insert into employee_state (id, description) values (4, 'Rejected, but can Reapply!');
set @read_only_employee_state_disabled = 0;




















