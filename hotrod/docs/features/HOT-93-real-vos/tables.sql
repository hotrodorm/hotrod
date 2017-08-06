create table persona (
  id number(10) primary key not null,
  nombre varchar2(30) not null,
  tipo number(3) not null -- 1:individual, 2:commercial, 3:gubernamental
);

create table cuenta (
  id number(10) primary key not null,
  nro_cuenta varchar2(30) not null,
  id_persona number(10) not null,
  tipo number(3) not null, -- 1:cc, 2:ahorro, 3:inversiones; 4:jubilación
  activa number(1) not null, -- 0:inactiva, 1:activa
  saldo number(14,2) not null,
  constraint fk1 foreign key (id_persona) references persona (id)
);

create table transaccion (
  id number(18) primary key not null,
  ejecutada date not null,
  monto number(10,2) not null,
  id_cuenta number(10) not null,
  tipo number(2) not null, -- 0:caja, 1:online, 2:cajero automático, 3:terceros
  constraint fk2 foreign key (id_cuenta) references cuenta (id)
);

drop table transaccion;
drop table cuenta;
drop table persona;