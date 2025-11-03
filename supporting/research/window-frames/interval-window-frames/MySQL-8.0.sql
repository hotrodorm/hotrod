create table quote (
  id int,
  price double precision,
  recorded timestamp
);

insert into quote (id, price, recorded) values (1, 13, timestamp '2019-01-02 03:00:00');
insert into quote (id, price, recorded) values (1, 15, timestamp '2019-01-02 03:15:00');
insert into quote (id, price, recorded) values (1, 14, timestamp '2019-01-02 03:18:00');
insert into quote (id, price, recorded) values (1, 19, timestamp '2019-01-02 04:00:00');
insert into quote (id, price, recorded) values (1, 21, timestamp '2019-01-02 04:20:00');
insert into quote (id, price, recorded) values (1, 18, timestamp '2019-01-02 04:40:00');
insert into quote (id, price, recorded) values (1, 20, timestamp '2019-01-02 05:00:00');
insert into quote (id, price, recorded) values (1, 25, timestamp '2019-01-02 07:20:00');
insert into quote (id, price, recorded) values (2, 100, timestamp '2019-01-02 03:40:00');
insert into quote (id, price, recorded) values (2, 102, timestamp '2019-01-02 03:45:00');
insert into quote (id, price, recorded) values (2, 99, timestamp '2019-01-02 03:50:00');
insert into quote (id, price, recorded) values (2, 90, timestamp '2019-01-02 05:30:00');
insert into quote (id, price, recorded) values (2, 84, timestamp '2019-01-02 05:45:00');

select id, price, recorded,
  max(price) over (
    partition by id 
    order by recorded
    range between interval 30 minute preceding and current row
    ) as moving_max
from quote
order by id, recorded

