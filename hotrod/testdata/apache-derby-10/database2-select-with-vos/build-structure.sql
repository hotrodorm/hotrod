
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
