package org.hotrod.runtime.livesql.dialects;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

public class SQLDialectFactory {

  private static final String TOOL_NAME = "HotRod";

  private DataSource dataSource = null;
  private SQLDialect dialect = null;

  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public SQLDialect getSqlDialect() throws SQLException {
    if (this.dialect == null) {
      synchronized (this) {
        if (this.dialect == null) {
          this.dialect = resolveSqlDialect();
        }
      }
    }
    return this.dialect;
  }

  private SQLDialect resolveSqlDialect() throws SQLException {
    Connection conn = this.dataSource.getConnection();
    DatabaseMetaData dm = conn.getMetaData();

    String name = dm.getDatabaseProductName();
    String version = dm.getDatabaseProductVersion();
    int majorVersion = dm.getDatabaseMajorVersion();
    int minorVersion = dm.getDatabaseMinorVersion();

    String uName = name.toUpperCase();

    if (name.equalsIgnoreCase("ORACLE")) {
      return new OracleDialect(name, version, minorVersion, majorVersion);
    } else if (uName.startsWith("HSQL")) {
      return new HyperSQLDialect(name, version, minorVersion, majorVersion);
    } else if (uName.startsWith("H2")) {
      return new H2Dialect(name, version, minorVersion, majorVersion);
    } else if (uName.startsWith("MYSQL")) {
      String productVersion = dm.getDatabaseProductVersion().toLowerCase();
      if (productVersion != null && productVersion.contains("mariadb")) {
        return new MariaDBDialect(name, version, minorVersion, majorVersion);
      } else {
        return new MySQLDialect(name, version, minorVersion, majorVersion);
      }
    } else if (uName.startsWith("ADAPTIVE SERVER ENTERPRISE")) {
      return new SybaseASEDialect(name, version, minorVersion, majorVersion);
    } else if (uName.startsWith("DB2")) {
      return new DB2Dialect(name, version, minorVersion, majorVersion);
    } else if (uName.startsWith("POSTGRESQL")) {
      return new PostgreSQLDialect(name, version, minorVersion, majorVersion);
    } else if (name.startsWith("Microsoft SQL Server")) {
      return new SQLServerDialect(name, version, minorVersion, majorVersion);
    } else if (uName.startsWith("APACHE DERBY")) {
      return new DerbyDialect(name, version, minorVersion, majorVersion);
    } else {
      throw new SQLException("Could not resolve the database adapter. "
          + "The product name reported by the JDBC driver '" + name + "' is not supported by " + TOOL_NAME + ".");
    }
  }

}
