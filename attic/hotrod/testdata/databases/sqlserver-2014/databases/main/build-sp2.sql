create trigger employee_state_read_only on employee_state
instead of insert, update, delete as
begin
  raiserror( 'Insert, update, or delete operation not allowed on read-only table "employee_state".', 16, 1)
  rollback transaction
end
//