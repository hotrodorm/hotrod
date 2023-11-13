package org.hotrod.torcs.ctp;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.torcs.ctp.db2.DB2PlanRetriever;
import org.hotrod.torcs.ctp.h2.GenericH2PlanRetriever;
import org.hotrod.torcs.ctp.h2.GenericH2PlanRetriever.GenericH2PlanMapper;
import org.hotrod.torcs.ctp.oracle.OraclePlanRetriever;
import org.hotrod.torcs.ctp.postgresql.PostgreSQLPlanRetriever;
import org.hotrod.torcs.ctp.sqlserver.SQLServerPlanRetriever;
import org.hotrod.torcs.plan.PlanRetrieverFactory.UnsupportedTorcsDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanRetrieverFactory {

  @Autowired
  private LiveSQLDialect liveSQLDialect;

  private OraclePlanRetriever oraclePlanRetriever = new OraclePlanRetriever();
  private DB2PlanRetriever db2PlanRetriever = new DB2PlanRetriever();
  private PostgreSQLPlanRetriever PostgreSQLPlanRetriever = new PostgreSQLPlanRetriever();
  private SQLServerPlanRetriever sqlServerPlanRetriever = new SQLServerPlanRetriever();

  private GenericH2PlanRetriever dummyH2PlanRetriever;

  public PlanRetriever getPlanRetriever(final GenericH2PlanMapper h2Mapper) throws UnsupportedTorcsDatabaseException {
    System.out.println(">>> PlanRetrieverFactory() 1 this.liveSQLDialect=" + liveSQLDialect);
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
    } else if (name.startsWith("H2")) {
      this.dummyH2PlanRetriever = new GenericH2PlanRetriever(h2Mapper);

      return this.dummyH2PlanRetriever;
    }
    System.out.println(">>> PlanRetrieverFactory() 2");
    throw new UnsupportedTorcsDatabaseException("Database not supported by Torcs CTP: "
        + this.liveSQLDialect.getProductName() + " version " + this.liveSQLDialect.getMajorVersion() + "."
        + this.liveSQLDialect.getMinorVersion() + " (" + this.liveSQLDialect.getProductVersion()
        + "). The databases supported by Torcs CTP are: Oracle, DB2, PostgreSQL, and SQL Server.");
  }

}
