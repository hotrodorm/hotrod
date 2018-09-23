create function employee_state_read_only() returns trigger as $BODY$
begin
    raise exception 'Insert, update, or delete operation not allowed on read-only table "employee_state".';
end;
$BODY$ language plpgsql;
//

create trigger employee_state_read_only before insert or update or delete on employee_state
  for each statement execute procedure employee_state_read_only();
//

create or replace function increment(i int) returns int as $BODY$
begin
  return i + 1;
end;
$BODY$ language plpgsql;
//

create or replace function lookup() 
  returns table (id int, name varchar)
as $BODY$
begin
  return query
  select a.id, a.name from account a;
end;
$BODY$ language plpgsql;
//

create or replace function lookupAccountTxs() 
  returns refcursor
as $BODY$
declare ref refcursor;
begin
  open ref for
  select a.*, t.amount 
    from account a
    join transaction t on t.account_id = a.id;
  return ref;
end;
$BODY$ language plpgsql;
//






















