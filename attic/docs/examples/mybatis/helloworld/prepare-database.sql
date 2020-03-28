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
