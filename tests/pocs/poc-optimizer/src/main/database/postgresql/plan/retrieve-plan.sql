create temp table plan(t jsonb);

$delim$

do $block$ 
declare l_sql text := $sql$
select status_code, max(order_id) as q from order_item where id between 5050 and 5380 group by status_code having sum(quantity) > 2;
$sql$;
  l_result jsonb;
begin 
  execute 'explain (format json) '|| l_sql into l_result;
  delete from plan;
  insert into plan (t) values (l_result);
end $block$;
$delim$

