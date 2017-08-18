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

insert into "transaction" (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234001, 1, 'Time 001', 100, 101);
insert into "transaction" (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234004, 2, 'Time 004', 150, 103);
insert into "transaction" (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234005, 4, 'Time 005', 160, 102);

drop sequence seq_transaction restrict;  
create sequence seq_transaction start with 10;
  
insert into account (id, name, type, current_balance, created_on, row_version) values (1, 'ACC2', 'CHK', 50, date('2016-01-01'), 0);
insert into account (id, name, type, current_balance, created_on, row_version) values (2, 'ACC123', 'SAV', 150, date('2016-02-01'), 0);
insert into account (id, name, type, current_balance, created_on, row_version) values (3, 'ACC201', 'CHK', 250, date('2016-03-01'), 0);
insert into account (id, name, type, current_balance, created_on, row_version) values (4, 'ACC500', 'SAV', 350, date('2016-04-01'), 0);
insert into account (id, name, type, current_balance, created_on, row_version) values (8, 'ACC8', 'CHK', null, date('2016-01-01'), 0);
insert into account (id, name, type, current_balance, created_on, row_version) values (9, 'ACC9', 'SAV', null, date('2016-01-02'), 0);
insert into account (id, name, type, current_balance, created_on, row_version) values (10, 'SAV0001', 'SAV',  100, date('2016-01-01'), 0);
insert into account (id, name, type, current_balance, created_on, row_version) values (11, 'ACC11', 'CHK', null, date('2016-01-03'), 0);
insert into account (id, name, type, current_balance, created_on, row_version) values (20, 'aCC123', 'SAV', 150, date('2016-02-01'), 0);
insert into account (id, name, type, current_balance, created_on, row_version) values (21, 'acc123', 'SAV', 150, date('2016-02-01'), 0);
insert into account (id, name, type, current_balance, created_on, row_version) values (22, 'acC123', 'SAV', 150, date('2016-02-01'), 0);
insert into account (id, name, type, current_balance, created_on, row_version) values (25, 'ACC732', 'CHK', 21050, date('2014-08-15'), 0);

-- =====
-- Types
-- =====

-- Quadrant

insert into quadrant (region, area, caption, active_state) values (1, 12, 'Quadrant 1-12', 1);
insert into quadrant (region, area, caption, active_state) values (1, 23, 'Quadrant 1-23', 0);
insert into quadrant (region, area, caption, active_state) values (5, 1, 'Quadrant 5-1', 1);
insert into quadrant (region, area, caption, active_state) values (14, 99, 'Quadrant 14-99', 0);
insert into quadrant (region, area, caption, active_state) values (18, 86, 'Quadrant 18-86', 1);

-- Special Characters

insert into " !#$%)(*+,-." (id, ":<>?&", "@[\]^_", "`{|}~") values (1, 'red', 'kind 1', 'type 2');
insert into " !#$%)(*+,-." (id, ":<>?&", "@[\]^_", "`{|}~") values (2, 'green', 'kind 3', 'type 4');
insert into " !#$%)(*+,-." (id, ":<>?&", "@[\]^_", "`{|}~") values (3, 'blue', 'kind 5', 'type 6');

-- enum

insert into employee_state (id, since, description, active) values (1, date('2015-06-17'), 'Enrolled', 1);
insert into employee_state (id, since, description, active) values (2, null, 'Accepted_OK', 1);
insert into employee_state (id, since, description, active) values (3, date('2016-04-29'), 'Pending Notification', 0);
insert into employee_state (id, since, description, active) values (4, date('2017-03-01'), 'Rejected, but can Reapply!', 1);

insert into employee_interim (start_date, caption) values (date('2014-08-15'), 'First Year');
insert into employee_interim (start_date, caption) values (date('2017-07-28'), 'Second Year');

insert into employee (id, name, state_id, initial_state_id, hired_on, classification) values (101, 'Peter', 2, 3, date('2017-08-01'), date('2014-08-15'));
insert into employee (id, name, state_id, initial_state_id, hired_on, classification) values (102, 'Alice', 1, 2, date('2017-07-15'), date('2014-08-15'));
insert into employee (id, name, state_id, initial_state_id, hired_on, classification) values (103, 'Donna', 4, 1, date('2017-08-03'), date('2017-07-28'));






