-- Prepare a temp table in the session

create temp table plan(t jsonb);

-- Save the plan to the temp table

$delim$

do $block$ 
declare l_sql text := $sql$

select *
from account a
join transaction t on t.account_id = a.id
join federal_branch b on b.id = t.fed_branch_id
where t.amount between 100 and 199
  and b.name like 'V%'

$sql$;
  l_result jsonb;
begin 
  execute 'explain (format json) '|| l_sql into l_result;
  delete from plan;
  insert into plan (t) values (l_result);
end $block$;
$delim$

-- Preparing a basic tree


with recursive
j (xid, parent_xid, child_number, o) as (
  select '1', null, 1, o from (select t -> 0 -> 'Plan' as o from plan) x
 union all
  select j.xid || '.' || s.ord, j.xid, s.ord::int, s.o
  from j cross join jsonb_array_elements(o -> 'Plans') with ordinality as s (o, ord)
)
-- select * from j;
, n (xid, parent_xid, child_number, o) as (
  select xid, parent_xid, child_number, o from (
    select '0', null, 1, jsonb_build_object('Node Type', 'Result', 'Total Cost', (o ->> 'Total Cost')::real, 'Plan Width', o ->> 'Plan Width'), o ->> 'Node Type' from j where xid = '1'
  ) x (xid, parent_xid, child_number, o, node_type) where node_type <> 'Result'
 union all
  select xid, case when xid = '1' and o ->> 'Node Type' <> 'Result' then '0' else parent_xid end, child_number, o from j
)
, renum as (select xid, row_number() over(order by xid) as id from n)
, s (id, parent_id, child_number, cost, operator, join_type, inner_unique, join_predicate, filter_predicate, table_name, alias, index_name, index_direction, access_predicate, rows, parallel, parent_rel, width, hash_cond, index_based_on_table, o) as (
  select a.id, b.id as parent_id, n.child_number,
    (o -> 'Total Cost')::real, o ->> 'Node Type', o ->> 'Join Type', o ->> 'Inner Unique', o ->> 'Join Filter', coalesce(o ->> 'Filter', o ->> 'Recheck Cond'), case when o->> 'Node Type' <> 'Index Only Scan' then o ->> 'Relation Name' end,
    o ->> 'Alias', o ->> 'Index Name', o ->> 'Scan Direction', o ->> 'Index Cond', (o -> 'Plan Rows')::real, o ->> 'Parallel Aware', o ->> 'Parent Relationship', (o ->> 'Plan Width')::int,
    o ->> 'Hash Cond',
    case when (o ->> 'Index Name') is not null and o ->> 'Relation Name' is null then (select i.tablename from pg_indexes i where i.indexname = (o ->> 'Index Name')) else o ->> 'Relation Name' end,
    n.o
  from n
  left join renum a on a.xid = n.xid
  left join renum b on b.xid = n.parent_xid
),
p as (
  select s.*,
    parent.inner_unique as parent_inner_unique,
    parent.hash_cond as parent_hash_cond
  from s
  left join s parent on parent.id = s.parent_id
),
microformat (less_than, available, divisor, tformat, append) as (
  select 9999.5, 0, 1, 'FM99990', ''
  union all select 99950, 0, 1000, 'FM990.0', 'k'
  union all select 999500, 0, 1000, 'FM9990', 'k'
  union all select 9995000, 0, 1000000, 'FM90.00', 'M'
  union all select 99950000, 0, 1000000, 'FM990.0', 'M'
  union all select 999500000, 0, 1000000, 'FM9990', 'M'
  union all select 1, 1, 1000000000, 'FM999990.00', 'G'
),
cost_mf as (select * from microformat where (select max(cost) from p) < less_than order by less_than limit 1),
g (section, line, rendered) as (
  select 1, 1, -- 1. Opening
    'digraph p1 { rankdir=BT; ranksep=0.3; graph [fontname = "helvetica", fontsize=9, bgcolor="#f0f0f0"]; node [fontname = "helvetica", fontsize = 9]; edge [fontname = "helvetica", fontsize = 9]; labelloc="t";'||
    ' label="SQL Execution Plan - '||'"; '||
    'subgraph tree { bgcolor="#808080"; '
  union all select 2, row_number() over(order by p.id), -- 2. Nodes for operators
    p.id || ' [shape=none width=0 height=0 margin=0 style="rounded" color="#eb684b" label=<<table cellspacing="0" border="2" cellborder="1"><tr>'||
    '<td bgcolor="#ffffff" width="40%"><font point-size="16">'||to_char(p.cost / cost_mf.divisor, tformat)||cost_mf.append||'</font><br/>cost</td>'||
    '<td bgcolor="#fff5c7">'||p.width||'<br/>width</td>'||
    '</tr>'||
    -- operator
    '<tr><td bgcolor="#ffffff" colspan="2">'||
    case when p.operator = 'Aggregate' and filter_predicate is not null then '<font point-size="10" color="#ff8c00"><b>&#127349;</b></font>' else '' end || -- index filter - orange 'F'
    '<font point-size="12" color="red"> </font>' ||
    case when p.child_number > 1 
    then
      case when coalesce(p.parent_inner_unique, '') = 'true' then '<font color="red">Seek</font> '
      else
        case when p.operator = 'Index Scan' then '<font color="red">Range</font> ' else '' end
      end
    else ''
    end ||
    p.operator ||
    case when p.operator like '%Join' then '' else coalesce('<font color="red">'||case when p.join_type = 'Inner' then '' else ' ' || p.join_type end || ' Join</font>', '') end ||
    case when coalesce(p.access_predicate, p.filter_predicate, p.join_predicate, p.parent_hash_cond) is not null then ' *'||p.id else '' end ||
    -- index
    '<font point-size="12"> </font></td></tr>'||
    case when p.index_name is not null then '<tr><td bgcolor="#c4ffca" align="left" colspan="2"><font point-size="10" color="#00bb34"><b>&#127352;</b></font>' || -- index access - green 'I'
      case when p.operator = 'Index Only Scan' and filter_predicate is not null then '<font point-size="4" color="red"> </font><font point-size="10" color="#ff8c00"><b>&#127349;</b></font>' else '' end || -- index filter - orange 'F'
      '<font point-size="14" color="red"> </font>'||p.index_name|| ' *'||p.id||'i</td></tr>' else ''
    end ||
    -- table
    case when p.table_name is not null then
      '<tr><td bgcolor="#c7e7ff" align="left" colspan="2"><font point-size="10" color="blue"><b>&#127351;</b></font>'|| -- heap access - blue 'H'
      case when p.filter_predicate is not null then '<font point-size="4" color="##ff8c00"> </font><font point-size="10" color="red"><b>&#127349;</b></font>' else '' end || -- heap filter - orange 'F'
      '<font point-size="14" color="red"> </font>' ||
      p.table_name||
      coalesce(case when p.alias = p.table_name then '' else ' ('||p.alias||')' end, '')
      || '</td></tr>' else '' end ||
    -- last line
    '<tr><td bgcolor="#ffffff" colspan="1" align="left" cellpadding="1">'||
    '</td><td bgcolor="#d0d0d0" width="10%">#' ||p.id||'</td></tr>'||
    '</table>>];'
    from p
    cross join cost_mf
  union all select 4, row_number() over(order by p.id), -- 4. Streams
      p.id||'->'||p.parent_id||'[color="gray60" label=" '||p.rows||' row'||case when p.rows = 1 then '' else 's' end||'"];'
    from p
    where p.parent_id is not null
  union all select 6, 1, -- 6. Starting Predicates subgraph
      '} subgraph cluster_1 { rank=source; color="#606060"; bgcolor=white; label=""; p [fontname = "monospace", shape=plaintext, style=solid, label='
  union all select 7, 1, -- 7. Predicates
      '"Predicates:' ||
      string_agg('\l  *'||id||' '|| ptype || replace(pred, '"', '\"'), '' order by id, ord) ||
      '\l'
    from (
      select id, ord, ptype, case when pred like '(%)' then substr(pred, 2, length(pred) - 2) else pred end  
      from (
        select p.id, 1 as ord, 'Access: ' as ptype, join_predicate as pred from p where join_predicate is not null union all
        select p.id, 2, 'Access: ', parent_hash_cond from p where parent_hash_cond is not null union all
        select p.id, 3, 'Access: ', access_predicate from p where access_predicate is not null union all
        select p.id, 4, 'Filter: ', filter_predicate from p where filter_predicate is not null
      ) x
    ) x
  union all select 8, 1, -- 8. Indexes
      'Indexes:' ||
      string_agg(footnote, ' ' order by id)
      || '\l"'
    from (
      select p.id,
        '\l  *'||p.id||'i '||p.index_based_on_table||' ('|| (
        select string_agg(coalesce(a.attname,(('{' || pg_get_expr( i.indexprs,i.indrelid ) || '}')::text[] )[k.i] ) || case when i.indoption[k.i - 1] = 0 then '' else ' DESC' end, ', ' order by k.i)
        from pg_index i 
        cross join lateral unnest(i.indkey) with ordinality as k(attnum,i)
        left join pg_attribute as a on i.indrelid = a.attrelid and k.attnum = a.attnum
        where i.indexrelid::regclass = p.index_name::regclass
        ) ||')' as footnote
      from p
      where p.index_name is not null
    ) x    
  union all select 9, 1, -- 9. Closing
    ' ] } }'
)
select rendered
from g
order by section, line;
