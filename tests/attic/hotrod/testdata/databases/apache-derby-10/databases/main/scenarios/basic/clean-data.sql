delete from application_config;

delete from " !#$%)(*+,-.";

delete from "transaction";
delete from account;

delete from client;
delete from federal_branch;

delete from employee;
delete from employee_state;
delete from employee_interim;

delete from types_other;

delete from quadrant;

-- Associations & Collections

delete from flower;
delete from leaf;
delete from branch;
delete from branch_type;
delete from tree;

-- restart sequences

drop sequence seq_agent restrict;
drop sequence seq_codes restrict;
drop sequence seq_test restrict;

create sequence seq_agent;
create sequence seq_codes;
create sequence seq_test;
