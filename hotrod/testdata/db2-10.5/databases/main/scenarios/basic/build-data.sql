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

    
INSERT INTO FEDERAL_BRANCH (ID,NAME) VALUES (1, 'California');
INSERT INTO FEDERAL_BRANCH (ID,NAME) VALUES (2, 'Maine');
INSERT INTO FEDERAL_BRANCH (ID,NAME) VALUES (3, 'Kansas');
insert into federal_branch (id, name) values (10, 'South');
insert into federal_branch (id, name) values (20, 'West');
insert into federal_branch (id, name) values (30, 'East');

insert into account (name, created_on, current_balance, started_on)
 values             ('CHK0123', '2015-12-29', 300, '2015-01-01');
insert into account (name, created_on, current_balance, started_on)
 values             ('CHK0124', '2015-11-26', 300, '2015-02-02');
insert into account (name, created_on, current_balance, started_on)
 values             ('CHK0125', '2015-10-12', 300, '2015-03-03');


insert into transaction (account_id, seq_id, time, amount, fed_branch_id)
 values                 (1, 1, '12:00:00', 51, 20);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id)
 values                 (1, 2, '12:00:03', 120, 20);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id)
 values                 (1, 3, '12.00.10', -30, 20);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id)
 values                 (2, 4, '12.00.03', 200, 1);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id)
 values                 (2, 5, '12.00.05', 15, 30);
 
 
