create trigger tri_employee_state_readonly_insert
before insert on employee_state
for each row
begin
  if coalesce(@read_only_employee_state_disabled, 0) <> 1 then
    signal sqlstate '45001' set message_text = 'Insert not allowed on read-only table "employee_state".';
  end if;
end//

create trigger tri_employee_state_readonly_update
before update on employee_state
for each row
begin
  if coalesce(@read_only_employee_state_disabled, 0) <> 1 then
    signal sqlstate '45001' set message_text = 'Update not allowed on read-only table "employee_state".';
  end if;
end//

create trigger tri_employee_state_readonly_delete
before delete on employee_state
for each row
begin
  if coalesce(@read_only_employee_state_disabled, 0) <> 1 then
    signal sqlstate '45001' set message_text = 'Delete not allowed on read-only table "employee_state".';
  end if;
end//