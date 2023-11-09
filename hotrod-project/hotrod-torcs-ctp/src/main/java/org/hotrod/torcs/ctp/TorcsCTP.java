package org.hotrod.torcs.ctp;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.ctp.PlanRetrieverFactory.TorcsDatabaseNotSupportedException;
import org.hotrod.torcs.ctp.h2.GenericH2PlanMapper;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TorcsCTP {

  @Autowired
  private PlanRetrieverFactory planRetrieverFactory;

  private PlanRetriever planRetriever;

  public TorcsCTP(final PlanRetrieverFactory planRetrieverFactory, final GenericH2PlanMapper h2Mapper) {
    this.planRetrieverFactory = planRetrieverFactory;
    System.out.println("TorcsCTP() -- this.factory=" + this.planRetrieverFactory);
    try {
      this.planRetriever = this.planRetrieverFactory.getPlanRetriever(h2Mapper);
    } catch (TorcsDatabaseNotSupportedException e) {
      throw new FatalBeanException(e.getMessage(), e);
    }
  }

  public String getEstimatedCTPExecutionPlan(final QueryExecution execution) {
    return this.planRetriever.getEstimatedCTPExecutionPlan(execution);
  }

  public String getActualCTPExecutionPlan(final QueryExecution execution) {
    return this.planRetriever.getActualCTPExecutionPlan(execution);
  }

}
