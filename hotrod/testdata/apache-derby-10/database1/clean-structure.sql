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
drop sequence seq_codes restrict;

-- Auto-generated columns 

drop table test_sequence1;
drop table test_sequence2;
drop sequence gen_seq1 restrict;
drop sequence gen_seq2 restrict;
drop table test_default1;
drop table test_default2;
drop table test_identity1;
drop table test_mixed1;
drop table test_seq_ide_def1;

-- Associations & Collections

drop table leaf;
drop table branch;
drop table tree;

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

