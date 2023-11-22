package org.hotrod.torcs.plan;

import java.sql.SQLException;

import org.hotrod.torcs.QueryExecution;

public class ApacheDerbyPlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution, int format) throws SQLException {
    throw new UnsupportedOperationException(
        "Apache Derby does not include functionality to provide query execution plans.");
  }

}
