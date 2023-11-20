package org.hotrod.torcs.plan;

import java.sql.SQLException;

import org.hotrod.torcs.QueryExecution;

public interface PlanRetriever {

  default String getEstimatedExecutionPlan(QueryExecution execution) throws SQLException {
    return getEstimatedExecutionPlan(execution, 0);
  }

  String getEstimatedExecutionPlan(QueryExecution execution, int variation) throws SQLException;

}
