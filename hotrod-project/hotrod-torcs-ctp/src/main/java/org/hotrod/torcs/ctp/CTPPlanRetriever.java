package org.hotrod.torcs.ctp;

import java.sql.SQLException;

import org.hotrod.torcs.QueryExecution;

public interface CTPPlanRetriever {

  String getEstimatedCTPExecutionPlan(QueryExecution execution) throws SQLException;

}
