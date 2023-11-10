package org.hotrod.torcs.ctp.h2;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.ctp.PlanRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericH2PlanRetriever implements PlanRetriever {

  @Autowired
  private GenericH2PlanMapper mapper;

  public GenericH2PlanRetriever(final GenericH2PlanMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public String getEstimatedCTPExecutionPlan(final QueryExecution execution) {
    Map<String, Object> params = new HashMap<>();
    params.put("sql", execution.getSQL());
    String plan = this.mapper.getEstimatedPlan(params);
    return plan;
  }

  @Override
  public String getActualCTPExecutionPlan(final QueryExecution execution) {
    Map<String, Object> params = new HashMap<>();
    params.put("sql", execution.getSQL());
    String plan = this.mapper.getActualPlan(params);
    return plan;
  }

  @Mapper
  public interface GenericH2PlanMapper {

    @Select("explain ${sql}")
    public String getEstimatedPlan(Map<String, Object> parameters);

    @Select("explain analyze ${sql}")
    public String getActualPlan(Map<String, Object> parameters);

  }

}
