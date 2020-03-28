-- === PostgreSQL 9.4 ===

drop table ride;

create table ride (
  car_id int,
  duration int,
  start_time timestamp,
  price int
);

insert into ride (car_id, duration, start_time, price) values (1, 100, '2018-01-01 12:00:00', 10);
insert into ride (car_id, duration, start_time, price) values (1, 50, '2018-01-01 12:01:00', 14);
insert into ride (car_id, duration, start_time, price) values (1, 20, '2018-01-01 12:02:00', 5);
insert into ride (car_id, duration, start_time, price) values (2, 100, '2018-01-01 12:10:00', 4);
insert into ride (car_id, duration, start_time, price) values (2, 100, '2018-01-01 12:11:00', 7);
insert into ride (car_id, duration, start_time, price) values (3, 40, '2018-01-01 12:10:00', 4);
insert into ride (car_id, duration, start_time, price) values (3, 15, '2018-01-01 12:11:00', 17);
insert into ride (car_id, duration, start_time, price) values (3, 5,  '2018-01-01 12:12:00', 14);
insert into ride (car_id, duration, start_time, price) values (3, 45, '2018-01-01 12:13:00', 14);
insert into ride (car_id, duration, start_time, price) values (4, 30, '2018-01-01 12:14:00', 10);
insert into ride (car_id, duration, start_time, price) values (5, 25, '2018-01-01 12:15:00', 20);

-- Partitions
select car_id, price, 
    sum(price) over (partition by car_id) as partition_total, 
    count(*) over (partition by car_id) as partition_count,
    row_number() over (partition by car_id) as partition_row_number
  from ride;

-- Running totals
select price, 
    sum(price) over (order by start_time) as running_total,
    count(*) over (order by start_time) as running_count,
    row_number() over (order by start_time) as running_row_number
  from ride;
  
-- Partitions and running totals
select car_id, price, 
    sum(price) over (partition by car_id order by start_time) as partition_running_total,
    count(*) over (partition by car_id order by start_time) as partition_running_count,
    row_number() over (partition by car_id order by start_time) as running_row_number
  from ride;
  
-- tiles (percentiles, quintiles, etc.)
select price,
    ntile(4) over (order by price) as tile4,
    ntile(5) over (order by price) as tile5,
    ntile(100) over (order by price) as tile100
    from ride;

-- Adjacent rows
select *,
    lag(price, 1) over(partition by car_id) as prev_price,
    lead(price, 1) over(partition by car_id) as next_price
  from ride;
    
-- Naming windows aliases
select car_id, price, 
    sum(price) over w1 as partition_running_total,
    count(*) over w1 as partition_running_count,
    row_number() over w1 as running_row_number
  from ride
  window w1 as (partition by car_id order by start_time);
  