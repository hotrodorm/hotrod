create or replace procedure sp1 (
  cursor_accoutns out sys_refcursor,
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

create or replace function get_lines
return sys.odcivarchar2list pipelined is
  lines dbms_output.chararr;
  numlines integer;
begin
  numlines := 999;
  dbms_output.get_lines(lines, numlines);
  if numlines > 0 then
    for i in 1..numlines loop
      pipe row (lines(i));
    end loop;
  end if;
end;
//
