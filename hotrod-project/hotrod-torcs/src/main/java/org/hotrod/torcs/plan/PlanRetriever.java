package org.hotrod.torcs.plan;

import java.sql.SQLException;

import org.hotrod.torcs.QueryExecution;

public interface PlanRetriever {

  String getEstimatedExecutionPlan(QueryExecution execution) throws SQLException;

}
