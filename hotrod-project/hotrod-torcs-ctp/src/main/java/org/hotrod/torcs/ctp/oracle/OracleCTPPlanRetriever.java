package org.hotrod.torcs.ctp.oracle;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.ctp.CTPPlanRetriever;
import org.springframework.stereotype.Component;

@Component
public class OracleCTPPlanRetriever implements CTPPlanRetriever {

  @Override
  public String getEstimatedCTPExecutionPlan(QueryExecution execution) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getActualCTPExecutionPlan(QueryExecution execution) {
    throw new UnsupportedOperationException("Torcs CTP cannot retrieve actual execution plans in the Oracle database. "
        + "To produce it in Oracle an Oracle DBA will enable statistics gathering (statistics_level = 'ALL'), "
        + "then the queries can be run normally, "
        + "and finally the actual plan can be retrieved with an extra privileged procedure. "
        + "This is out of the scope of Torcs CTP.");
  }

}
