package org.hotrod.torcs.ctp.db2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.ctp.CTPPlanRetriever;
import org.hotrod.torcs.setters.index.IndexSetter;
import org.hotrod.torcs.setters.name.NameSetter;
import org.springframework.stereotype.Component;

@Component
public class DB2CTPPlanRetriever implements CTPPlanRetriever {

  private static final String DB2_CTP1_PLAN_HEAD = //
      "with params (querytag) as (values '";

  private static final String DB2_CTP1_PLAN_TAIL = //
      "'),\n" + //
          "t as (select s.* from explain_statement s cross join params p where s.explain_level = 'P' and s.querytag = p.querytag),\n"
          + //
          "te as (\n" + //
          "  select case\n" + //
          "    when count(*) < 1 then raise_error('70001', 'No execution plan found.')\n" + //
          "    when count(*) > 1 then raise_error('70002', 'Too many execution plans found.')\n" + //
          "    else 1 end as cp from t\n" + //
          "),\n" + //
          "o as ( -- operators\n" + //
          "  select o.* from t cross join te join explain_operator o on\n" + //
          "      (o.explain_requester, o.explain_time, o.source_name, o.source_schema, o.source_version, o.explain_level, o.stmtno, o.sectno) =\n"
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
          "  where s.source_id <> -1\n" + //
          "),\n" + //
          "d as ( -- datasets (streams from sources of data rows)\n" + //
          "  select d.* from t join explain_stream d on\n" + //
          "    (d.explain_requester, d.explain_time, d.source_name, d.source_schema, d.source_version, d.explain_level, d.stmtno, d.sectno) =\n"
          + //
          "    (t.explain_requester, t.explain_time, t.source_name, t.source_schema, t.source_version, t.explain_level, t.stmtno, t.sectno)\n"
          + //
          "  where d.source_id = -1\n" + //
          "),\n" + //
          "i as (\n" + //
          "  select i.creator as index_schema, i.name as index_name, i.tbcreator as table_schema, i.tbname as table_name, i.uniquerule,\n"
          + //
          "    cast('['|| listagg('{\"_name\": \"'|| hex(cast(coalesce(c.colname, text) as varchar(2000))) ||'\", \"asc\": '|| case when c.colorder = 'A' then 'true' else 'false' end ||'}', ', ') within group (order by c.colseq) ||']' as varchar(2000)) as members\n"
          + //
          "  from d\n" + //
          "  join sysibm.sysindexes i on i.creator = d.object_schema and i.name = d.object_name\n" + //
          "  join sysibm.sysindexcoluse c on c.indschema = d.object_schema and c.indname = d.object_name\n" + //
          "  group by i.creator, i.name, i.tbcreator, i.tbname, i.uniquerule\n" + //
          "),\n" + //
          "p as (\n" + //
          "  select p.operator_id,\n" + //
          "    '[' || listagg('{'||\n" + //
          "    '\"predicate_id\": ' || coalesce(''||p.predicate_id, 'null') || ', ' ||\n" + //
          "    '\"how_applied\": \"' || trim(p.how_applied) || '\", ' ||\n" + //
          "    '\"relop_type\": \"' || trim(p.relop_type) || '\", ' ||\n" + //
          "    '\"subquery\": \"' || trim(p.subquery) || '\", ' ||\n" + //
          "    '\"filter_factor\": ' || coalesce(''||p.filter_factor, 'null') || ', ' ||\n" + //
          "    '\"_predicate_text\": \"' || hex(cast(p.predicate_text as varchar(1000))) || '\", ' ||\n" + //
          "    '\"range_num\": ' || coalesce(''||p.range_num, 'null') || ', ' ||\n" + //
          "    '\"index_colseq\": ' || coalesce(''||p.index_colseq, 'null')\n" + //
          "    || '}', ', ') within group(order by p.how_applied, p.relop_type, p.predicate_id) || ']'\n" + //
          "    as predicates\n" + //
          "  from t\n" + //
          "  join explain_predicate p on (p.explain_requester, p.explain_time, p.source_name, p.source_schema, p.source_version, p.explain_level, p.stmtno, p.sectno) =\n"
          + //
          "                              (t.explain_requester, t.explain_time, t.source_name, t.source_schema, t.source_version, t.explain_level, t.stmtno, t.sectno)\n"
          + //
          "  group by p.operator_id\n" + //
          "  ),\n" + //
          "a as (\n" + //
          "  select operator_id, cast('{' || listagg('\"'|| argument_type ||'\": ' || vals, ', ') || '}' as varchar(2000)) as args\n"
          + //
          "  from (\n" + //
          "    select a.operator_id, '_' || trim(a.argument_type) as argument_type, case when count(*) > 1 then '[' else '' end || listagg('\"'||hex(trim(a.argument_value)) || '\"', ', ') || case when count(*) > 1 then ']' else '' end as vals\n"
          + //
          "    from t\n" + //
          "    join explain_argument a on\n" + //
          "      (a.explain_requester, a.explain_time, a.source_name, a.source_schema, a.source_version, a.explain_level, a.stmtno, a.sectno) =\n"
          + //
          "      (t.explain_requester, t.explain_time, t.source_name, t.source_schema, t.source_version, t.explain_level, t.stmtno, t.sectno)\n"
          + //
          "    group by a.operator_id, a.argument_type\n" + //
          "  ) x\n" + //
          "  group by operator_id\n" + //
          "),\n" + //
          "aa as (\n" + //
          "  select a.operator_id, cast('{' || listagg('\"_'|| a.argument_type ||'\": \"' || hex(trim(a.argument_value)) || '\"', ', ') || '}' as varchar(500)) as args\n"
          + //
          "  from t\n" + //
          "  join explain_argument a on\n" + //
          "    (a.explain_requester, a.explain_time, a.source_name, a.source_schema, a.source_version, a.explain_level, a.stmtno, a.sectno) =\n"
          + //
          "    (t.explain_requester, t.explain_time, t.source_name, t.source_schema, t.source_version, t.explain_level, t.stmtno, t.sectno)\n"
          + //
          "  group by a.operator_id\n" + //
          "),\n" + //
          "jop as (\n" + //
          "select\n" + //
          "  o.operator_id,\n" + //
          "  '{' ||\n" + //
          "  '\"operator_id\": ' || coalesce(''||o.operator_id, 'null') || ', ' ||\n" + //
          "  '\"operator_type\": ' || coalesce('\"'||trim(o.operator_type)||'\"', 'null') || ', ' ||\n" + //
          "  '\"total_cost\": ' || coalesce(''||o.total_cost, 'null') || ', ' ||\n" + //
          "  '\"io_cost\": ' || coalesce(''||o.io_cost, 'null') || ', ' ||\n" + //
          "  '\"cpu_cost\": ' || coalesce(''||o.cpu_cost, 'null') || ', ' ||\n" + //
          "  '\"first_row_cost\": ' || coalesce(''||o.first_row_cost, 'null') || ', ' ||\n" + //
          "  '\"re_total_cost\": ' || coalesce(''||o.re_total_cost, 'null') || ', ' ||\n" + //
          "  '\"re_io_cost\": ' || coalesce(''||o.re_io_cost, 'null') || ', ' ||\n" + //
          "  '\"re_cpu_cost\": ' || coalesce(''||o.re_cpu_cost, 'null') || ', ' ||\n" + //
          "  '\"comm_cost\": ' || coalesce(''||o.comm_cost, 'null') || ', ' ||\n" + //
          "  '\"buffers\": ' || coalesce(''||o.buffers, 'null') || ', ' ||\n" + //
          "  '\"remote_total_cost\": ' || coalesce(''||o.remote_total_cost, 'null') || ', ' ||\n" + //
          "  '\"remote_comm_cost\": ' || coalesce(''||o.remote_comm_cost, 'null') ||\n" + //
          "  case when s.stream_id is not null then\n" + //
          "  ', \"stream_id\": ' || coalesce(''||s.stream_id, 'null') || ', ' ||\n" + //
          "  '\"parent_id\": ' || coalesce(''||s.target_id, 'null') || ', ' ||\n" + //
          "  '\"stream_count\": ' || coalesce(''||s.stream_count, 'null') || ', ' ||\n" + //
          "  '\"column_count\": ' || coalesce(''||s.column_count, 'null') || ', ' ||\n" + //
          "  '\"predicate_id\": ' || coalesce(''||s.predicate_id, 'null') || ', ' ||\n" + //
          "  '\"_column_names\": ' || case when s.column_names is null then 'null' else '\"'||hex(cast(s.column_names as varchar(1000)))||'\"' end\n"
          + //
          "  else\n" + //
          "  ', \"parent_id\": null'\n" + //
          "  end ||\n" + //
          "  case when d.stream_id is not null then\n" + //
          "  ', \"dstream_id\": ' || coalesce(''||d.stream_id, 'null') || ', ' ||\n" + //
          "  '\"_dobject_schema\": ' || case when d.object_schema is null then 'null' else '\"'||hex(trim(d.object_schema))||'\"' end || ', ' ||\n"
          + //
          "  '\"_dobject_name\": ' || case when d.object_name is null then 'null' else '\"'||hex(trim(d.object_name))||'\"' end || ', ' ||\n"
          + //
          "  '\"dstream_count\": ' || coalesce(''||d.stream_count, 'null') || ', ' ||\n" + //
          "  '\"dcolumn_count\": ' || coalesce(''||d.column_count, 'null') || ', ' ||\n" + //
          "  '\"dpredicate_id\": ' || coalesce(''||d.predicate_id, 'null') || ', ' ||\n" + //
          "  '\"_dcolumn_names\": ' || case when d.column_names is null then 'null' else '\"'||hex(cast(d.column_names as varchar(1000)))||'\"' end ||\n"
          + //
          "  case when i.index_name is not null then\n" + //
          "  ', \"index\": { \"_index_schema\": '  || case when i.index_schema is null then 'null' else '\"'||hex(trim(i.index_schema))||'\"' end || ', ' ||\n"
          + //
          "  '\"_index_name\": '  || case when i.index_name is null then 'null' else '\"'||hex(trim(i.index_name))||'\"' end || ', ' ||\n"
          + //
          "  '\"_table_schema\": '  || case when i.table_schema is null then 'null' else '\"'||hex(trim(i.table_schema))||'\"' end || ', ' ||\n"
          + //
          "  '\"_table_name\": '  || case when i.table_name is null then 'null' else '\"'||hex(trim(i.table_name))||'\"' end || ', ' ||\n"
          + //
          "  '\"uniquerule\": '  || case when i.uniquerule is null then 'null' else '\"'||i.uniquerule||'\"' end || ', ' ||\n"
          + //
          "  '\"members\": '  || i.members ||\n" + //
          "  '}'\n" + //
          "  else '' end  \n" + //
          "  else '' end ||\n" + //
          "  coalesce(', \"args\": '|| a.args, '') ||\n" + //
          "  coalesce(', \"predicates\": '|| p.predicates, '') ||\n" + //
          "  '}'\n" + //
          "  as op\n" + //
          "from o\n" + //
          "left join s on s.source_id = o.operator_id\n" + //
          "left join d on d.target_id = o.operator_id\n" + //
          "left join i on i.index_schema = d.object_schema and i.index_name = d.object_name\n" + //
          "left join a on a.operator_id = o.operator_id\n" + //
          "left join p on p.operator_id = o.operator_id\n" + //
          "),\n" + //
          "js as (\n" + //
          "select operator_id, case when operator_id = (select min(operator_id) from jop) then '' else ', ' end || op as op from jop \n"
          + //
          "union all select -1, '{ \"format\": \"ctp-db2-luw-1\", \"requester\": \"'||trim(explain_requester)||'\", \"timestamp\": \"'|| to_char(explain_time, 'YYYY-MM-DD HH24:MI:SS') ||'\", \"querytag\": '|| coalesce('\"'||trim(querytag)||'\"', 'null') ||', \"operators\": [' from t\n"
          + //
          "union all select 1000, ']}' from sysibm.sysdummy1\n" + //
          ")\n" + //
          "select op from js order by operator_id";

  @Override
  public String getEstimatedCTPExecutionPlan(final QueryExecution execution) throws SQLException {
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);

      String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
      try (PreparedStatement psd = conn
          .prepareStatement("delete from explain_statement where querytag='" + uuid + "'");) {
        psd.execute(); // clean up previous plans, if any

        String planSave = "explain plan set querytag='" + uuid + "' for\n" + execution.getSQL();
//        System.out.println("--- planSave ---\n" + planSave + "\n------------------------");

        if (execution.getNameSetters().isEmpty()) {

          try (PreparedStatement ps = conn.prepareStatement(planSave);) {
            for (IndexSetter s : execution.getIndexSetters()) {
              s.applyTo(ps);
            }
            ps.execute(); // save the plan

            String extractPlan = DB2_CTP1_PLAN_HEAD + uuid + DB2_CTP1_PLAN_TAIL;
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

        } else {

          try (CallableStatement cs = conn.prepareCall(planSave);) {
            for (IndexSetter s : execution.getIndexSetters()) {
              s.applyTo(cs);
            }
            for (NameSetter s : execution.getNameSetters()) {
              s.applyTo(cs);
            }
            cs.execute(); // save the plan

            String extractPlan = DB2_CTP1_PLAN_HEAD + uuid + DB2_CTP1_PLAN_TAIL;
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

}
