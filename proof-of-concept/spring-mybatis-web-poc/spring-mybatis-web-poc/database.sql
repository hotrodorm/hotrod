create table cuenta (
  id integer not null generated always as identity,
  numero varchar(20) not null,
  tipo varchar(10) not null,
  saldo integer,
  creada_en date,
  primary key (id)
);

insert into cuenta (numero, tipo, saldo, creada_en) values ('5393', 'CC', 50, date('2016-01-01'));
insert into cuenta (numero, tipo, saldo, creada_en) values ('6924', 'AHORRO', 150, date('2016-02-01'));
insert into cuenta (numero, tipo, saldo, creada_en) values ('1292', 'CC', 250, date('2016-03-01'));
insert into cuenta (numero, tipo, saldo, creada_en) values ('7092', 'AHORRO', 350, date('2016-04-01'));
insert into cuenta (numero, tipo, saldo, creada_en) values ('4493', 'CC', null, date('2016-01-01'));
insert into cuenta (numero, tipo, saldo, creada_en) values ('8221', 'AHORRO', null, date('2016-01-02'));
insert into cuenta (numero, tipo, saldo, creada_en) values ('77453', 'JUBILAC',  100, date('2016-01-01'));
insert into cuenta (numero, tipo, saldo, creada_en) values ('1932', 'CC', null, date('2016-01-03'));
insert into cuenta (numero, tipo, saldo, creada_en) values ('99405', 'INVERS', 150, date('2016-02-01'));
insert into cuenta (numero, tipo, saldo, creada_en) values ('99283', 'INVERS', 150, date('2016-02-01'));
insert into cuenta (numero, tipo, saldo, creada_en) values ('6285', 'AHORRO', 150, date('2016-02-01'));
insert into cuenta (numero, tipo, saldo, creada_en) values ('2945', 'CC', 21050, date('2014-08-15'));
