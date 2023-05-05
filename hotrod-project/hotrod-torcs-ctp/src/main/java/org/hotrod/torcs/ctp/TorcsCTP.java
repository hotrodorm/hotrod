package org.hotrod.torcs.ctp;

import org.hotrod.torcs.Statement;
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

  public String getEstimatedCTPExecutionPlan(final Statement st) {
    return this.planRetriever.getEstimatedCTPExecutionPlan(st);
  }

  public String getActualCTPExecutionPlan(final Statement st) {
    return this.planRetriever.getActualCTPExecutionPlan(st);
  }

}
