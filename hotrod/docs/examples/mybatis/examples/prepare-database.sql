-- Table with identity-generated PK

drop table if exists vehicle;

create table vehicle (
  id integer identity primary key not null,
  brand varchar(30) not null,
  model varchar(30) not null,
  used boolean not null,
  current_mileage integer not null,
  purchased_on date
);

insert into vehicle (brand, model, used, current_mileage, purchased_on)
  values ('Kia', 'Soul', true, '28500', '2014-03-14');

insert into vehicle (brand, model, used, current_mileage, purchased_on)
  values ('Toyota', 'Tercel', false, '26', '2017-01-28');
  
insert into vehicle (brand, model, used, current_mileage, purchased_on)
  values ('DeLorean', 'DMC-12', true, '241689', '1982-11-17');

-- Table with no PK

drop table if exists log;

create table log (
  recorded_at timestamp not null,
  message varchar(100) not null
);

-- Table with sequence-generated PK

drop table if exists branch;

create table branch (
  id integer primary key not null,
  name varchar(50) not null
);

drop sequence if exists branch_seq;

create sequence branch_seq;

-- Dynamic <update> tag

drop table if exists purchase_order;

create table purchase_order (
  id bigint not null primary key,
  subtotal double not null,
  food_type integer not null,
  zip_code varchar(20) not null,
  order_date date not null,
  quantity integer not null,
  discount double null,
  shipping_type int not null
);


