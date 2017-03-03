--      VISIT*---BRANCH---*DAILY_REPORT
--                 |   \
--                 |    --*MT_VVAL002 (VEHICLE_VALUATION)          
--                 |  
--                 |               +------+
-- CAR---------    |               |      |
--             \   *               *      |
-- MOTORCYCLE---VEHICLE--      --CLIENT---+
--             /         \    /
-- TRUCK-------           *  * 
--                      PURCHASE     

-- drop all database objects

drop table if exists visit;
drop view if exists car;
drop view if exists truck;
drop view if exists motorcycle;
drop table if exists vehicle;
drop table if exists client;
drop sequence if exists client_seq;
drop table if exists branch;
drop table if exists purchase;
drop sequence if exists pdf_report_file_seq;
drop table if exists daily_report;
drop table if exists mt_vval002;

-- Table with identity-generated PK

create table branch (
  id integer identity primary key not null,
  name varchar(50) not null
);

insert into branch (id, name) values (101, 'Daytona');
insert into branch (id, name) values (102, 'Chattanooga');
insert into branch (id, name) values (103, 'Indianapolis');
insert into branch (id, name) values (104, 'Ventura');
insert into branch (id, name) values (105, 'Talladega');
insert into branch (id, name) values (106, 'Costa Mesa');

create table visit (
  recorded_at datetime not null,
  branch_id integer not null,
  notes varchar(1000) not null,
  constraint fk_visit_branch foreign key (branch_id) references branch (id),
);

create table vehicle (
  id integer identity primary key not null,
  brand varchar(30) not null,
  model varchar(30) not null,
  type varchar(15) not null check type in ('CAR', 'TRUCK', 'MOTORCYCLE'),
  vin varchar(20) not null,
  engine_number varchar(30) not null,
  mileage integer,
  purchased_on date not null,
  branch_id integer,
  list_price integer,
  sold boolean not null,
  constraint fk_vehicle_branch foreign key (branch_id) references branch (id),
  constraint vehicle_uq_vin unique (vin),
  constraint vehicle_uq_en unique (engine_number)
);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Kia', 'Soul', 'CAR', 'VIN1207', 'EN102934', '28500', '2016-03-14', 101, 10500, true);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Peterbilt', '379', 'TRUCK', 'VIN1023', 'EN102935', '84500', '2016-02-22', 102, 115000, false);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Toyota', 'Tercel', 'CAR', 'VIN9144', 'EN102936', '26', '2017-01-28', 103, null, false);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Kenworth', 'K100', 'TRUCK', 'VIN1377', 'EN102937', '15', '2017-02-17', 101, 138000, false);
  
insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('DeLorean', 'DMC-12', 'CAR', 'VIN7492', 'EN102938', '241689', '2015-11-17', 101, 26800, false);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Yamaha', 'YZ-250f', 'MOTORCYCLE', 'VIN3958', 'EN102939', '45600', '2017-02-17', 105, 21500, false);

  insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Toyota', 'Tercel', 'CAR', 'VIN12563', 'EN102-8821', '56000', '2016-12-18', null, null, false);

create view car as select * from vehicle where type = 'CAR';
create view truck as select * from vehicle where type = 'TRUCK';
create view motorcycle as select * from vehicle where type = 'MOTORCYCLE';

create table client (
  id integer primary key not null,
  created_at datetime not null,
  name varchar(40) not null,
  state char(2) not null,
  drivers_license varchar(20) not null,
  referred_by_id integer,
  total_purchased bigint not null,
  vip boolean default false,
  row_version bigint not null,
  constraint fk_client_referred_by foreign key (referred_by_id) references client (id),
  constraint client_uq_sdl unique (state, drivers_license)
);  

create sequence client_seq;

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (20, '2017-01-15', 'John', 'VA', 'B1234567890', null, 0, false, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (21, '2017-02-05', 'Jane', 'CA', 'C546385490', null, 25000, false, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (22, '2017-02-05', 'Peter', 'WV', 'W154432607', 21, 0, false, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (23, '2017-01-16', 'Scott', 'MI', 'M32607-16A', 21, 33500, false, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (24, '2017-03-21', 'Gunilla', 'TX', 'T318AK294', 23, 44000, true, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (25, '2017-03-15', 'Leyla', 'MO', 'MO8WN7832', 21, 28500, false, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (26, '2017-01-05', 'Marie', 'CA', 'C535893758', null, 65200, true, 0);


create table purchase (
  id bigint not null primary key,
  subtotal double not null,
  food_type integer not null,
  zip_code varchar(20) not null,
  order_date date not null,
  quantity integer not null,
  discount double null,
  shipping_type int not null
);

create sequence pdf_report_file_seq;

create table daily_report (
  report_date date not null,
  branch_id integer not null,
  total_sold bigint not null,
  primary key (report_date, branch_id),
  constraint fk_report_branch foreign key (branch_id) references branch (id)
);

create table mt_vval002 (          -- Vehicle Valuation TABLE
  if date not null,                -- Valuation Date
  sldbrid integer not null,        -- Branch Id
  "$valuation" decimal(12, 2) not null,  -- Total Branch Valuation
  primary key (if, sldbrid),
  constraint fk_vval_branch foreign key (sldbrid) references branch (id)
);

drop table if exists dd2;

create table dd2 (
  id integer primary key not null,
  dif date not null,
  qbid integer not null,
  constraint fk_dd2_val foreign key (dif, qbid) references mt_vval002 (if, sldbrid)
);


