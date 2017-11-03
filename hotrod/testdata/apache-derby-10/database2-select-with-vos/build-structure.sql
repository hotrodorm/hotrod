
create table category (
  id integer primary key not null,
  name varchar(40) not null,
  percent_interest numeric(5,2) not null,
  interest_free_days integer not null
);

create table person (
  id integer primary key not null,
  name varchar(30) not null,
  type smallint not null, -- 1:individual, 2:commercial, 3:government
  category_id integer not null,
  constraint fk_person_category foreign key (category_id) references category (id)
);

create table account (
  id integer primary key not null,
  account_number varchar(30) not null,
  person_id integer not null,
  type smallint not null, -- 1:checking, 2:savings, 3:investments; 4:retirement
  active smallint not null, -- 0:inactive, 1:active
  balance numeric(14,2) not null,
  constraint fk1 foreign key (person_id) references person (id)
);

create table "transaction" (
  id integer primary key not null,
  executed_on date not null,
  amount numeric(10,2) not null,
  account_id integer not null,
  type smallint not null, -- 0:cashier, 1:online, 2:ATM, 3:third party
  constraint fk2 foreign key (account_id) references account (id)
);

create table log (
  recorded_at timestamp not null,
  note varchar(5000) not null,
  recorded_by varchar(20) not null,
  office_id integer
);

create table office (
  id integer primary key not null,
  name varchar(40) not null,
  region varchar(10) not null check (region in ('NORTH', 'SOUTH', 'EAST'))
);

create view north_office as select * from office where region = 'NORTH';

create table document (
  id integer primary key not null,
  content xml not null
);
  
create table doc_comment (
  id integer primary key not null,
  doc_id integer not null,
  notes varchar(1000) not null
);

-- Car Example

create table brand (
  id integer primary key not null 
    generated always as identity,
  name varchar(40) not null,
  unique (name)
);

create table car (
  id integer primary key not null 
    generated always as identity,
  brand_id integer not null,
  type varchar(10) not null,
  constraint fk_car_brand foreign key
    (brand_id) references brand (id)
);

create view van as
  select * from car where type = 'VAN';
  
create table repair (
  id integer primary key not null,
  repaired_on timestamp not null,
  car_id integer not null,
  constraint fk_repair_car foreign key 
    (car_id) references car (id)
);
    
    


