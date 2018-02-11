declare
  ca sys_refcursor;
  ca_row account%rowtype;
  gross number(10);
  total number(10);
begin
  total := 17;
  USER1.SP1(ca, 100, gross, total);
  dbms_output.put_line('Gross=' || gross || ' Total=' || total);
  loop
    fetch ca into ca_row;
    exit when ca%notfound;
    dbms_output.put_line('* Account ID=' || ca_row.id);
  end loop;
  close ca;
end;
//


