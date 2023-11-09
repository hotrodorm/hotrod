package org.hotrod.torcs.ctp.db2;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hotrod.torcs.QuerySample;
import org.hotrod.torcs.ctp.PlanRetriever;
import org.springframework.stereotype.Component;

@Component
public class DB2PlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedCTPExecutionPlan(QuerySample sample) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getActualCTPExecutionPlan(QuerySample sample) {
    // TODO Auto-generated method stub
    return null;
  }

  @Mapper
  public static interface DB2SQLPlanMapper {

    @Select("explain (format json) ${sql}")
    public List<String> getEstimatedPlan(Map<String, Object> parameters);

    @Select("explain (format json, analyze) ${sql}")
    public List<String> getActualPlan(Map<String, Object> parameters);

  }

}
