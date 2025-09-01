create table code (
  id integer primary key not null,
  caption varchar(20) not null,
  type varchar(20) not null
);

create table address (
  id integer primary key not null,
  line1 varchar(40) not null,
  line2 varchar(40),
  city varchar(30),
  state char(2),
  zip_code char(5)
);

create table shipment (
  id integer primary key not null,
  address_id integer not null,
  shipped_on date,
  constraint fk_shipment_address foreign key (address_id) references address (id)
);

create table product (
  id integer primary key not null,
  description varchar(80) not null
);

create table customer (
  id integer primary key not null,
  first_name varchar(20) not null,
  last_name varchar(20) not null,
  phone_number varchar(20) not null,
  address_id integer not null,
  constraint fk_customer_address foreign key (address_id) references address (id)
);

create index ix_customer_first on customer (first_name);

create index ix_customer_last on customer (last_name);

create table "order" (
  id integer primary key not null,
  customer_id integer not null,
  placed date not null,
  shipment_id integer,
  status_code integer not null,
  constraint fk_order_customer foreign key (customer_id) references customer (id),
  constraint fk_order_shipment foreign key (shipment_id) references shipment (id),
  constraint fk_order_status foreign key (status_code) references code (id)
);

create index ix_order_placed on "order" (placed);

create table order_item (
  id integer primary key not null,
  order_id integer not null,
  product_id integer,
  quantity integer not null,
  status_code integer not null,
  deferred_shipment_date date,
  constraint fk_item_order foreign key (order_id) references "order" (id),
  constraint fk_order_product foreign key (product_id) references product (id),
  constraint fk_item_status foreign key (status_code) references code (id)
);

