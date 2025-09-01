create or replace function addlen(txt varchar2) return varchar deterministic is
begin
  return txt || length(txt);
end;
//

create or replace function addlen2(txt varchar2) return varchar deterministic is
begin
  return txt || '2';
end;
//

