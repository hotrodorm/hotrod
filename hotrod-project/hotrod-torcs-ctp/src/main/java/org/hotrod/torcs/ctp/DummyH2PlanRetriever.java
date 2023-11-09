package org.hotrod.torcs.ctp;

import org.hotrod.torcs.QuerySample;
import org.springframework.stereotype.Component;

@Component
public class DummyH2PlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedCTPExecutionPlan(final QuerySample sample) {
    return "Dummy estimated plan 1";
  }

  @Override
  public String getActualCTPExecutionPlan(final QuerySample sample) {
    return "Dummy actual plan 1";
  }

}
