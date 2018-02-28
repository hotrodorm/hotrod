-- Cannot enable/disable triggers in MySQL, so this option is not useful.

-- create trigger employee_state_insert_read_only before insert on employee_state for each row
-- begin
-- 	signal sqlstate '20001' set message_text = 'The employee_state table is a read only table. Inserts, updates, and deletes are not allowed.';
-- end //

-- create trigger employee_state_update_read_only before update on employee_state for each row
-- begin
--   signal sqlstate '20002' set message_text = 'The employee_state table is a read only table. Inserts, updates, and deletes are not allowed.';
-- end //

-- create trigger employee_state_delete_read_only before delete on employee_state for each row
-- begin
--   signal sqlstate '20003' set message_text = 'The employee_state table is a read only table. Inserts, updates, and deletes are not allowed.';
-- end //
