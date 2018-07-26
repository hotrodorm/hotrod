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

insert into account (id, current_balance, name, created_on) values (1234001, 120, 'CHK1004', '2015-12-10 12:00:45');
insert into account (id, current_balance, name, created_on) values (1234004, 500, 'SAV7018', '2015-12-08 12:00:45');
insert into account (id, current_balance, name, created_on) values (1234005, 45, 'CHK2301', '2015-09-01 12:00:45');
  
insert into "transaction" (account_id, seq_id, time, amount, fed_branch_id) values (1234001, 1, 'Time 001', 100, 101);
insert into "transaction" (account_id, seq_id, time, amount, fed_branch_id) values (1234004, 2, 'Time 004', 150, 103);
insert into "transaction" (account_id, seq_id, time, amount, fed_branch_id) values (1234005, 4, 'Time 005', 160, 102);
insert into "transaction" (account_id, seq_id, time, amount, fed_branch_id) values (1234005, 15, 'Time 007', -110, 103);
  
insert into config_values(node, cell, name, verbatim) values(10, 100, 'prop1', 'value1');
insert into config_values(node, cell, name, verbatim) values(10, 101, 'prop2', 'value2');
insert into config_values(node, cell, name, verbatim) values(20, 100, 'prop3', 'value3');
  
insert into vehicle (id, name, mileage, version_number) values (1, 'Volkswagen', 10000, 32765);
insert into vehicle (id, name, mileage, version_number) values (2, 'Ford', 25000, 15);
