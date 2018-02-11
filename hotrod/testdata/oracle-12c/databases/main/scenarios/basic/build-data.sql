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

insert into transaction (account_id, seq_id, time, amount, fed_branch_id) values (1234001, 100, 'Time 001', 100, 101);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) values (1234004, 101, 'Time 004', 150, 103);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) values (1234005, 102, 'Time 005', 160, 102);
  
insert into account (name, type, current_balance, created_on, row_version) values ('ACC2', 'CHK', 50, to_date('2016-01-01','YYYY-MM-DD'), 0);
insert into account (name, type, current_balance, created_on, row_version) values ('ACC123', 'SAV', 150, to_date('2016-02-01','YYYY-MM-DD'), 0);
insert into account (name, type, current_balance, created_on, row_version) values ('ACC201', 'CHK', 250, to_date('2016-03-01','YYYY-MM-DD'), 0);
insert into account (name, type, current_balance, created_on, row_version) values ('ACC500', 'SAV', 350, to_date('2016-04-01','YYYY-MM-DD'), 0);
insert into account (name, type, current_balance, created_on, row_version) values ('ACC8', 'CHK', null, to_date('2016-01-01','YYYY-MM-DD'), 0);
insert into account (name, type, current_balance, created_on, row_version) values ('ACC9', 'SAV', null, to_date('2016-01-02','YYYY-MM-DD'), 0);
insert into account (name, type, current_balance, created_on, row_version) values ('SAV0001', 'SAV',  100, to_date('2016-01-01', 'YYYY-MM-DD'), 0);
insert into account (name, type, current_balance, created_on, row_version) values ('ACC11', 'CHK', null, to_date('2016-01-03','YYYY-MM-DD'), 0);
insert into account (name, type, current_balance, created_on, row_version) values ('aCC123', 'SAV', 150, to_date('2016-02-01','YYYY-MM-DD'), 0);
insert into account (name, type, current_balance, created_on, row_version) values ('acc123', 'SAV', 150, to_date('2016-02-01','YYYY-MM-DD'), 0);
insert into account (name, type, current_balance, created_on, row_version) values ('acC123', 'SAV', 150, to_date('2016-02-01','YYYY-MM-DD'), 0);


-- =====
-- Types
-- =====

insert into types_other (id) values(1);
-- update types_other set row1 = 'AAAMaHAAEAAAAIHAAZ' where id = 1;
update types_other set itv2 = TO_YMINTERVAL('01-02') where id = 1;
update types_other set itv4 = TO_DSINTERVAL('2 10:20:30.456') where id = 1;
-- update types_other set oth1 = XMLType('<Warehouse whNo="100"><Building>Owned</Building></Warehouse>') where id = 1;
update types_other set oth2 = httpuritype.createuri('http://www.oracle.com') where id = 1;
update types_other set names = namearray('Vine', 'Vidi', 'Vincere') where id = 1;
update types_other set stu1 = person_struct(123, to_date('2003/07/09', 'yyyy/mm/dd')) where id = 1;

-- Insert with identity

-- insert into account (name, type, current_balance, created_on, row_version) 
--    values ('AC_123456', 'SAV', 150, to_date('2016-02-01','YYYY-MM-DD'), 0) returning id into :inserted_value;

-- select inserted_value from dual;

