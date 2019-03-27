drop table if exists cuenta;
drop table if exists cliente;

create table cliente (
  id integer identity primary key not null,
  nombre varchar(20) not null,
  constraint uq_cliente_nombre unique (nombre)
);

insert into cliente (id, nombre) values (10, 'Pedro');
insert into cliente (id, nombre) values (20, 'Juan');
insert into cliente (id, nombre) values (30, 'Diego');

create table cuenta (
  num_cta varchar(20) not null,
  id_cliente integer,
  saldo integer,
  creada_en date,
  primary key (num_cta),
  constraint fk1 foreign key (id_cliente) references cliente (id)
);

insert into cuenta (num_cta, id_cliente, creada_en, saldo) values ('C01', 10, '2013-07-25',  5000);
insert into cuenta (num_cta, id_cliente, creada_en, saldo) values ('C02', 20, '2015-03-05', 12000);
insert into cuenta (num_cta, id_cliente, creada_en, saldo) values ('A08', 20, '2016-08-17',  3000);
insert into cuenta (num_cta, id_cliente, creada_en, saldo) values ('C03', 30, '2014-12-20', 71000);
