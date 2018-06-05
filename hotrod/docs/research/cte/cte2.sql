drop table persona;
drop table shopper_login;
drop table shopper;

create table shopper (
  id int primary key not null,
  name varchar(20),
  referred_by_id int,
  constraint fk1 foreign key (referred_by_id) references shopper (id)
);

insert into shopper (id, name, referred_by_id) values (1, 'Peter', null);
insert into shopper (id, name, referred_by_id) values (2, 'Anna', 1);
insert into shopper (id, name, referred_by_id) values (3, 'Mary', null);
insert into shopper (id, name, referred_by_id) values (4, 'John', 1);
insert into shopper (id, name, referred_by_id) values (5, 'Donna', 3);
insert into shopper (id, name, referred_by_id) values (6, 'Michael', 4);

create table shopper_login (
  user_id int,
  login_time timestamp,
  successful int,
  primary key (user_id, login_time),
  constraint fk2 foreign key (user_id) references shopper (id)
);

create table persona (
  user_id int primary key not null,
  description varchar(200),
  constraint fk3 foreign key (user_id) references shopper (id)
);

with 
  daily_login as ( -- 1. "CTE" (common table expression)
    select user_id, date(login_time) as day from shopper_login group by user_id, day
  ),
  frequent_user as ( -- 2. "Dependent CTE"
    select user_id, count(*) as days from daily_login group by user_id
    having count(*) >= 3
  ),
  recursive referrer2 (id) as (
    select id, referred_by_id from frequent_user
    union all
    select s.id, s.referred_by_id from referrer r join shopper s on s.id = r.referred_by_id
  )
select 
    s.name, f.days,
    (select description from persona p where p.user_id = s.id) as persona
  from frequent_user f
  join shopper s on s.id = f.user_id
  left join referrer r on r.id = s.id and 

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


