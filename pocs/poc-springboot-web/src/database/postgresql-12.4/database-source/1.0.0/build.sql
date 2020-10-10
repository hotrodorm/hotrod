create table product (
  id bigint primary key not null,
  name varchar(20) not null,
  price int not null default 25
);

create table historic_price (
  product_id int not null,
  from_date date not null,
  price int not null,
  primary key (product_id, from_date),
  constraint fk1 foreign key (product_id) references product (id)
);


