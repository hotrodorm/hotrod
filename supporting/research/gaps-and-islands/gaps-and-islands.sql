drop table island;

create table island (
  id int,
  segment int,
  x_start int,
  x_end int,
  height int
);

insert into island (id, segment, x_start, x_end, height) values (1, 10, 1, 4, 500);
insert into island (id, segment, x_start, x_end, height) values (2, 10, 1, 2, 501);
insert into island (id, segment, x_start, x_end, height) values (3, 10, 2, 3, 502);
insert into island (id, segment, x_start, x_end, height) values (4, 10, 3, 4, 503);
insert into island (id, segment, x_start, x_end, height) values (5, 10, 5, 8, 504);
insert into island (id, segment, x_start, x_end, height) values (5, 10, 5, 6, 505);
insert into island (id, segment, x_start, x_end, height) values (6, 10, 7, 9, 506);
insert into island (id, segment, x_start, x_end, height) values (10, 12, 3, 5, 400);
insert into island (id, segment, x_start, x_end, height) values (11, 12, 1, 3, 401);
insert into island (id, segment, x_start, x_end, height) values (12, 12, 4, 6, 402);
insert into island (id, segment, x_start, x_end, height) values (13, 12, 7, 9, 403);
insert into island (id, segment, x_start, x_end, height) values (14, 12, 7, 9, 404);
insert into island (id, segment, x_start, x_end, height) values (15, 12, 8, 10, 405);

with 
x as (
  select
    id, segment, x_start, x_end, height,
    -- max(x_end) over(partition by segment order by x_start, id rows between unbounded preceding and 1 preceding) as max_endw,
    case when max(x_end) over(partition by segment order by x_start, id rows between unbounded preceding and 1 preceding) < x_start then 1 else 0 end as incw
  from island 
),
y as (
  select
    id, segment, x_start, x_end, height,
    sum(incw) over(partition by segment order by x_start, id) as grp
  from x
)
select segment, grp, min(x_start) as s, max(x_end) as e, min(height) as min_h, max(height) as max_h
from y
group by segment, grp
order by segment, grp

_______________________________________________

-- 	segment  grp  s   e  min_h  max_h
-- 	-------  ---  -  --  -----  -----
-- 	     10    0  1   4    500    503          
-- 	     10    1  5   9    504    506          
-- 	     12    0  1   6    400    402          
-- 	     12    1  7  10    403    405          

