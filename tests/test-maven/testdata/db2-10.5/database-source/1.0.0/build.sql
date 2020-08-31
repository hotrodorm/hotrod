create table region (
  region_id int primary key not null,
  name varchar(20) not null unique,
  unified_code int not null,
  population bigint,
  constraint uq101 unique (unified_code)
);

create table city (
  city_id int primary key not null,
  city_name varchar(20) not null,
  code_1 int not null,
  code_2 varchar(6) not null,
  constraint fk20 foreign key (code_1) references region (region_id),
  constraint fk21 foreign key (code_1) references region (unified_code),
  constraint fk22 foreign key (code_2) references region (region_id),
  constraint fk23 foreign key (code_2) references region (unified_code),
  constraint fk24 foreign key (city_name) references region (name)
);







