create function employee_state_read_only() returns trigger as $sp_delimiter$
begin
    raise exception 'Insert, update, or delete operation not allowed on read-only table "employee_state".';
end;
$sp_delimiter$ language plpgsql;
//

create trigger employee_state_read_only before insert or update or delete on employee_state
  for each statement execute procedure employee_state_read_only();
//
