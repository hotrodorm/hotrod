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
