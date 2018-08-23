create trigger employee_state_read_only 
  before insert or update or delete on employee_state
begin
  raise_application_error(-20001, 'Insert, update, or delete operation not allowed on read-only table "employee_state".');
end;
//