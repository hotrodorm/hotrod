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

-- Multi-type parameters

create or replace procedure params1() is
BEGIN
    dbms_output.put_line('Hello World ');
END;
//


create or replace procedure params2 (
  account_name in varchar2(20),
  balance_sum inout number(9),
  balance out number(9)
) is
begin
	select current_balance into balance_out
	  from account
	  where name = account_name;
end;
//




























