
-- Oracle defines two types of intervals: 
--  * abstract ones as INTERVAL YEAR TO MONTH
--  * concrete ones as INTERVAL DAY TO SECOND

-- =====================
-- 1. Abstract Intervals
-- =====================

-- Data type: INTERVAL YEAR [(year_precision)] TO MONTH

-- Literals : INTERVAL 'year-month' YEAR [(precision)] TO MONTH
--            INTERVAL 'year' YEAR [(precision)]
--            INTERVAL 'month' MONTH

create table t1 (
  a interval year(4) to month -- year precision defaults to 2 for YEAR and also for MONTH
);

insert into t1 (a) values (interval '17' year); -- 17 years
insert into t1 (a) values (interval '105' year(3)); -- 105 years; YEAR(3) since the precision is bigger than the default one.

insert into t1 (a) values (interval '8' month); -- 8 months
insert into t1 (a) values (interval '120' month); -- 120 months, automatically converted to '10-0'

insert into t1 (a) values (interval '4-10' year to month); -- 4 years and 10 months. This notation accepts month between 0 and 11.
insert into t1 (a) values (interval '120-11' year(3) to month); -- YEAR(3) since the default precision is 2.

insert into t1 (a) values (interval '107' year); -- Invalid: precision cannot accommodate this value.
insert into t1 (a) values (interval '6-12' year to month); -- Invalid: this notation only accepts months with value 0 to 11.

-- Abstract Interval Arithmetic

select interval '12' year + interval '5' year from dual; -- 17-0
select interval '12' year + interval '5' month from dual; -- 12-5
select interval '12' month + interval '5' month from dual; -- 1-5
select interval '12-3' year to month + interval '5-1' year to month from dual; -- 17-4
select interval '12-3' year to month + interval '2' year from dual; -- 14-3
select interval '12-3' year to month + interval '2' month from dual; -- 12-5

select interval '12-3' year to month - interval '5-1' year to month from dual; -- 7-2

select interval '12' year * 2.123 from dual; -- 25-5

select interval '12' year / 2.123 from dual; -- 5-7
 
select interval '12-3' year to month + INTERVAL '4 5:06' DAY TO MINUTE from dual; -- Error: ORA-30081: invalid data type for datetime/interval arithmetic 

-- =====================
-- 2. Concrete Intervals
-- =====================
  
-- Data type: INTERVAL DAY [(day_precision)] TO SECOND [(fractional_seconds_precision)]
              day_precision defaults to 2; fractional_seconds_precision defaults to 6 decimal places

-- Literals : INTERVAL leading (leading_precision) to trailing(fractional_seconds_precision)

create table t2 (
  a interval day(3) to second -- day precision set to 3, second precision set to 6.
);

insert into t2 (a) values (INTERVAL '5' DAY); -- 5 days.
insert into t2 (a) values (INTERVAL '999' DAY(3)); -- 999 days.

insert into t2 (a) values (INTERVAL '8' HOUR); -- 8 hours.
insert into t2 (a) values (INTERVAL '40' HOUR); -- 40 hours (1 day 16 hours)
insert into t2 (a) values (INTERVAL '250' HOUR(3)); -- 250 hours (10 days 10 hours)

insert into t2 (a) values (INTERVAL '30' MINUTE); -- 30 minutes.
insert into t2 (a) values (INTERVAL '135' MINUTE); -- 135 minutes (2 hours 15 minutes)

insert into t2 (a) values (INTERVAL '7.123456' SECOND); -- 7.123456 s
insert into t2 (a) values (INTERVAL '7.12345678' SECOND); -- 7.123457 s (rounded since precision is 6)

insert into t2 (a) values (INTERVAL '11 10:09:08.555' DAY TO SECOND(3)); -- 11 days, 10 hours, 09 minutes, 08 seconds, and 555 ms.
insert into t2 (a) values (INTERVAL '11 10:09' DAY TO MINUTE); -- 11 days, 10 hours and 09 minutes.
insert into t2 (a) values (INTERVAL '100 10' DAY(3) TO HOUR); -- 100 days 10 hours.
insert into t2 (a) values (INTERVAL '09:08:07.6666666' HOUR TO SECOND(7)); -- 9 hours, 08 minutes, and 7.6666666 seconds.
insert into t2 (a) values (INTERVAL '09:30' HOUR TO MINUTE); -- 9 hours and 30 minutes.
insert into t2 (a) values (INTERVAL '15:30' MINUTE TO SECOND); -- 15 minutes 30 seconds.
insert into t2 (a) values (INTERVAL '15.6789' SECOND(2,3)); --  Rounded to 15.679 seconds. Because the precision is 3, the fractional second ‘6789’ is rounded to ‘679’  

-- Concrete Interval Arithmetic

select INTERVAL '1 2:03' DAY TO MINUTE + INTERVAL '4 5:06' DAY TO MINUTE from dual; -- 5 days 7 hours 9 minutes  
select INTERVAL '4 5:06' DAY TO MINUTE - INTERVAL '1 3:02' DAY TO MINUTE from dual; -- 3 days 2 hours 4 minutes
select INTERVAL '4 5:06' DAY TO MINUTE * 2 from dual; -- 8 days 10 hours 12 minutes
select INTERVAL '4 5:06' DAY TO MINUTE * 2.123 from dual; -- 8 days 22 hours 38 minutes 7.08 s
select 2.123 * INTERVAL '4 5:06' DAY TO MINUTE from dual; -- 8 days 22 hours 38 minutes 7.08 s
select INTERVAL '2 5:06' DAY TO MINUTE / 2 from dual; -- 1 day 2 hours 33 minutes
select INTERVAL '2 5:06' DAY TO MINUTE / 2.123 from dual; -- 1 days 1 hour 0 minutes 42.3928 s

select INTERVAL '4 5:06' DAY TO MINUTE + INTERVAL '15:30' MINUTE TO SECOND from dual; -- 4 days 5 hours 21 minutes 30 s

-- Arithmetic against DATE

select current_date + interval '12-3' year to month from dual; -- 2031-09-20 15:11:45.0
select current_date + INTERVAL '4 5:06' DAY TO MINUTE from dual; -- 2019-06-24 20:18:10.0

-- Arithmetic against TIMESTAMP (no time zone)

select localtimestamp + interval '12-3' year to month from dual; -- 2031-09-20 15:13:45.941165
select localtimestamp + INTERVAL '4 5:06' DAY TO MINUTE from dual; -- 2019-06-24 20:19:58.855

-- Arithmetic against TIMESTAMP (with time zone)

select current_timestamp + interval '12-3' year to month from dual; -- 2031-09-20 15:13:45.941165
select current_timestamp + INTERVAL '4 5:06' DAY TO MINUTE from dual; -- 2019-06-24 20:19:58.855


_________________________________________________________________________________________

alter session set time_zone = '-04:00';
drop table t3;
create table t3 (
  id varchar2(30),
  d date,
  t timestamp,
  tz timestamp with time zone,
  tlz timestamp with local time zone
);
insert into t3 (id, d, t, tz, tlz) values('current_timestamp', current_timestamp, current_timestamp, current_timestamp, current_timestamp);
insert into t3 (id, d, t, tz, tlz) values('localtimestamp', localtimestamp, localtimestamp, localtimestamp, localtimestamp);
insert into t3 (id, d, t, tz, tlz) values('current_date', current_date, current_date, current_date, current_date);

alter session set time_zone = '+03:00';
select id, t, tz, tlz from t3;

 ID                T                          TZ                         TLZ                        
 ----------------- -------------------------- -------------------------- -------------------------- 
 current_timestamp 2019-06-20 15:41:18.204518 2019-06-20 15:41:18.204518 2019-06-20 15:41:18.204518 
 localtimestamp    2019-06-20 15:41:18.208161 2019-06-20 15:41:18.208161 2019-06-20 15:41:18.208161 
 current_date      2019-06-20 15:41:18.0      2019-06-20 15:41:18.0      2019-06-20 15:41:18.0      
 
alter session set time_zone = '+03:00';
select id, '' || d as d , '' || t as t, '' || tz as tz, '' || tlz as tlz from t3;

 ID                D         T                            TZ                                  TLZ                          
 ----------------- --------- ---------------------------- ----------------------------------- ---------------------------- 
 current_timestamp 20-JUN-19 20-JUN-19 03.41.18.204518 PM 20-JUN-19 03.41.18.204518 PM -04:00 20-JUN-19 10.41.18.204518 PM 
 localtimestamp    20-JUN-19 20-JUN-19 03.41.18.208161 PM 20-JUN-19 03.41.18.208161 PM -04:00 20-JUN-19 10.41.18.208161 PM 
 current_date      20-JUN-19 20-JUN-19 03.41.18.000000 PM 20-JUN-19 03.41.18.000000 PM -04:00 20-JUN-19 10.41.18.000000 PM 
 


_____________________________________________________________________


date()
time()
datetime()
date(dt)
time(dt)
datetime(d, t)
_____________________

time(iv) : time (ignores date part)
interval(d, t) : interval (days + time)

diff(d1, d2) : int, interval? (days, d1 - d2)
diff(t1, t2) : interval? (t1- t2)
diff(dt1, dt2) : interval (dt1 - dt2)
diff(iv1, iv2) : interval

add(d, int/interval?) : date
add(t, interval?): time
add(dt, interval) : datetime
add(iv, interval) : interval

mult(iv, scalar) : interval



















  
  
  
  
  
  
  
  
  
  
  
