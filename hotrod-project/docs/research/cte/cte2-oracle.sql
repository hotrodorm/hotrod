drop table persona;
drop table shopper_login;
drop table shopper;

create table shopper (
  id int primary key not null,
  name varchar(20),
  ref_id int,
  constraint fk1 foreign key (ref_id) references shopper (id)
);

insert into shopper (id, name, ref_id) values (1, 'Peter', null);
insert into shopper (id, name, ref_id) values (2, 'Anna', 1);
insert into shopper (id, name, ref_id) values (3, 'Mary', null);
insert into shopper (id, name, ref_id) values (4, 'John', 1);
insert into shopper (id, name, ref_id) values (5, 'Donna', 3);
insert into shopper (id, name, ref_id) values (6, 'Michael', 4);

create table shopper_login (
  user_id int,
  login_time date,
  success int,
  primary key (user_id, login_time),
  constraint fk2 foreign key (user_id) references shopper (id)
);

insert into shopper_login (user_id, login_time, success) values (3, timestamp '2018-06-20 10:01:00', 1);
insert into shopper_login (user_id, login_time, success) values (3, timestamp '2018-06-21 17:30:00', 1);
insert into shopper_login (user_id, login_time, success) values (3, timestamp '2018-06-22 18:40:00', 1);
insert into shopper_login (user_id, login_time, success) values (5, timestamp '2018-06-25 10:01:00', 1);
insert into shopper_login (user_id, login_time, success) values (5, timestamp '2018-06-26 17:30:00', 1);
insert into shopper_login (user_id, login_time, success) values (5, timestamp '2018-06-27 18:40:00', 1);
insert into shopper_login (user_id, login_time, success) values (6, timestamp '2018-06-28 10:01:00', 1);
insert into shopper_login (user_id, login_time, success) values (6, timestamp '2018-06-29 17:30:00', 1);
insert into shopper_login (user_id, login_time, success) values (6, timestamp '2018-06-29 18:40:00', 1);
insert into shopper_login (user_id, login_time, success) values (4, timestamp '2018-06-19 12:34:56', 1);
insert into shopper_login (user_id, login_time, success) values (4, timestamp '2018-06-19 15:01:02', 1);


create table persona (
  user_id int primary key not null,
  description varchar(200),
  constraint fk3 foreign key (user_id) references shopper (id)
);

insert into persona (user_id, description) values (1, 'Standard');
insert into persona (user_id, description) values (2, 'Pricy');
insert into persona (user_id, description) values (3, 'Fashionista');

with 
  daily_login as ( -- 1. Independent CTE
    select user_id, trunc(login_time) as day, count(*) from shopper_login group by user_id, trunc(login_time)
  ),
  frequent_user as ( -- 2. Dependent CTE
    select user_id, count(*) as days from daily_login group by user_id
    having count(*) >= 2
  ),
  referrer (frequent_id, id, rid, ref_level) as ( -- 3. Recursive CTE
    select fu.user_id, s.id, s.ref_id, 1 from frequent_user fu join shopper s on fu.user_id = s.id
    union all
    select r.frequent_id, s.id, s.ref_id, r.ref_level + 1 from referrer r join shopper s on s.id = r.rid
  )
select s.id, s.name, r.id as original_referrer,
    ( -- 4. Scalar Subquery
    select max(login_time) from shopper_login l where l.user_id = s.id and l.success = 1
    ) as last_login,
    m.first_login
  from shopper s
  join referrer r on r.frequent_id = s.id
  join ( -- 5. Derived Table / Table Expression / Inline View
    select user_id, min(login_time) first_login from shopper_login where success = 1 group by user_id
  ) m on m.user_id = s.id
  where r.rid is null
    and s.id not in ( -- 6. Nested Subquery
      select user_id from persona where description = 'Fashionista'
    );




-- ============================================================================
 
  
-- Does not work!

with mshopper (id, referred_by_id) as (
    select id, referred_by_id from shopper where left(name, 1) = 'M'
  ),
  recursive referrer (id, referred_by_id, client, initial) as (
    select id, referred_by_id, name, name from mshopper
    union all
    select s.id, s.referred_by_id, r.client, s.name from referrer r join shopper s on s.id = r.referred_by_id
)
select * from referrer

-- Works!

with recursive referrer (id, referred_by_id, client, initial) as (
    select id, referred_by_id, name, name from shopper where left(name, 1) = 'M'
    union all
    select s.id, s.referred_by_id, r.client, s.name from referrer r join shopper s on s.id = r.referred_by_id
)
select * from referrer where referred_by_id is null order by client

-- ================= ORACLE WORKS ===================

with referrer (id, referred_by_id, client, original) as (
    select id, referred_by_id, name, name from shopper where substr(name, 1, 1) = 'M'
    union all
    select s.id, s.referred_by_id, r.client, s.name from referrer r join shopper s on s.id = r.referred_by_id
)
select * from referrer where referred_by_id is null order by client


with referrer (id, referred_by_id, client, original) as (
    select id, referred_by_id, name, name from shopper where substr(name, 1, 1) = 'M'
    union all
    select s.id, s.referred_by_id, r.client, s.name from referrer r join shopper s on s.id = r.referred_by_id
    -- select 1, 2, 'abc', 'def' from dual
)
select * from referrer 


