package org.hotrod.torcs.ctp.plans;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hotrod.torcs.QuerySample;
import org.hotrod.torcs.ctp.PlanRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQLPlanRetriever implements PlanRetriever {

  @Autowired
  private PostgreSQLPlanMapper mapper;

  private AtomicLong n = new AtomicLong();

  @Override
  public String getEstimatedCTPExecutionPlan(final QuerySample sample) {
    PreparedSQL ps = new PreparedSQL(sample.getSQL());
    System.out.println("ps=" + ps);
    Map<String, Object> params = new HashMap<>();
    params.put("planName", "plan" + n.getAndIncrement());
    params.put("paramTypes", String.join(",", Collections.nCopies(ps.getNumberOfParams(), "unknown")));
    params.put("sql", ps.getPreparedSQL());
    params.put("paramValues", String.join(",", Collections.nCopies(ps.getNumberOfParams(), "null")));

    List<String> r = this.mapper.getEstimatedPlan(params);
    return r.stream().collect(Collectors.joining(" "));
  }

  @Override
  public String getActualCTPExecutionPlan(final QuerySample sample) {
    Map<String, Object> params = new HashMap<>();
    params.put("sql", sample.getSQL());
    List<String> r = this.mapper.getActualPlan(params);
    return r.stream().collect(Collectors.joining(" "));
  }

  public class PreparedSQL {

    private String sql;
    private int numberOfParams;

    public PreparedSQL(final String sql) {
      this.numberOfParams = 0;
      StringBuilder sb = new StringBuilder();
      int pos = 0;
      boolean inApos = false;
      boolean inQuotes = false;
      while (pos < sql.length()) {
        String s = null;
        int c = sql.charAt(pos);
        if (inApos) {
          if (c == '\'') {
            inApos = false;
          }
        } else if (inQuotes) {
          if (c == '"') {
            inQuotes = false;
          }
        } else { // plain text
          if (c == '\'') {
            inApos = true;
          } else if (c == '"') {
            inQuotes = true;
          } else if (c == '?') {
            s = "$" + (++this.numberOfParams);
          }
        }
        if (s == null) {
          sb.append((char) c);
        } else {
          sb.append(s);
        }
        pos++;
      }
      this.sql = sb.toString();
    }

    public String getPreparedSQL() {
      return sql;
    }

    public int getNumberOfParams() {
      return numberOfParams;
    }

    public String toString() {
      return "[" + this.numberOfParams + "]" + this.sql;
    }

  }

//  deallocate sql1;
//
//  prepare sql1 (unknown) as 
//  SELECT
//    i.id, 
//    i.amount, 
//    i.branch_id
//  FROM public.invoice i
//  WHERE i.branch_id = $1
//
//  explain (format json) execute sql1(null);  

  @Mapper
  public static interface PostgreSQLPlanMapper {

    // planName: plan1
    // paramTypes: unknown x n
    // sql: select ...
    // values: null x n

    @Select("prepare ${planName} (${paramTypes}) as ${sql}; explain (format json) execute ${planName} (${paramValues}); deallocate ${planName};")
    public List<String> getEstimatedPlan(Map<String, Object> parameters);

    @Select("prepare ${planName} (${paramTypes}) as ${sql}; explain (format json, analyze) execute ${planName} (${paramValues}); deallocate ${planName};")
    public List<String> getActualPlan(Map<String, Object> parameters);

  }

}
