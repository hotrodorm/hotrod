create function employee_state_read_only() returns trigger as $sp_delimiter$
  begin
    raise exception 'The employee_state table is a read only table. Inserts, updates, and deletes are not allowed.';
  end;
$sp_delimiter$ language plpgsql;
//

create trigger employee_state_read_only before insert or update or delete on employee_state
  for each row execute procedure employee_state_read_only();
//
