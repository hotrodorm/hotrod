package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

public class PlanRetrieverFactory {

  public static PlanRetriever getTorcsPlanRetriever(final DataSource ds)
      throws SQLException, UnsupportedTorcsDatabaseException {
    try (Connection conn = ds.getConnection()) {
      DatabaseMetaData dm = conn.getMetaData();
      String name = dm.getDatabaseProductName();
      String uName = name.toUpperCase();

      if (name.equalsIgnoreCase("ORACLE")) {
        return new OraclePlanRetriever();
      } else if (uName.startsWith("HSQL")) {
        return new HyperSQLPlanRetriever();
      } else if (uName.startsWith("H2")) {
        return new H2PlanRetriever();
      } else if (uName.startsWith("MYSQL")) {
        String productVersion = dm.getDatabaseProductVersion().toLowerCase();
        if (productVersion != null && productVersion.contains("mariadb")) {
          return new MariaDBPlanRetriever();
        } else {
          return new MySQLPlanRetriever();
        }
      } else if (uName.startsWith("ADAPTIVE SERVER ENTERPRISE")) {
        return new SybaseASEPlanRetriever();
      } else if (uName.startsWith("DB2")) {
        return new DB2PlanRetriever();
      } else if (uName.startsWith("POSTGRESQL")) {
        return new PostgreSQLPlanRetriever();
      } else if (name.startsWith("Microsoft SQL Server")) {
        return new SQLServerPlanRetriever();
      } else if (uName.startsWith("APACHE DERBY")) {
        return new ApacheDerbyPlanRetriever();
      }
      throw new UnsupportedTorcsDatabaseException(
          "Database not supported by Torcs: " + dm.getDatabaseProductName() + " version " + dm.getDatabaseMajorVersion()
              + "." + dm.getDatabaseMinorVersion() + " (" + dm.getDatabaseProductVersion() + ").");
    }

  }

  public static class UnsupportedTorcsDatabaseException extends Exception {

    private static final long serialVersionUID = 1L;

    public UnsupportedTorcsDatabaseException(final String message) {
      super(message);
    }

  }

}
