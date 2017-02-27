--      VISIT*---BRANCH---*DAILY_REPORT
--                 |   \
--                 |    --*MT_RP002 (monthly_report)          
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
drop table if exists mt_rp002; -- monthly_report TABLE

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
  notes carchar(1000) not null,
  constraint fk_visit_branch foreign key (branch_id) references branch (id),
);

create table vehicle (
  id integer identity primary key not null,
  brand varchar(30) not null,
  model varchar(30) not null,
  type varchar(15) not null check type in ('CAR', 'TRUCK', 'MOTORCYCLE'),
  vin varchar(20) not null,
  engine_number varchar(30) not null,
  mileage integer not null,
  purchased_on date not null,
  selling_branch_id integer,
  selling_price integer,
  sold boolean not null,
  constraint fk_vehicle_branch foreign key (branch_id) references branch (id),
  constraint vehicle_uq_vin unique (vin),
  constraint vehicle_uq_en unique (engine_number)
);

insert into vehicle (brand, model, type, mileage, purchased_on, selling_branch_id, sold)
  values ('Kia', 'Soul', 'CAR', '28500', '2016-03-14', 101, true);

insert into vehicle (brand, model, type, mileage, purchased_on, selling_branch_id, sold)
  values ('Peterbilt', '379', 'TRUCK', '84500', '2016-02-22', 102, false);

insert into vehicle (brand, model, type, mileage, purchased_on, selling_branch_id, sold)
  values ('Toyota', 'Tercel', 'CAR', '26', '2017-01-28', 103, false);

insert into vehicle (brand, model, type, mileage, purchased_on, selling_branch_id, sold)
  values ('Kenworth', 'K100', 'TRUCK', '15', '2017-02-17', 101, false);
  
insert into vehicle (brand, model, type, mileage, purchased_on, selling_branch_id, sold)
  values ('DeLorean', 'DMC-12', 'CAR', '241689', '2015-11-17', 101, false);

insert into vehicle (brand, model, type, mileage, purchased_on, selling_branch_id, sold)
  values ('Yamaha', 'YZ-250f', 'MOTORCYCLE', '45600', '2017-02-17', 105, false);

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

create table mt_rp002 (     -- monthly_report TABLE
  if date not null,         -- report_date
  sldbrid integer not null, -- branch_id
  "$sold" bigint not null,  -- total_sold
  primary key (if, sldbrid),
  constraint fk_report_branch foreign key (sldbrid) references branch (id)
);

