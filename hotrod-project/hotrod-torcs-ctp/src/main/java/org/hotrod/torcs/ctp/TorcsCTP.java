package org.hotrod.torcs.ctp;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.ctp.CTPPlanRetrieverFactory.UnsupportedTorcsCTPDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TorcsCTP {

  @Autowired
  private CTPPlanRetrieverFactory ctpPlanRetrieverFactory;

  private Map<Integer, CTPPlanRetriever> planRetrievers = new HashMap<>();

  public String getEstimatedCTPExecutionPlan(final QueryExecution execution)
      throws SQLException, UnsupportedTorcsCTPDatabaseException {
    CTPPlanRetriever r = this.planRetrievers.get(execution.getDataSourceReference().getId());
    if (r == null) {
      r = get(execution);
    }
    return r.getEstimatedCTPExecutionPlan(execution);
  }

  public String getActualCTPExecutionPlan(final QueryExecution execution)
      throws SQLException, UnsupportedTorcsCTPDatabaseException {
    System.out.println(">>> ! getActualCTPExecutionPlan()");
    CTPPlanRetriever r = this.planRetrievers.get(execution.getDataSourceReference().getId());
    if (r == null) {
      r = get(execution);
    }
    return r.getActualCTPExecutionPlan(execution);
  }

  private synchronized CTPPlanRetriever get(final QueryExecution execution)
      throws SQLException, UnsupportedTorcsCTPDatabaseException {
    CTPPlanRetriever r = this.planRetrievers.get(execution.getDataSourceReference().getId());
    if (r == null) {
      r = this.ctpPlanRetrieverFactory.getTorcsCTPPlanRetriever(execution);
      this.planRetrievers.put(execution.getDataSourceReference().getId(), r);
    }
    return r;
  }

}
