package org.hotrod.torcs.ctp;

import org.hotrod.torcs.QuerySample;
import org.hotrod.torcs.ctp.PlanRetrieverFactory.TorcsDatabaseNotSupportedException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TorcsCTP {

  @Autowired
  private PlanRetrieverFactory planRetrieverFactory;

  private PlanRetriever planRetriever;

  public TorcsCTP(final PlanRetrieverFactory planRetrieverFactory) {
    this.planRetrieverFactory = planRetrieverFactory;
    System.out.println("TorcsCTP() -- this.factory=" + this.planRetrieverFactory);
    try {
      this.planRetriever = this.planRetrieverFactory.getPlanRetriever();
    } catch (TorcsDatabaseNotSupportedException e) {
      throw new FatalBeanException(e.getMessage(), e);
    }
  }

  public String getEstimatedCTPExecutionPlan(final QuerySample sample) {
    return this.planRetriever.getEstimatedCTPExecutionPlan(sample);
  }

  public String getActualCTPExecutionPlan(final QuerySample sample) {
    return this.planRetriever.getActualCTPExecutionPlan(sample);
  }

}
