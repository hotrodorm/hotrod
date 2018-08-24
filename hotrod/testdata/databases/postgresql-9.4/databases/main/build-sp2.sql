create table trigger_control (
  name varchar(100) not null unique,
  enabled boolean not null
);
//

insert into trigger_control (name, enabled) values ('employee_state_read_only', true);
//

create function employee_state_read_only() returns trigger as $sp_delimiter$
declare tenabled boolean;
begin
	select t.enabled into tenabled from trigger_control t where t.name = 'employee_state_read_only';
  if tenabled is null or tenabled then
    raise exception 'Insert, update, or delete operation not allowed on read-only table "employee_state".';
    else
    return null;
  end if;
end;
$sp_delimiter$ language plpgsql;
//

create trigger employee_state_read_only before insert or update or delete on employee_state
  for each statement execute procedure employee_state_read_only();
//
