package org.hotrod.torcs.ctp.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.ctp.CTPPlanRetriever;
import org.hotrod.torcs.setters.Setter;
import org.springframework.stereotype.Component;

@Component
public class OracleCTPPlanRetriever implements CTPPlanRetriever {

  private static final String EXTRACT_CTP_PLAN = //
      "with\n" + //
          "p as (select * from sys.plan_table$ where plan_id = (select max(plan_id) from sys.plan_table$)),\n" + //
          "i as (\n" + //
          "  select p.id,\n" + //
          "    ' {\"_owner\":\"' || rawtohex(max(c.index_owner)) || '\", ' ||\n" + //
          "    '\"_name\":\"'|| rawtohex(max(c.index_name)) || '\", ' ||\n" + //
          "    '\"_table_owner\":\"'|| rawtohex(max(ix.table_owner)) || '\", ' ||\n" + //
          "    '\"_table_name\":\"'|| rawtohex(max(ix.table_name)) || '\", ' ||\n" + //
          "    '\"members\":[' ||\n" + //
          "          listagg('{\"_name\":' || '\"' || rawtohex(\n" + //
          "          case when e.column_expression is null then c.column_name\n" + //
          "            else extractvalue(dbms_xmlgen.getxmltype(\n" + //
          "              'select e.column_expression from all_ind_expressions e where e.index_owner = '''||\n" + //
          "              c.index_owner||''' and e.index_name = '''||c.index_name||''' and e.column_position = '||c.column_position), '//text()')\n"
          + //
          "          end) ||'\"'\n" + //
          "          || ', \"asc\":' || decode(c.descend, 'ASC', 'true', 'false') || '}', ', ') within group (order by c.column_position)\n"
          + //
          "    || ']}' as index_def\n" + //
          "  from p\n" + //
          "  left join all_indexes ix on ix.owner = p.object_owner and ix.index_name = p.object_name\n" + //
          "  left join all_ind_columns c on c.index_owner = p.object_owner and c.index_name = p.object_name\n" + //
          "  left join all_ind_expressions e on e.index_owner = c.index_owner and e.index_name = c.index_name and e.column_position = c.column_position\n"
          + //
          "  where c.index_name is not null\n" + //
          "  group by p.id\n" + //
          "),\n" + //
          "q as (\n" + //
          "select p.id,\n" + //
          "  '{' ||\n" + //
          "  '\"id\":' || p.id || ', ' ||\n" + //
          "  '\"_operation\":' || decode(p.operation, null, 'null', '\"' || rawtohex(p.operation) ||'\"') || ', ' ||\n"
          + //
          "  '\"parent_id\":' || coalesce('' || p.parent_id, 'null') || ', ' ||\n" + //
          "  '\"cost\":' || coalesce('' || p.cost, 'null') || ', ' ||\n" + //
          "  '\"cardinality\":' || coalesce('' || p.cardinality, 'null') || ', ' ||\n" + //
          "  '\"io_cost\":' || coalesce('' || p.io_cost, 'null') || ', ' ||\n" + //
          "  '\"cpu_cost\":' || coalesce('' || p.cpu_cost, 'null') || ', ' ||\n" + //
          "  '\"time\":' || coalesce('' || p.time, 'null') || ', ' ||\n" + //
          "  '\"bytes\":' || coalesce('' || p.bytes, 'null') || ', ' ||\n" + //
          "  '\"_options\":' || decode(p.options, null, 'null', '\"' || rawtohex(p.options) ||'\"') || ', ' ||\n" + //
          "  '\"_access_predicates\":' || decode(p.access_predicates, null, 'null', '\"' || rawtohex(p.access_predicates) ||'\"') || ', ' ||\n"
          + //
          "  '\"_filter_predicates\":' || decode(p.filter_predicates, null, 'null', '\"' || rawtohex(p.filter_predicates) ||'\"') || ', ' ||\n"
          + //
          "  '\"_object_owner\":' || decode(p.object_owner, null, 'null', '\"' || rawtohex(p.object_owner) ||'\"') || ', ' ||\n"
          + //
          "  '\"_object_name\":' || decode(p.object_name, null, 'null', '\"' || rawtohex(p.object_name) ||'\"') || ', ' ||\n"
          + //
          "  '\"_object_alias\":' || decode(p.object_alias, null, 'null', '\"' || rawtohex(p.object_alias) ||'\"') || ', ' ||\n"
          + //
          "  '\"index_def\":' || coalesce(i.index_def, 'null') ||\n" + //
          "  '}' as node\n" + //
          "from p\n" + //
          "left join all_indexes ix on ix.index_name = p.object_name\n" + //
          "left join i on i.id = p.id\n" + //
          "),\n" + //
          "meta as (select max(statement_id) as statement_id, max(plan_id) as plan_id, max(timestamp) as timestamp from p)\n"
          + //
          "select line\n" + //
          "from (\n" + //
          "  select -1 as ord, '{\"format\":\"ctp-oracle-1\", ' ||\n" + //
          "    '\"_statement_id\":' || decode(statement_id, null, 'null', '\"' || rawtohex(statement_id) ||'\"') || ', ' ||\n"
          + //
          "    '\"plan_id\":' || coalesce('' || plan_id, 'null') || ', ' ||\n" + //
          "    '\"timestamp\":' || decode(timestamp, null, 'null', '\"' || to_char(timestamp, 'YYYY-MM-DD\"T\"HH24:MI:SS') ||'\"') || ', ' ||\n"
          + //
          "    '\"operators\":[' as line \n" + //
          "  from meta\n" + //
          "  union all select id, case when id = 0 then '' else ', ' end || node  from q\n" + //
          "  union all select 9999, ']}' from dual\n" + //
          ") x\n" + //
          "order by ord";

  @Override
  public String getEstimatedCTPExecutionPlan(QueryExecution execution) throws SQLException {
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement("explain plan for\n" + execution.getSQL());) {
        for (Setter s : execution.getSetters()) {
          s.applyTo(ps);
        }
        ps.execute();
        try (PreparedStatement psr = conn.prepareStatement(EXTRACT_CTP_PLAN); ResultSet rs = psr.executeQuery();) {
          StringBuilder sb = new StringBuilder();
          boolean first = true;
          while (rs.next()) {
            sb.append((first ? "" : "\n") + rs.getString(1));
            first = false;
          }
          return sb.toString();
        }
      } finally {
        conn.rollback();
      }
    }

  }

  @Override
  public String getActualCTPExecutionPlan(QueryExecution execution) {
    throw new UnsupportedOperationException("Torcs CTP cannot retrieve actual execution plans in the Oracle database. "
        + "To produce it an Oracle DBA will need to enable statistics gathering (statistics_level = 'ALL'). "
        + "Then the queries can be run normally; "
        + "As a final step, the actual plan can be retrieved with an extra privileged procedure. "
        + "This is out of reach for Torcs CTP.");
  }

}
