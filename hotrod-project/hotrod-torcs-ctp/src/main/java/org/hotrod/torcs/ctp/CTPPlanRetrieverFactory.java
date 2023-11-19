package org.hotrod.torcs.ctp;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.ctp.db2.DB2CTPPlanRetriever;
import org.hotrod.torcs.ctp.oracle.OracleCTPPlanRetriever;
import org.hotrod.torcs.ctp.postgresql.PostgreSQLCTPPlanRetriever;
import org.hotrod.torcs.ctp.sqlserver.SQLServerCTPPlanRetriever;
import org.springframework.stereotype.Component;

@Component
public class CTPPlanRetrieverFactory {

  public CTPPlanRetriever getTorcsCTPPlanRetriever(final QueryExecution execution)
      throws SQLException, UnsupportedTorcsCTPDatabaseException {
    try (Connection conn = execution.getDataSourceReference().getDataSource().getConnection();) {
      DatabaseMetaData dm = conn.getMetaData();
      String name = dm.getDatabaseProductName();
      String uName = name.toUpperCase();
      if (name.equalsIgnoreCase("ORACLE")) {
        return new OracleCTPPlanRetriever();
      } else if (uName.startsWith("DB2")) {
        return new DB2CTPPlanRetriever();
      } else if (uName.startsWith("POSTGRESQL")) {
        return new PostgreSQLCTPPlanRetriever();
      } else if (name.startsWith("Microsoft SQL Server")) {
        return new SQLServerCTPPlanRetriever();
      }
      throw new UnsupportedTorcsCTPDatabaseException("Database not supported by Torcs CTP: " + name + " version "
          + dm.getDatabaseMajorVersion() + "." + dm.getDatabaseMinorVersion() + " (" + dm.getDatabaseProductVersion()
          + "). The databases supported by Torcs CTP are: Oracle, DB2, PostgreSQL, and SQL Server.");
    }

  }

  public static class UnsupportedTorcsCTPDatabaseException extends Exception {

    private static final long serialVersionUID = 1L;

    public UnsupportedTorcsCTPDatabaseException(String message) {
      super(message);
    }

  }

}
