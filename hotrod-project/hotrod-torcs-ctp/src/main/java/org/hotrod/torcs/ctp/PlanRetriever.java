package org.hotrod.torcs.ctp;

import org.hotrod.torcs.Statement;

public interface PlanRetriever {

  String getEstimatedCTPExecutionPlan(Statement st);

  String getActualCTPExecutionPlan(Statement st);

}
