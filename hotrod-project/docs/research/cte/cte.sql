drop table payment;

drop table artist;

drop table representative;

create table representative (
  id int primary key not null,
  public_name varchar(50) not null
);

create table artist (
  id int primary key not null,
  name varchar(20) not null,
  rep_id int not null,
  constraint fk_rep foreign key (rep_id) references representative (id)
);

create table payment (
  payment_id int primary key not null,
  ticket_number int not null,
  venue_id int not null,
  artist_id int not null,
  amount int not null,
  constraint fk_artist foreign key (artist_id) references artist (id)
);

create table top_venue (
  id int primary key not null,
  type varchar(20) not null
);

with ticket as ( -- CTE (common table expression)
    select ticket_number, max(venue_id) as venue_id, max(artist_id) as artist_id, sum(amount) as paid 
      from payment 
      group by ticket_number
  ),
  event as ( -- Dependent CTE
    select ...
  ),
select -- primary statement
    a.id, a.name, v.avg_paid, ( -- "scalar" subquery (returns a single value)
      select public_name from representative r where r.id = a.rep_id
    ) as rep_name
  from artist a
  join ( -- "from" subquery (temp table or nested select?); cannot be correlated, since it's evaluated only once.
    select venue_id, artist_id, avg(paid) as avg_paid
      from ticket 
      where paid > 0
      group by venue_id, artist_id
    ) v
    on v.artist_id = a.id
  where v.venue_id in ( -- "typical" subquery (typical old-school)
    select id from top_venue where type = 'C'
    )
  ;

-- ================================================================================

with 
  daily_sales as ( 
    -- 1. "CTE" (common table expression)
    select sale_date, person_id, sum(sale_amount) as daily_amount, max(branch_id) from sale group by sale_date, person_id
  ),
  person_avg as ( 
    -- 2. "Dependent CTE"
    select person_id, avg(daily_amount) as avg_amount, max(branch_id) from daily_sales group by person_id, sale_date
  ),
  coworker as (
    -- 3. "Recursive CTE"
    select ...
  ),
select
    a.person_id, a.avg_amount, ( 
      -- 4. "Correlated Scalar" subquery (returns a single value)
      select name from salesperson where id = a.person_id
    ) as name
  from person_avg a
  join ( 
    -- 5. "Tabular" subquery (temp table/nested select); cannot be correlated, since it's evaluated only once.
    select person_id, sum(amount) as expense_amount from daily_expense where type <> 'travel' group by person_id
    ) e on e.person_id = a.person_id
  where a.avg_amount > (
      -- 6. Typical "old-school correlated" subquery
      select avg(sale_amount) from branch_sales b where b.id = a.branch_id
    )
    and a.person_id not in ( 
      -- 7. Typical "old-school non-correlated" subquery
      select person_id from inactive_person
    )
  
-- 1. CTE  

with 
  daily_sales as ( -- 1. CTE (common table expression)
    select sale_date, person_id, sum(sale_amount) as daily_amount from sale group by sale_date, person_id
  )
select -- primary statement
    a.person_id, a.avg_amount
  from person_avg a;

-- 2. Dependent CTE

with 
  daily_sales as ( -- 1. CTE (common table expression)
    select sale_date, person_id, sum(sale_amount) as daily_amount from sale group by sale_date, person_id
  ),
  person_avg as ( -- 2. Dependent CTE
    select person_id, avg(daily_amount) as avg_amount from daily_sales group by person_id, sale_date
  )
select -- primary statement
    a.person_id, a.avg_amount
  from person_avg a;

-- 3. Scalar subquery

select -- primary statement
    sale_date, person_id, sale_amount, ( -- 3. Scalar subquery (returns a single value)
      select name from salesperson where id = a.person_id
    ) as name
  from sale

-- 4. From subquery


  

























