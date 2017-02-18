    
insert into federal_branch (id, name) values (101, 'Virginia');
insert into federal_branch (id, name) values (102, 'Maryland');
insert into federal_branch (id, name) values (103, 'Delaware');
insert into transaction2 (account_id, time, amount, fed_branch_id) 
  values (1234001, 'Time 001', 100, 101);
insert into transaction2 (account_id, time, amount, fed_branch_id) 
  values (1234004, 'Time 004', 150, 103);
insert into transaction2 (account_id, time, amount, fed_branch_id) 
  values (1234005, 'Time 005', 160, 102);
  
insert into account (current_balance, name, created_on) values (120, 'CHK1004', '2015-12-10');
insert into account (current_balance, name, created_on) values (500, 'SAV7018', '2015-12-08');
insert into account (current_balance, name, created_on) values (45, 'CHK2301', '2015-9-1');
     
-- =====
-- Types
-- =====
