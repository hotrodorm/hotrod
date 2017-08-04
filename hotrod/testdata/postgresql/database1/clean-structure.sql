drop table if exists vehicle;
drop view if exists tx_branch;

drop table if exists application_config;

drop table if exists codes; 
drop table if exists quadrant; 

drop table if exists agent; 
drop table if exists client; 
drop table if exists transaction; 
drop table if exists federal_branch; 
drop table if exists state_branch; 
drop table if exists account; 

drop table if exists config_values; 
drop table if exists properties; 
drop table if exists parameters; 
drop table if exists log; 

drop sequence if exists seq_agent; 

drop table if exists vehicle;

-- =========
-- All Types
-- =========

drop table if exists types_numeric;
drop table if exists types_char;
drop table if exists types_binary;
drop table if exists types_date_time;
drop table if exists types_other;  

drop TYPE if exists mood;
drop TYPE if exists complex;

drop table employee;
drop table employee_state;
