create function reverse (
  instring varchar(100)
)
  returns varchar(100)
  contains sql deterministic no external action
  language sql
begin
declare outputstring varchar(100);
declare stringlength int;
declare loopcounter int;
declare charatpos varchar(1);
if (instring is null) then
  return null;
else          
  set outputstring = '';
  set stringlength = length(instring); 
  set loopcounter = stringlength;
  while (loopcounter >= 1) do
     set outputstring = outputstring || substr (instring, loopcounter, 1);
     set loopcounter = loopcounter - 1;
  end while;
  return outputstring;
end if;
end  
//

-- Read only table

create variable read_only_employee_state smallint default 1
//

create or replace trigger employee_state_read_only
before insert or delete or update on employee_state for each row
begin
	if read_only_employee_state <> 0
    then signal sqlstate '80001' set message_text = 
      'Insert, update, or delete operation not allowed on read-only table "employee_state".';
  end if;
end//



  









