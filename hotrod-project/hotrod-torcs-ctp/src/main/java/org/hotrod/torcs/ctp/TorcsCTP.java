package org.hotrod.torcs.ctp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.ctp.CTPPlanRetrieverFactory.UnsupportedTorcsCTPDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TorcsCTP {

  @Autowired
  private CTPPlanRetrieverFactory ctpPlanRetrieverFactory;

  private LogResistantFormatter fmt = new LogResistantFormatter();

  private Map<Integer, CTPPlanRetriever> planRetrievers = new HashMap<>();

  public List<String> getEstimatedCTPExecutionPlan(final QueryExecution execution)
      throws SQLException, UnsupportedTorcsCTPDatabaseException, IOException {
    CTPPlanRetriever r = this.planRetrievers.get(execution.getDataSourceReference().getId());
    if (r == null) {
      r = get(execution);
    }
    return this.fmt.render(r.getEstimatedCTPExecutionPlan(execution));
  }

  public void setSegmentSize(final int size) {
    this.fmt.setSegmentSize(size);
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
