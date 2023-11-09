package org.hotrod.torcs.ctp;

import org.hotrod.torcs.QueryExecution;

public interface PlanRetriever {

  String getEstimatedCTPExecutionPlan(QueryExecution execution);

  String getActualCTPExecutionPlan(QueryExecution execution);

}
