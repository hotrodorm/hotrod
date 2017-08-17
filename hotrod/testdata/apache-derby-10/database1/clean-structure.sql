drop view tx_branch;

drop table application_config;

drop table codes; 
drop table quadrant; 

drop table client; 
drop table "transaction"; 
drop table federal_branch; 
drop table state_branch; 

drop view hefty_account;
drop table account; 

drop table config_values; 
drop table properties; 
drop table parameters; 
drop table log; 

drop table deputy;
drop table agent; 

drop sequence seq_agent restrict;
drop sequence seq_account restrict;
drop sequence seq_transaction restrict;
drop sequence seq_codes restrict;

drop sequence seq_test restrict;
drop table test_sequence;

-- database objects with special character in their names

drop table " !#$%)(*+,-.";

-- enum

drop table employee;
drop table employee_state;
drop table employee_interim;

-- unsupported multi-reference FKs

drop table house;
drop table house_type;

-- =========
-- All Types
-- =========

drop table types_numeric;
drop table types_char;
drop table types_date_time;

drop table types_binary;
drop table types_other;  

