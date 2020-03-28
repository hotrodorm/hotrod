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
 
insert into "car#part$Price" ("part#", "price$Dollar", "%discount") values (123, 456, 789);
insert into "car#part$Price" ("part#", "price$Dollar", "%discount") values (101, 202, 303);

 
INSERT INTO schema2.house (id,name) VALUES (1,'House 1');
INSERT INTO schema2.house (id,name) VALUES (2,'House 2');

--INSERT INTO house (address,price) VALUES ('123 Maple St', 150000);
--INSERT INTO house (address,price) VALUES ('456 Oak St', 320000);
--INSERT INTO house (address,price) VALUES ('789 Columbia Pike', 250000);

insert into schema2.house (id, name) values (70, 'Main Source');
insert into schema2.house (id, name) values (71, 'Secondary Source');
  
insert into schema2.account_alert (raised_at, account_id, house_id) values (timestamp '2018-01-01 17:56:23', 1, 70);
insert into schema2.account_alert (raised_at, account_id, house_id) values (timestamp '2018-01-02 18:12:34', 3, 70);
insert into schema2.account_alert (raised_at, account_id, house_id) values (timestamp '2018-01-20 20:59:59', 2, 71);
insert into schema2.account_alert (raised_at, account_id, house_id) values (timestamp '2018-01-21 08:05:12', 2, 71);


set read_only_employee_state = 0;
insert into employee_state (id, description) values (1, 'Applying');
insert into employee_state (id, description) values (2, 'Rejected');
insert into employee_state (id, description) values (3, 'Active');
insert into employee_state (id, description) values (4, 'Inactive');
set read_only_employee_state = 1;





















