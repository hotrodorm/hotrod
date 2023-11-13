package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

public class PlanRetrieverFactory {

  public static TorcsPlanRetriever getTorcsPlanRetriever(final DataSource ds)
      throws SQLException, UnsupportedTorcsDatabaseException {
    try (Connection conn = ds.getConnection()) {
      DatabaseMetaData dm = conn.getMetaData();
      String name = dm.getDatabaseProductName();
      if (name.startsWith("H2")) {
        return new H2TorcsPlanRetriever();
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
