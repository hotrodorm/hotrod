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
drop sequence if exists data_file_seq;
drop table if exists preferred_colors;

-- Table with identity-generated PK

create table branch (
  id integer identity primary key not null,
  name varchar(50) not null,
  current_cash integer default 0 not null
);

insert into branch (id, name, current_cash) values (101, 'Daytona', 15000);
insert into branch (id, name, current_cash) values (102, 'Chattanooga', 6000);
insert into branch (id, name, current_cash) values (103, 'Indianapolis', 18500);
insert into branch (id, name, current_cash) values (104, 'Ventura', 4100);
insert into branch (id, name, current_cash) values (105, 'Talladega', 9700);
insert into branch (id, name, current_cash) values (106, 'Costa Mesa', 800);

create table visit (
  recorded_at datetime not null,
  branch_id integer not null,
  notes varchar(1000) default '(no notes taken)' not null,
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
  purchased_on date default current_date() not null,
  branch_id integer,
  list_price integer,
  sold boolean default false not null,
  constraint fk_vehicle_branch foreign key (branch_id) references branch (id),
  constraint vehicle_uq_vin unique (vin),
  constraint vehicle_uq_en unique (engine_number)
);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Kia', 'Soul', 'CAR', 'VIN1207', 'EN102934', 28500, '2016-03-14', 101, 10500, true);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Peterbilt', '379', 'TRUCK', 'VIN1023', 'EN102935', 84500, '2016-02-22', 102, 115000, false);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Toyota', 'Tercel', 'CAR', 'VIN9144', 'EN102936', 26, '2017-01-28', 103, null, false);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Kenworth', 'K100', 'TRUCK', 'VIN1377', 'EN102937', 15, '2017-02-17', 101, 138000, false);
  
insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('DeLorean', 'DMC-12', 'CAR', 'VIN7492', 'EN102938', 241689, '2015-11-17', 101, 26800, false);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Yamaha', 'YZ250F', 'MOTORCYCLE', 'VIN3958', 'EN102939', 45600, '2017-02-17', 105, 7490, true);

  insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Yamaha', 'YZ125', 'MOTORCYCLE', 'VIN4190', 'EN102112', 33127, '2017-01-05', 105, 6290, false);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Toyota', 'Yaris', 'CAR', 'VIN5543', 'EN1023255', 95000, '2016-11-17', null, null, false);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Toyota', 'Tercel', 'CAR', 'VIN12563', 'EN028821', 56000, '2016-12-18', null, null, false);

insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold)
  values ('Kia', 'Rio', 'CAR', 'VIN4401', 'EN102881', 28500, '2016-03-14', 104, 14900, true);

create view car as select * from vehicle where type = 'CAR';

-- create trigger car_view_updatable instead of insert, update, delete on car for each row 
--   call "examples.triggers.UpdatableCarViewTrigger";

create view truck as select * from vehicle where type = 'TRUCK';
create view motorcycle as select * from vehicle where type = 'MOTORCYCLE';

create table client (
  id integer primary key not null,
  created_at datetime not null,
  name varchar(40) not null,
  state char(2) not null,
  drivers_license varchar(20) not null,
  referred_by_id integer,
  total_purchased bigint,
  vip boolean default false,
  row_version bigint not null,
  constraint fk_client_referred_by foreign key (referred_by_id) references client (id),
  constraint client_uq_sdl unique (state, drivers_license)
);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (20, '2017-01-15', 'John', 'VA', 'B1234567890', null, 0, false, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (21, '2017-02-05', 'Jane', 'CA', 'C546385490', null, 0, false, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (22, '2017-02-05', 'Peter', 'WV', 'W154432607', 21, 0, false, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (23, '2017-01-16', 'Scott', 'MI', 'M32607-16A', 21, 0, false, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (24, '2017-03-21', 'Gunilla', 'TX', 'T318AK294', 23, 0, true, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (25, '2017-03-15', 'Leyla', 'MO', 'MO8WN7832', 21, 0, false, 0);

insert into client (id, created_at, name, state, drivers_license, referred_by_id, total_purchased, vip, row_version) 
  values (26, '2017-01-05', 'Marie', 'CA', 'C535893758', null, 0, true, 0);

create sequence client_seq start with 100;

create table purchase (
  id bigint identity not null primary key,
  vehicle_price integer not null,
  client_id integer not null,
  vehicle_id integer not null,
  zip_code varchar(20) not null,
  purchase_date date not null,
  extras_price integer null,
  discount integer null,
  tax integer null,
  final_price integer not null,
  free_spare_tire boolean not null default false,
  free_set_of_keys boolean not null default false,
  free_floor_mats boolean not null default false,
  free_tank_of_gas boolean not null default false,
  free_oil_change boolean not null default false,
  constraint fk_purchase_client foreign key (client_id) references client (id),
  constraint fk_purchase_vehicle foreign key (vehicle_id) references vehicle (id)
);

insert into purchase (vehicle_price, client_id, vehicle_id, zip_code, purchase_date, extras_price, discount, tax, final_price)
  values (10300, 21, 1, '90210', '2017-03-21', 0, 0, 560, 10860);

insert into purchase (vehicle_price, client_id, vehicle_id, zip_code, purchase_date, extras_price, discount, tax, final_price)
  values (7400, 21, 6, '04502', '2017-02-28', 450, 500, 260, 7610);

insert into purchase (vehicle_price, client_id, vehicle_id, zip_code, purchase_date, extras_price, discount, tax, final_price)
  values (18200, 24, 10, '22181', '2017-02-28', 0, 0, 840, 19040);

create sequence pdf_report_file_seq start with 60;

create table daily_report (
  report_date date not null,
  branch_id integer not null,
  total_sold bigint default 0 not null,
  primary key (report_date, branch_id),
  constraint fk_report_branch foreign key (branch_id) references branch (id)
);

create sequence data_file_seq start with 210;

create table mt_vval002 (          -- Vehicle Valuation TABLE
  if date not null,                -- Valuation Date
  sldbrid integer not null,        -- Branch Id
  "$valuation" decimal(12, 2) not null,  -- Total Branch Valuation
  primary key (if, sldbrid),
  constraint fk_vval_branch foreign key (sldbrid) references branch (id)
);

create table preferred_colors (
  id integer primary key not null,
  colors array not null
);





 

