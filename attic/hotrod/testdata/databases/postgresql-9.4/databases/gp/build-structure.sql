
create table vendor (
  id int primary key not null,
  name varchar(100) not null,
  host varchar(200) not null,
  port int not null,
  username varchar(40) not null,
  password varchar(232) not null, -- 100 chars + 16-byte IV
  poll_delay_ms int not null,
  remote_outbound_dir varchar(200) not null,
  remote_inbound_dir varchar(200) not null,
  requested_threads int not null,
  status int not null,
  constraint chk_active check (status in (0, 1)),
  constraint uq_vendor_name unique(name)
);

insert into vendor (id, name, host, port, username, password, poll_delay_ms, remote_outbound_dir, remote_inbound_dir, requested_threads, status) 
  values (1, 'ACS', '192.168.56.201', 22, 'user1', 'bfa33f7f38c6ff8a5cca83657063e67d', 10000, 'basedir1/send', 'basedir1/receive', 5, 1);

create table client (
  id int primary key not null,
  name varchar(50) not null,
  vendor_id int not null,
  local_outbound_dir varchar(200) not null,
  local_inbound_dir varchar(200) not null,
  requested_threads int not null,
  status int not null,
  constraint chk_active check (status in (0, 1)),
  constraint fk_client_vendor foreign key (vendor_id) references vendor (id),
  constraint uq_client_name unique(name)
);

insert into client (id, name, vendor_id, local_outbound_dir, local_inbound_dir, requested_threads, status) values (1, 'Black Lung', 1, 'local-dir/sftp/dcmwc-transfer/cbp-send', 'local-dir/sftp/dcmwc-transfer/cbp-receive', 5, 1);
insert into client (id, name, vendor_id, local_outbound_dir, local_inbound_dir, requested_threads, status) values (2, 'DFEC', 1, 'local-dir/sftp/dfec-transfer/cbp-send', 'local-dir/sftp/dfec-transfer/cbp-receive', 5, 1);
insert into client (id, name, vendor_id, local_outbound_dir, local_inbound_dir, requested_threads, status) values (3, 'Energy', 1, 'local-dir/sftp/eecs-transfer/cbp-send', 'local-dir/sftp/eecs-transfer/cbp-receive', 5, 1);
insert into client (id, name, vendor_id, local_outbound_dir, local_inbound_dir, requested_threads, status) values (4, 'Long Shore', 1, 'local-dir/sftp/dlhwc-transfer/cbp-send', 'local-dir/sftp/dlhwc-transfer/cbp-receive', 5, 1);

create table client_file_prefix (
  prefix varchar(50) not null,
  name varchar(100),
  client_id int not null,
  constraint fk_prefix_client foreign key (client_id) references client (id),
  constraint uq_prefix unique (prefix)
);

-- DFEC

insert into client_file_prefix (client_id, prefix, name) values (2, 'AI', '"Adjustment Inbound" (Fiscal AR1s and AR2s)');
insert into client_file_prefix (client_id, prefix, name) values (2, 'AO', '"Adjustment Outbound" (Fiscal AR1s and AR2s)');
insert into client_file_prefix (client_id, prefix, name) values (2, 'BH', '"Bill History"');
insert into client_file_prefix (client_id, prefix, name) values (2, 'CE', '"Claimant Eligibility"');
insert into client_file_prefix (client_id, prefix, name) values (2, 'CI', '"COP Nurse Bill Pay Acknowledgements Into iFECS"');
insert into client_file_prefix (client_id, prefix, name) values (2, 'CN', '"COP Nurse Bills"');
insert into client_file_prefix (client_id, prefix, name) values (2, 'DG', '"Drug" ‐ Compound Drugs Denied/Paid');
insert into client_file_prefix (client_id, prefix, name) values (2, 'EI', '"Eligibility Interface"');
insert into client_file_prefix (client_id, prefix, name) values (2, 'FL', '"FLips" (SFC cases that have "FLIP"ed)');
insert into client_file_prefix (client_id, prefix, name) values (2, 'NW', '"NeW entrant ‐ Opioid/Compound Drugs"');
insert into client_file_prefix (client_id, prefix, name) values (2, 'OP', '"OverPayments"');
insert into client_file_prefix (client_id, prefix, name) values (2, 'PD', '"PBM Pharmacy Data"');
insert into client_file_prefix (client_id, prefix, name) values (2, 'PV', '"ProVider"');
insert into client_file_prefix (client_id, prefix, name) values (2, 'TF', '"Treasury File"');
insert into client_file_prefix (client_id, prefix, name) values (2, '01', 'BOS CMR docs');
insert into client_file_prefix (client_id, prefix, name) values (2, '02', 'NYC CMR docs');
insert into client_file_prefix (client_id, prefix, name) values (2, '03', 'PHI CMR docs');
insert into client_file_prefix (client_id, prefix, name) values (2, '06', 'JAX CMR docs');
insert into client_file_prefix (client_id, prefix, name) values (2, '09', 'CLE CMR docs');
insert into client_file_prefix (client_id, prefix, name) values (2, '10', 'CHI CMR docs');
insert into client_file_prefix (client_id, prefix, name) values (2, '11', 'KCM CMR docs');
insert into client_file_prefix (client_id, prefix, name) values (2, '12', 'DEN CMR docs');
insert into client_file_prefix (client_id, prefix, name) values (2, '13', 'SFC CMR docs');
insert into client_file_prefix (client_id, prefix, name) values (2, '14', 'SEA CMR docs');
insert into client_file_prefix (client_id, prefix, name) values (2, '16', 'DAL CMR docs');
insert into client_file_prefix (client_id, prefix, name) values (2, '25', 'DO25 CMR ');
insert into client_file_prefix (client_id, prefix, name) values (2, '50', 'H&R/ECAB/NO CMR docs');

-- Black Lung

insert into client_file_prefix (client_id, prefix, name) values (1, 'BLB1', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLBH', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLC1', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLCM', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLCR', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLCU', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLD1', null);

insert into client_file_prefix (client_id, prefix, name) values (1, 'BLD2', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLDA', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLDU', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLE1', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLEE', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLEV', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLM1', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLM2', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLO1', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLOP', null);

insert into client_file_prefix (client_id, prefix, name) values (1, 'BLMR', 'Mailroom (OIS) docs?');
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLMU', 'Mailroom (maybe Claimant updates?)');

insert into client_file_prefix (client_id, prefix, name) values (1, 'BLP2', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLPC', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLPU', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLR1', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLRC', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLS1', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLTC', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLTE', null);

insert into client_file_prefix (client_id, prefix, name) values (1, 'BLBV', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLCV', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLM3', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLMV', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLOV', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLP1', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLP3', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLPV', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLRV', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLSC', null);
insert into client_file_prefix (client_id, prefix, name) values (1, 'BLSV', null);

-- Energy

insert into client_file_prefix (client_id, prefix, name) values (3, 'ENBH', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENCS', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENCM', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENCU', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENEE', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENER', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENEV', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENOP', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENPU', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENR1', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENR2', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENRG', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENTC', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENTE', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'EN11', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'EN1E', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'EN1V', null);

insert into client_file_prefix (client_id, prefix, name) values (3, 'ENBE', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENBV', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENCE', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENCR', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENCV', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENIA', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENIE', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENIV', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENMA', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENME', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENMN', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENMR', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENMU', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENMV', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENOE', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENOV', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENPA', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENPC', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENPE', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENPR', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENPV', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENRC', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENRE', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENRV', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENSC', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENSE', null);
insert into client_file_prefix (client_id, prefix, name) values (3, 'ENSV', null);

-- vendor  division
-- ======  ========
--                       "Initiator"
-- -       -           
-- TRI     -      
--                       "FTP Controller"
-- TRI     -      
-- TR_     TRI           (received, not processed)
-- TRN     %      
--                       "Processor"
-- %       TRI           (not processed)
-- %       TR_           (in process)
-- %       TRN           (fully processed)
-- %       TRN/ACK       (acknowledge to be sent)
--                       "FTP Controller"
-- TRN/ACK TRN/AC_       (acknowledge being sent)
-- TRN/ACK TRN/ACD       (acknowledge sent)

create table severity (
  id int primary key not null,
  name varchar(30) not null
);

insert into severity (id, name) values (1, 'Warning');
insert into severity (id, name) values (2, 'Critical');
insert into severity (id, name) values (3, 'Fatal');

create table operation (
  id int primary key not null,
  name varchar(100) not null
);

insert into operation (id, name) values (100, 'Failed to load application configuration');

insert into operation (id, name) values (101, 'Connect to remote SFTP server.');
insert into operation (id, name) values (102, 'Get remote SFTP version');
insert into operation (id, name) values (103, 'List remote SFTP files.');

insert into operation (id, name) values (104, 'Receive TRI - Identify remote TRI file''s client');
insert into operation (id, name) values (105, 'Receive TRI - Mark the remote TRI file as "in progress" (rename it).');
insert into operation (id, name) values (106, 'Receive TRI - Prepare TRI local dir structure to retrive it.');
insert into operation (id, name) values (107, 'Receive TRI - Retrieve TRI inner files.');
insert into operation (id, name) values (108, 'Receive TRI - Retrieve the TRI file.');
insert into operation (id, name) values (109, 'Receive TRI - Mark the remote TRI file as "complete" (rename it).');

insert into operation (id, name) values (110, 'Receive ACK - Identify remote ACK file''s client');
insert into operation (id, name) values (111, 'Receive ACK - Mark the remote ACK file as "in progress" (rename it).');
insert into operation (id, name) values (112, 'Receive ACK - Retrieve the ACK file.');
insert into operation (id, name) values (113, 'Receive ACK - Mark the remote ACK file as "complete" (rename it).');
insert into operation (id, name) values (114, 'Receive ACK - Delete ACK''s related remote trigger inner files and directory.');

insert into operation (id, name) values (115, 'Send TRI - Identify local TRI file''s client');
insert into operation (id, name) values (116, 'Send TRI - Mark the local TRI file as "in progress" (rename it).');
insert into operation (id, name) values (117, 'Send TRI - Create the remote dir structure to send TRI.');
insert into operation (id, name) values (118, 'Send TRI - Send the TRI inner files.');
insert into operation (id, name) values (119, 'Send TRI - Send the TRI file.');
insert into operation (id, name) values (120, 'Send TRI - Mark the local TRI file as "complete" (rename it).');

insert into operation (id, name) values (121, 'Send ACK - Identify local ACK file''s client');
insert into operation (id, name) values (122, 'Send ACK - Mark the local ACK file as "in progress" (rename it).');
insert into operation (id, name) values (123, 'Send ACK - Send the ACK file.');
insert into operation (id, name) values (124, 'Send ACK - Mark the local ACK file as "complete" (rename it).');
insert into operation (id, name) values (125, 'Send ACK - Delete related local trigger inner directory and directory.');

create table cause (
  id int primary key not null,
  name varchar(100) not null
);

insert into cause (id, name) values (20, 'Could not connect to SFTP Server.');
insert into cause (id, name) values (21, 'Could not get remote SFTP version.');
insert into cause (id, name) values (22, 'Could not list remote SFTP directory.');
insert into cause (id, name) values (23, 'Unrecognized file.');
insert into cause (id, name) values (24, 'Unrecognized TRI prefix (client).');
insert into cause (id, name) values (25, 'Remote TRI file not found.');
insert into cause (id, name) values (26, 'Remote file system error.');
insert into cause (id, name) values (27, 'Other SFTP error.');
insert into cause (id, name) values (28, 'Could not create local dir.');
insert into cause (id, name) values (29, 'Remote dir not found.');
insert into cause (id, name) values (30, 'Remote file not found.');
insert into cause (id, name) values (31, 'Interrupted file transfer.');
insert into cause (id, name) values (32, 'Local file system full.');
insert into cause (id, name) values (33, 'Permission denied.');
insert into cause (id, name) values (34, 'Unrecognized ACK prefix (client).');
insert into cause (id, name) values (35, 'Could not rename local file.');
insert into cause (id, name) values (36, 'Could not create remote prefix dir; file with same name already exists.');
insert into cause (id, name) values (37, 'Permission denied to create remote prefix dir.');
insert into cause (id, name) values (38, 'Parent dir not found for remote prefix dir.');
insert into cause (id, name) values (39, 'Could not create remote TRI content dir; file with same name already exists.');
insert into cause (id, name) values (40, 'Permission denied to create remote TRI content dir.');
insert into cause (id, name) values (41, 'Parent dir not found for remote TRI content dir.');
insert into cause (id, name) values (42, 'Local file not found.');
insert into cause (id, name) values (43, 'Could not delete local file.');
insert into cause (id, name) values (44, 'Could not delete local dir.');

insert into cause (id, name) values (999, 'Other cause');

create table alert (
  raised_at timestamp not null,
  vendor_id int,
  client_id int,
  originator char(1) not null, -- 'c' for client; 'v' for vendor
  severity_id int not null,
  operation_id int not null,
  cause_id int not null,
  error_message varchar(200),
  g varchar(10) default 'xyz',
  constraint fk_alert_vendor foreign key (vendor_id) references vendor (id),
  constraint fk_alert_client foreign key (client_id) references client (id),
  constraint fk_alert_severity foreign key (severity_id) references severity (id),
  constraint fk_alert_operation foreign key (operation_id) references operation (id),
  constraint fk_alert_cause foreign key (cause_id) references cause (id),
  constraint uq_originator check (originator in ('v', 'c')),
  constraint ck_client_vendor check (vendor_id is not null or client_id is not null)
);

create index ix_alert_vendor_raised on alert (vendor_id, originator, raised_at);
create index ix_alert_client_raised on alert (client_id, originator, raised_at);

insert into alert (vendor_id, raised_at,           client_id, originator, severity_id, operation_id, cause_id, error_message)
           values (1,         '2018-07-18 11:25:04', 1,         'v',        3,           108,          31,       'Network unreachable.');    

insert into alert (vendor_id, raised_at,           client_id, originator, severity_id, operation_id, cause_id, error_message)
           values (1,         '2018-07-18 09:17:04', 1,         'v',        3,           108,          31,       'Network unreachable.');    

insert into alert (vendor_id, raised_at,           client_id, originator, severity_id, operation_id, cause_id, error_message)
           values (1,         '2018-07-18 05:40:04', 1,         'v',        3,           108,          31,       'Network unreachable.');    
           
insert into alert (vendor_id, raised_at,           client_id, originator, severity_id, operation_id, cause_id, error_message)
           values (1,         '2018-07-18 09:03:00', 3,         'c',        2,           102,          21,       'Could not get SFTP version.');    

insert into alert (vendor_id, raised_at,           client_id, originator, severity_id, operation_id, cause_id, error_message)
           values (1,         '2018-07-17 19:40:07', 3,         'c',        1,           103,          23,       'Unknown file extension ".DIR".');    

insert into alert (vendor_id, raised_at,           client_id, originator, severity_id, operation_id, cause_id, error_message)
           values (1,         '2018-07-16 17:06:01', 3,         'c',        3,           101,          20,       'Server timed out after 30 s.');    

           
create table execution_round (
  id bigserial primary key not null,
  initiated_at timestamp not null,
  vendor_id int not null,
  remote_sftp_version int,
  ended_at timestamp,
  tri_inbound_files int,
  tri_inbound_content_files int,
  tri_inbound_attempted_bytes bigint,
  tri_inbound_actual_bytes bigint,
  tri_inbound_elapsed int,
  tri_outbound_files int,
  tri_outbound_content_files int,
  tri_outbound_attempted_bytes bigint,
  tri_outbound_actual_bytes bigint,
  tri_outbound_elapsed int,
  ack_inbound_files int,
  ack_inbound_attempted_bytes bigint,
  ack_inbound_actual_bytes bigint,
  ack_inbound_elapsed int,
  ack_outbound_files int,
  ack_outbound_attempted_bytes bigint,
  ack_outbound_actual_bytes bigint,
  ack_outbound_elapsed int,
  errors_alerts int,
  critical_alerts int,
  warnings_alerts int,
  constraint fk_round_vendor foreign key (vendor_id) references vendor (id)
);

create index ix_execround_vendor_initiated_at on execution_round (vendor_id, initiated_at);






















