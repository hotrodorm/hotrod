create or replace procedure sp1 (
  cursor_accounts out sys_refcursor,
  net in number(10),
  gross out number(10),
  total in out number(10)
) is begin
  gross := net * 1.16;
  total := total + gross;
  open cursor_accounts for select * from account;
end;
//

create or replace procedure sp2 (
  gross in number(10)
) is begin
	dbms_output.put_line('Hello World!');
end;
//

-- Function that returns a cursor

create or replace function get_accounts
  return sys_refcursor
as 
  cursor1 sys_refcursor;
begin
  open cursor1 for select * from account;
  return cursor1;
end get_accounts;
//




























