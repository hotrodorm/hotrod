package org.hotrod.runtime.livesql.dialects;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrodorm.hotrod.utils.SUtil;

public class LiveSQLDialectFactory {

  public static LiveSQLDialect getLiveSQLDialect(final DataSource dataSource, final String liveSQLDialectName,
      final String liveSQLDialectVDatabaseName, final String liveSQLDialectVersionString,
      final String liveSQLDialectMajorVersion, final String liveSQLDialectMinorVersion) throws LiveSQLDialectException {

    if (dataSource == null && SUtil.isEmpty(liveSQLDialectName)) {
      throw new RuntimeException(
          "Either the dataSource of the liveSQLDialectName must be provided, but both were null.");
    }

    try {

      if (!SUtil.isEmpty(liveSQLDialectName)) {
        Integer majorVersion = null;
        Integer minorVersion = null;
        try {
          if (!SUtil.isEmpty(liveSQLDialectMajorVersion)) {
            majorVersion = Integer.valueOf(liveSQLDialectMajorVersion);
          }
        } catch (NumberFormatException e) {
          throw new LiveSQLDialectException(
              "Could not designate the SQL dialect. The 'livesqlmajorversion' property has an invalid numeric value: "
                  + liveSQLDialectMajorVersion);
        }
        try {
          if (!SUtil.isEmpty(liveSQLDialectMinorVersion)) {
            minorVersion = Integer.valueOf(liveSQLDialectMinorVersion);
          }
        } catch (NumberFormatException e) {
          throw new LiveSQLDialectException(
              "Could not designate the SQL dialect. The 'livesqlminorversion' property has an invalid numeric value: "
                  + liveSQLDialectMinorVersion);
        }

        LiveSQLDialect sqlDialect = DesignatedLiveSQLDialect.resolveDesignatedDialect(liveSQLDialectName, liveSQLDialectVDatabaseName,
            liveSQLDialectVersionString, majorVersion, minorVersion);
        return sqlDialect;
      } else {
        try {
          LiveSQLDialect sqlDialect = discoverFromDatasource(dataSource);
          return sqlDialect;
        } catch (SQLException e) {
          throw new LiveSQLDialectException(e.getMessage(), e.getCause());
        }
      }

    } catch (RuntimeException e) {
      throw new LiveSQLDialectException("Could not resolve SQL Dialect", e);
    }
  }

  public static enum DesignatedLiveSQLDialect {
    ORACLE, DB2, POSTGRESQL, SQL_SERVER, MARIADB, MYSQL, SYBASE_ASE, H2, HYPERSQL, DERBY;

    public static LiveSQLDialect resolveDesignatedDialect(final String liveSQLDialectName,
        final String liveSQLDialectDatabaseName, final String liveSQLDialectVersionString,
        final Integer liveSQLDialectMajorVersion, final Integer liveSQLDialectMinorVersion) {

      if (DesignatedLiveSQLDialect.ORACLE.name().equals(liveSQLDialectName)) {
        return new OracleDialect(false, liveSQLDialectDatabaseName, liveSQLDialectVersionString,
            liveSQLDialectMajorVersion, liveSQLDialectMinorVersion);
      } else if (DesignatedLiveSQLDialect.DB2.name().equals(liveSQLDialectName)) {
        return new DB2Dialect(false, liveSQLDialectDatabaseName, liveSQLDialectVersionString,
            liveSQLDialectMajorVersion, liveSQLDialectMinorVersion);
      } else if (DesignatedLiveSQLDialect.POSTGRESQL.name().equals(liveSQLDialectName)) {
        return new PostgreSQLDialect(false, liveSQLDialectDatabaseName, liveSQLDialectVersionString,
            liveSQLDialectMajorVersion, liveSQLDialectMinorVersion);
      } else if (DesignatedLiveSQLDialect.SQL_SERVER.name().equals(liveSQLDialectName)) {
        return new SQLServerDialect(false, liveSQLDialectDatabaseName, liveSQLDialectVersionString,
            liveSQLDialectMajorVersion, liveSQLDialectMinorVersion);
      } else if (DesignatedLiveSQLDialect.MARIADB.name().equals(liveSQLDialectName)) {
        return new MariaDBDialect(false, liveSQLDialectDatabaseName, liveSQLDialectVersionString,
            liveSQLDialectMajorVersion, liveSQLDialectMinorVersion);
      } else if (DesignatedLiveSQLDialect.MYSQL.name().equals(liveSQLDialectName)) {
        return new MySQLDialect(false, liveSQLDialectDatabaseName, liveSQLDialectVersionString,
            liveSQLDialectMajorVersion, liveSQLDialectMinorVersion);
      } else if (DesignatedLiveSQLDialect.SYBASE_ASE.name().equals(liveSQLDialectName)) {
        return new SybaseASEDialect(false, liveSQLDialectDatabaseName, liveSQLDialectVersionString,
            liveSQLDialectMajorVersion, liveSQLDialectMinorVersion);
      } else if (DesignatedLiveSQLDialect.H2.name().equals(liveSQLDialectName)) {
        return new H2Dialect(false, liveSQLDialectDatabaseName, liveSQLDialectVersionString, liveSQLDialectMajorVersion,
            liveSQLDialectMinorVersion);
      } else if (DesignatedLiveSQLDialect.DERBY.name().equals(liveSQLDialectName)) {
        return new DerbyDialect(false, liveSQLDialectDatabaseName, liveSQLDialectVersionString,
            liveSQLDialectMajorVersion, liveSQLDialectMinorVersion);
      } else if (DesignatedLiveSQLDialect.HYPERSQL.name().equals(liveSQLDialectName)) {
        return new HyperSQLDialect(false, liveSQLDialectDatabaseName, liveSQLDialectVersionString,
            liveSQLDialectMajorVersion, liveSQLDialectMinorVersion);
      } else { // Invalid
        StringBuilder sb = new StringBuilder();
        for (DesignatedLiveSQLDialect d : DesignatedLiveSQLDialect.values()) {
          sb.append(" - " + d.name() + "\n");
        }
        throw new RuntimeException("[" + LiveSQLDialectFactory.class.getSimpleName()
            + "] Could not resolve the SQL dialect. Invalid property 'livesqldialectname' with value '"
            + liveSQLDialectName + "'. Valid values are:\n" + sb.toString());
      }

    }

  }

  // Utilities

  private static LiveSQLDialect discoverFromDatasource(final DataSource dataSource) throws SQLException {
    Connection conn = dataSource.getConnection();
    DatabaseMetaData dm = conn.getMetaData();

    String name = dm.getDatabaseProductName();
    String version = dm.getDatabaseProductVersion();
    int majorVersion = dm.getDatabaseMajorVersion();
    int minorVersion = dm.getDatabaseMinorVersion();

    String uName = name.toUpperCase();

    if (name.equalsIgnoreCase("ORACLE")) {
      return new OracleDialect(true, name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("HSQL")) {
      return new HyperSQLDialect(true, name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("H2")) {
      return new H2Dialect(true, name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("MYSQL")) {
      String productVersion = dm.getDatabaseProductVersion().toLowerCase();
      if (productVersion != null && productVersion.contains("mariadb")) {
        return new MariaDBDialect(true, name, version, majorVersion, minorVersion);
      } else {
        return new MySQLDialect(true, name, version, majorVersion, minorVersion);
      }
    } else if (uName.startsWith("ADAPTIVE SERVER ENTERPRISE")) {
      return new SybaseASEDialect(true, name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("DB2")) {
      return new DB2Dialect(true, name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("POSTGRESQL")) {
      return new PostgreSQLDialect(true, name, version, majorVersion, minorVersion);
    } else if (name.startsWith("Microsoft SQL Server")) {
      return new SQLServerDialect(true, name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("APACHE DERBY")) {
      return new DerbyDialect(true, name, version, majorVersion, minorVersion);
    } else {
      throw new SQLException("[" + LiveSQLDialectFactory.class.getSimpleName() + "] Could not resolve the SQL dialect. "
          + "The database product reported by the JDBC driver '" + name + "' is not supported.");
    }
  }

  public static class LiveSQLDialectException extends Exception {

    private static final long serialVersionUID = 1L;

    public LiveSQLDialectException(String message, Throwable cause) {
      super(message, cause);
    }

    public LiveSQLDialectException(String message) {
      super(message);
    }

  }

}
