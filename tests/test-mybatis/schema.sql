create table dt (
  id int primary key not null,
  d date,
  t time,
  ts timestamp,
  dt datetime,
  sdt smalldatetime
);

insert into dt (id, d, t, ts, dt, sdt) values (1, '2024-01-02', '14:15:16', '2024-02-15 12:34:56', '2024-02-16 12:34:56', '2024-02-17 12:34:56');
