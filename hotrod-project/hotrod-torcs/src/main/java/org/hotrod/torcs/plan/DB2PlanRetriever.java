package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.setters.Setter;

public class DB2PlanRetriever implements PlanRetriever {

  private static final String TREE_PLAN_HEAD = //
      "with\n" + //
          "t as ( -- statement (use WHERE to filter the desired one)\n" + //
          "  select * from explain_statement where querytag='";

  private static final String TREE_PLAN_TAIL = //
      "'\n" + //
          "),\n" + //
          "te as (\n" + //
          "  select case\n" + //
          "    when count(*) < 1 then raise_error('70001', 'No execution plan found.')\n" + //
          "    when count(*) > 1 then raise_error('70002', 'Too many execution plans found.')\n" + //
          "    else 1 end as cp from t where explain_level = 'P'\n" + //
          "  ),\n" + //
          "o as ( -- operators\n" + //
          "  select o.* from t join explain_operator o on\n" + //
          "    (o.explain_requester, o.explain_time, o.source_name, o.source_schema, o.source_version, o.explain_level, o.stmtno, o.sectno) =\n"
          + //
          "    (t.explain_requester, t.explain_time, t.source_name, t.source_schema, t.source_version, t.explain_level, t.stmtno, t.sectno)\n"
          + //
          "),\n" + //
          "s as ( -- streams (between operators)\n" + //
          "  select s.* from t join explain_stream s on\n" + //
          "    (s.explain_requester, s.explain_time, s.source_name, s.source_schema, s.source_version, s.explain_level, s.stmtno, s.sectno) =\n"
          + //
          "    (t.explain_requester, t.explain_time, t.source_name, t.source_schema, t.source_version, t.explain_level, t.stmtno, t.sectno)\n"
          + //
          "    where s.source_id <> -1\n" + //
          "),\n" + //
          "d as ( -- datasets (streams from sources of data rows)\n" + //
          "  select d.* from t join explain_stream d on\n" + //
          "    (d.explain_requester, d.explain_time, d.source_name, d.source_schema, d.source_version, d.explain_level, d.stmtno, d.sectno) =\n"
          + //
          "    (t.explain_requester, t.explain_time, t.source_name, t.source_schema, t.source_version, t.explain_level, t.stmtno, t.sectno)\n"
          + //
          "    where d.source_id = -1\n" + //
          "),\n" + //
          "p as ( -- predicates\n" + //
          "  select p.* from t join explain_predicate p on\n" + //
          "    (p.explain_requester, p.explain_time, p.source_name, p.source_schema, p.source_version, p.explain_level, p.stmtno, p.sectno) =\n"
          + //
          "    (t.explain_requester, t.explain_time, t.source_name, t.source_schema, t.source_version, t.explain_level, t.stmtno, t.sectno)\n"
          + //
          "    where how_applied not in ('JOIN')\n" + //
          "),\n" + //
          "a as ( -- arguments (of operators)\n" + //
          "  select a.* from t join explain_argument a on\n" + //
          "    (a.explain_requester, a.explain_time, a.source_name, a.source_schema, a.source_version, a.explain_level, a.stmtno, a.sectno) =\n"
          + //
          "    (t.explain_requester, t.explain_time, t.source_name, t.source_schema, t.source_version, t.explain_level, t.stmtno, t.sectno)\n"
          + //
          "    where argument_type = 'OUTERJN'\n" + //
          "),\n" + //
          "op as ( -- operator arguments\n" + //
          "  select o.operator_id, listagg(a.argument_value, ' ') as args\n" + //
          "    from o\n" + //
          "    left join a on a.operator_id = o.operator_id\n" + //
          "    group by o.operator_id\n" + //
          "),\n" + //
          "e as ( -- operator costs from streams\n" + //
          "  select s.target_id, max(o.total_cost) as max_cost, sum(o.total_cost) as sum_cost, count(*) as number_of_streams\n"
          + //
          "    from s\n" + //
          "    join o on o.operator_id = s.source_id\n" + //
          "    group by s.target_id\n" + //
          "),\n" + //
          "n (source_id, target_id, cost, rows, io1, operator,\n" + //
          "      source_object, table_name, column_names,\n" + //
          "      footnote, cost_alert, fetch_alert\n" + //
          "  ) as (\n" + //
          "  select s.source_id, s.target_id, o.total_cost, s.stream_count, o.io_cost, o.operator_type,\n" + //
          "      d.object_name, i.tbname, i.colnames,\n" + //
          "      case when (select count(*) from p where p.operator_id = o.operator_id) > 0 then 1 else 0 end,\n" + //
          "      case when e.number_of_streams > 1 and o.total_cost > 1.2 * e.sum_cost then 1 else 0 end,\n" + //
          "      case when o.operator_type in ('FETCH', 'TBSCAN') then 1 else 0 end\n" + //
          "    from s\n" + //
          "    left join o on o.operator_id = s.source_id\n" + //
          "    left join d on d.target_id = o.operator_id\n" + //
          "    left join e on e.target_id = o.operator_id\n" + //
          "    left join sysibm.sysindexes i on i.name = d.object_name\n" + //
          "  ),\n" + //
          "face (label, level, orden, source_id, target_id) as (\n" + //
          "  select o.operator_type, 1, repeat(' ', 100), operator_id, null from o where operator_id = 1\n" + //
          "  union all\n" + //
          "  select\n" + //
          "      replace(repeat('.  ', level), '.  .  .  ', '.  .  |  ')\n" + //
          "      || cast(n.cost as bigint) || ' '\n" + //
          "      || case when n.cost_alert = 1 then '[$] ' else '' end\n" + //
          "      || case when n.fetch_alert = 1 then '<< ' else '' end\n" + //
          "      || trim(n.operator) || ' ' \n" + //
          "      || case when n.footnote = 1 then '*' || n.source_id || ' ' else '' end\n" + //
          "      || '(' \n" + //
          "      || case\n" + //
          "        when n.rows is null then '--'\n" + //
          "        when n.rows > 10000000 then '' || cast(n.rows / 1000000 as bigint) || 'M'\n" + //
          "        when n.rows > 9999 then '' || cast(n.rows / 1000 as bigint) || 'k'\n" + //
          "        else '' || cast(ceiling(n.rows) as bigint)\n" + //
          "      end || ' rows, '\n" + //
          "      || cast(n.io1 as bigint) || ' io) '\n" + //
          "      || case\n" + //
          "        when n.source_object is null then ''\n" + //
          "        else\n" + //
          "          n.source_object ||\n" + //
          "          case\n" + //
          "            when n.table_name is null then ''\n" + //
          "            else ' on ' || n.table_name || ' (' || n.column_names || ')'\n" + //
          "            end\n" + //
          "        end\n" + //
          "      ,\n" + //
          "      face.level + 1,\n" + //
          "      trim(face.orden) || '/' || lpad(cast(n.source_id as varchar(3)), 3, '0'),\n" + //
          "      n.source_id,\n" + //
          "      n.target_id\n" + //
          "    from n, face where n.target_id = face.source_id\n" + //
          "    and face.level <= 200\n" + //
          ")\n" + //
          "select label as plan from (\n" + //
          "  select label, orden from face\n" + //
          "  union all select ' ', 'a' from sysibm.sysdummy1\n" + //
          "  union all select 'Predicates:', 'b' from sysibm.sysdummy1\n" + //
          "  union all select '*' || p.operator_id || ' ' || trim(p.how_applied) || ' ' || p.predicate_text,\n" + //
          "    'c' || lpad(cast(p.operator_id as varchar(3)), 3, '0') || p.how_applied from p  \n" + //
          "  union all select ' ', 'd1' from sysibm.sysdummy1\n" + //
          "  union all select 'Legend:', 'd2' from sysibm.sysdummy1\n" + //
          "  union all select '<< Reads from the heap.', 'e1' from sysibm.sysdummy1\n" + //
          "  union all select '$ Operation is at least 20% more expensive than its combined children.', 'e2' from sysibm.sysdummy1\n"
          + //
          ") x order by orden";

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution) throws SQLException {
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);

      String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
      try (PreparedStatement psd = conn
          .prepareStatement("delete from explain_statement where querytag='" + uuid + "'");) {
        psd.execute(); // clean up previous plans, if any

        String planSave = "explain plan set querytag='" + uuid + "' for\n" + execution.getSQL();
//        System.out.println("--- planSave ---\n" + planSave + "\n------------------------");
        try (PreparedStatement ps = conn.prepareStatement(planSave);) {
          for (Setter s : execution.getSetters()) {
            s.applyTo(ps);
//            System.out.println("--- Setting Param: " + p);
//            ps.setInt(1, 15);
//            ps.setObject(1, 15, java.sql.Types.NUMERIC);
//            ps.setObject(p.getIndex(), p.getValue(), p.getType());
          }
          ps.execute(); // save the plan

          String extractPlan = TREE_PLAN_HEAD + uuid + TREE_PLAN_TAIL;
//          System.out.println("--- extractPlan ---\n" + extractPlan + "\n------------------------");
          try (PreparedStatement pse = conn.prepareStatement(extractPlan);) {
            try (ResultSet rs = pse.executeQuery();) {
              StringBuilder sb = new StringBuilder();
              boolean first = true;
              while (rs.next()) {
                sb.append((first ? "" : "\n") + rs.getString(1));
                first = false;
              }
              return sb.toString();
            }
          }
        } finally {
          conn.rollback();
        }
      }
    }
  }

}
