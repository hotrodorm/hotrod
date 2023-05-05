package org.hotrod.torcs.ctp.plans;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hotrod.torcs.Statement;
import org.hotrod.torcs.ctp.PlanRetriever;
import org.springframework.stereotype.Component;

@Component
public class OraclePlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedCTPExecutionPlan(Statement st) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getActualCTPExecutionPlan(Statement st) {
    // TODO Auto-generated method stub
    return null;
  }

  @Mapper
  public static interface OracleSQLPlanMapper {

    @Select("explain (format json) ${sql}")
    public List<String> getEstimatedPlan(Map<String, Object> parameters);

    @Select("explain (format json, analyze) ${sql}")
    public List<String> getActualPlan(Map<String, Object> parameters);

  }

}
