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
  )
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



