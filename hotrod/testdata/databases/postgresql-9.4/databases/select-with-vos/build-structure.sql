
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
  constraint fk100 foreign key (person_id) references person (id)
);

create table "transaction" (
  id integer primary key not null,
  executed_on date not null,
  amount numeric(10,2) not null,
  account_id integer not null,
  type smallint not null, -- 0:cashier, 1:online, 2:ATM, 3:third party
  constraint fk200 foreign key (account_id) references account (id)
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

create table kind (
  id int primary key,
  caption varchar(60)
);

create table brand (
  id serial primary key,
  name varchar(40) not null unique,
  kind_id int constraint fk1 references kind
);

create table car (
  id serial primary key,
  brand_id int constraint fk2 references brand,
  type varchar(10)
);

create view van as
  select * from car where type = 'VAN';
  
create table repair (
  id int primary key,
  repaired_on timestamp,
  car_id int constraint fk3 references car
);
    
    


