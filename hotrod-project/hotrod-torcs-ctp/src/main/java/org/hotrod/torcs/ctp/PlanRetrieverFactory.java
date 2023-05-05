package org.hotrod.torcs.ctp;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.torcs.ctp.plans.DB2PlanRetriever;
import org.hotrod.torcs.ctp.plans.OraclePlanRetriever;
import org.hotrod.torcs.ctp.plans.PostgreSQLPlanRetriever;
import org.hotrod.torcs.ctp.plans.SQLServerPlanRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanRetrieverFactory {

  @Autowired
  private LiveSQLDialect liveSQLDialect;

  @Autowired
  private OraclePlanRetriever oraclePlanRetriever;

  @Autowired
  private DB2PlanRetriever db2PlanRetriever;

  @Autowired
  private PostgreSQLPlanRetriever PostgreSQLPlanRetriever;

  @Autowired
  private SQLServerPlanRetriever sqlServerPlanRetriever;

  public PlanRetriever getPlanRetriever() throws TorcsDatabaseNotSupportedException {
    String name = this.liveSQLDialect.getProductName();
    String uName = name.toUpperCase();
    if (name.equalsIgnoreCase("ORACLE")) {
      return this.oraclePlanRetriever;
    } else if (uName.startsWith("DB2")) {
      return this.db2PlanRetriever;
    } else if (uName.startsWith("POSTGRESQL")) {
      return this.PostgreSQLPlanRetriever;
    } else if (name.startsWith("Microsoft SQL Server")) {
      return this.sqlServerPlanRetriever;
    }
    throw new TorcsDatabaseNotSupportedException("Database not supported by Torcs CTP: "
        + this.liveSQLDialect.getProductName() + " version " + this.liveSQLDialect.getMajorVersion() + "."
        + this.liveSQLDialect.getMinorVersion() + " (" + this.liveSQLDialect.getProductVersion()
        + "). The supported databases are Oracle, DB2, PostgreSQL, and SQL Server.");
  }

  public static class TorcsDatabaseNotSupportedException extends Exception {

    private static final long serialVersionUID = 1L;

    protected TorcsDatabaseNotSupportedException(String message) {
      super(message);
    }

  }

}
