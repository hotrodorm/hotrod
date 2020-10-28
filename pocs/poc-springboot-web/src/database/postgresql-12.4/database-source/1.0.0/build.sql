create table product (
  id bigint primary key not null,
  name varchar(20) not null,
  price int not null default 25,
  sku bigint unique not null
);

create table historic_price (
  product_id int not null,
  from_date date not null,
  price int not null,
  sku bigint,
  primary key (product_id, from_date),
  constraint fk1 foreign key (product_id) references product (id),
  constraint fk2 foreign key (sku) references product (sku)
);

create table account (
  id serial not null,
  name varchar(100) not null,
  type varchar(10) not null,
  current_balance integer not null,
  created_on timestamp not null,
  active int not null,
  primary key (id),
  constraint account_name_uc unique (name)
);

create table item (
  id int,
  description varchar(200),
  price decimal(14, 2),
  created_on timestamp,
  active boolean,
  icon bytea, -- blob
  store_code uuid -- object
);

create table island (
  id int,
  segment int,
  x_start int,
  x_end int,
  height int
);

create table federal_branch (
  id integer not null,
  name varchar(30) not null,
  primary key (id)
);

create table transaction (
  account_id integer not null,
  seq_id serial not null,
  time varchar(16) not null,
  amount integer not null,
  fed_branch_id bigint,
  primary key (seq_id),
  constraint tx_account_id_time unique (account_id, time),
  constraint fk_tx_account1 foreign key (account_id) 
    references account (id),
  constraint fk_tx_fed_branch foreign key (fed_branch_id)
    references federal_branch (id)
);

create table types_binary (
  bin1 bytea, -- this is like a blob
  bol1 boolean
);

create type complex as (
  r double precision,
  t double precision
);

create table types_other (
  geo1 point,
  geo2 line,
  geo3 lseg,
  geo4 box,
  geo5 path,
  geo6 polygon,
  geo7 circle,
  net1 cidr, -- net specs
  net2 inet,
  net3 macaddr,
  uui1 uuid, -- universally unique identifier
  jso1 json,
  jso2 jsonb,
  arr1 integer[],
  arr2 char[][],
  arr3 integer array,
  com1 complex,
  ran1 int4range,
  ran2 int8range,
  ran3 numrange,
  ran4 tsrange,
  ran5 tstzrange,
  ran6 daterange
);  