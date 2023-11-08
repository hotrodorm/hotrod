package org.hotrod.torcs.ctp;

import org.hotrod.torcs.QuerySample;
import org.hotrod.torcs.ctp.PlanRetrieverFactory.TorcsDatabaseNotSupportedException;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class TorcsCTP implements ApplicationContextAware {

  @Autowired
  private PlanRetrieverFactory factory;

  private PlanRetriever planRetriever;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    try {
      this.planRetriever = this.factory.getPlanRetriever();
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
