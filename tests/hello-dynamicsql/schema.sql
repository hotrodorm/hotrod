create table employee (
  id int primary key not null,
  first_name varchar(20),
  last_name varchar(20),
  hired_on date,
  active char(1),
  city_id int,
  title varchar(30)  
);
