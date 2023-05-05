package org.hotrod.torcs.ctp.plans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hotrod.torcs.Statement;
import org.hotrod.torcs.ctp.PlanRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQLPlanRetriever implements PlanRetriever {

  @Autowired
  private PostgreSQLPlanMapper mapper;

  @Override
  public String getEstimatedCTPExecutionPlan(Statement st) {
    Map<String, Object> params = new HashMap<>();
    params.put("sql", st.getActualSQL());
    List<String> r = this.mapper.getEstimatedPlan(params);
    return r.stream().collect(Collectors.joining(" "));
  }

  @Override
  public String getActualCTPExecutionPlan(Statement st) {
    Map<String, Object> params = new HashMap<>();
    params.put("sql", st.getActualSQL());
    List<String> r = this.mapper.getActualPlan(params);
    return r.stream().collect(Collectors.joining(" "));
  }

  @Mapper
  public static interface PostgreSQLPlanMapper {

    @Select("explain (format json) ${sql}")
    public List<String> getEstimatedPlan(Map<String, Object> parameters);

    @Select("explain (format json, analyze) ${sql}")
    public List<String> getActualPlan(Map<String, Object> parameters);

  }

}
