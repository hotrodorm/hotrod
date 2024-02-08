drop table if exists invoice_line; 
drop table if exists invoice; 
drop table if exists customer;
drop table if exists product; 
drop table if exists category; 

create table customer (
  id int primary key not null,
  name varchar(15) not null
);

insert into customer (id, name) values
  (1, 'John Smith'),
  (2, 'Peter Ubisie'),
  (3, 'Anne With an E');

create table invoice (
  id int primary key not null,
  customer_id int references customer (id),
  purchase_date date,
  paid int
);

insert into invoice (id, customer_id, purchase_date, paid) values
  (1014, 1, date '2024-01-15', 1),
  (1015, 2, date '2024-01-16', 0),
  (1016, 2, date '2024-01-16', 1);

create table category (
  id int primary key not null,
  name varchar(10)
);

insert into category (id, name) values
  (50, 'Gadgets'),
  (51, 'Music');

create table product (
  id int primary key not null,
  name varchar(20),
  category_id int references category (id)
);

insert into product (id, name, category_id) values
  (20, 'Daguerrotype', 50),
  (21, 'Piano Forte', 51);

create table invoice_line (
  invoice_id int references invoice (id),
  product_id int references product (id),
  qty int
);

insert into invoice_line (invoice_id, product_id, qty) values
  (1014, 21, 1),
  (1015, 20, 1),
  (1015, 21, 2);
