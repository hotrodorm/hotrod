create trigger employee_state_read_only before insert or update or delete on employee_state for each row
begin
  raise_application_error(-20001,'The employee_state table is a read only table. Inserts, updates, and deletes are not allowed.');
end;
