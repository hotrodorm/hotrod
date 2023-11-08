package org.hotrod.torcs.ctp;

import org.hotrod.torcs.QuerySample;

public interface PlanRetriever {

  String getEstimatedCTPExecutionPlan(QuerySample sample);

  String getActualCTPExecutionPlan(QuerySample sample);

}
