package org.hotrod.runtime.livesql.dialects;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrodorm.hotrod.utils.XUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("sqlDialectFactory")
public class SQLDialectFactory {

  private static final String TOOL_NAME = "HotRod";

  public static enum Dialect {
    ORACLE, DB2, POSTGRESQL, SQL_SERVER, MARIADB, MYSQL, SYBASE_ASE, H2, HYPERSQL, DERBY;

    public static SQLDialect resolveDialect(final String dialect, final String databaseName, final String versionString,
        final Integer majorVersion, final Integer minorVersion) {

      if (Dialect.ORACLE.name().equals(dialect)) {
        return new OracleDialect(databaseName, versionString, majorVersion, minorVersion);
      } else if (Dialect.DB2.name().equals(dialect)) {
        return new DB2Dialect(databaseName, versionString, majorVersion, minorVersion);
      } else if (Dialect.POSTGRESQL.name().equals(dialect)) {
        return new PostgreSQLDialect(databaseName, versionString, majorVersion, minorVersion);
      } else if (Dialect.SQL_SERVER.name().equals(dialect)) {
        return new SQLServerDialect(databaseName, versionString, majorVersion, minorVersion);
      } else if (Dialect.MARIADB.name().equals(dialect)) {
        return new MariaDBDialect(databaseName, versionString, majorVersion, minorVersion);
      } else if (Dialect.MYSQL.name().equals(dialect)) {
        return new MySQLDialect(databaseName, versionString, majorVersion, minorVersion);
      } else if (Dialect.SYBASE_ASE.name().equals(dialect)) {
        return new SybaseASEDialect(databaseName, versionString, majorVersion, minorVersion);
      } else if (Dialect.H2.name().equals(dialect)) {
        return new H2Dialect(databaseName, versionString, majorVersion, minorVersion);
      } else if (Dialect.DERBY.name().equals(dialect)) {
        return new DerbyDialect(databaseName, versionString, majorVersion, minorVersion);
      } else if (Dialect.HYPERSQL.name().equals(dialect)) {
        return new HyperSQLDialect(databaseName, versionString, majorVersion, minorVersion);
      } else { // Invalid
        StringBuilder sb = new StringBuilder();
        for (Dialect d : Dialect.values()) {
          sb.append(" - " + d.name() + "\n");
        }
        throw new RuntimeException("[" + SQLDialectFactory.class.getSimpleName()
            + "] Could not resolve the SQL dialect. Invalid property 'dialect' with value '" + dialect
            + "'. Valid values are:\n" + sb.toString());
      }

    }

  }

  @Autowired
  private DataSource dataSource = null;

  private String dialect = null;
  private String databaseName = null;
  private String versionString = null;
  private Integer majorVersion = null;
  private Integer minorVersion = null;

  private SQLDialect sqlDialect = null;

  // Datasource option

  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  // Designated dialect

  public void setDialect(final String dialect) {
    this.dialect = dialect;
  }

  public void setDatabaseName(final String databaseName) {
    this.databaseName = databaseName;
  }

  public void setVersionString(final String versionString) {
    this.versionString = versionString;
  }

  public void setMajorVersion(final Integer majorVersion) {
    this.majorVersion = majorVersion;
  }

  public void setMinorVersion(final Integer minorVersion) {
    this.minorVersion = minorVersion;
  }

  // Getters

  public SQLDialect getSqlDialect() {
    if (this.sqlDialect == null) {
      synchronized (this) {
        if (this.sqlDialect == null) {
          try {
            this.sqlDialect = resolveSQLDialect();
          } catch (RuntimeException e) {
            // Show abridged stack trace always. Can help debugging
            System.out.println("---\n" + XUtil.renderThrowable(e) + "\n---");
            throw e;
          }
        }
      }
    }
    return this.sqlDialect;
  }

  // Utilities

  private SQLDialect resolveSQLDialect() {

    if (this.dataSource != null && this.dialect != null) {
      throw new RuntimeException("[" + this.getClass().getSimpleName() + "] Could not resolve the SQL dialect. "
          + "Either the 'dataSource' or 'dialect' property can be set, but not both.");
    }
    if (this.dataSource == null && this.dialect == null) {
      throw new RuntimeException("[" + this.getClass().getSimpleName() + "] Could not resolve the SQL dialect. "
          + "Either 'dataSource' or 'dialect' must be specified.");
    }

    if (this.dataSource != null) {
      try {
        return resolveFromDatasource();
      } catch (SQLException e) {
        throw new RuntimeException(e.getMessage(), e.getCause());
      }
    } else {
      try {
        return resolveFromDialect();
      } catch (SQLException e) {
        throw new RuntimeException(e.getMessage(), e.getCause());
      }
    }

  }

  private SQLDialect resolveFromDialect() throws SQLException {

    if (this.databaseName == null) {
      throw new SQLException("[" + this.getClass().getSimpleName() + "] Could not resolve the SQL dialect. "
          + "The 'databaseName' property is not set.\n"
          + "For most dialects this property is not relevant and you can specify an empty string. "
          + "The typical exception to the rule is IBM DB2 where z/OS versions differ considerably from LUW versions, even when the version numbers are similar.");
    }
    if (this.versionString == null) {
      throw new SQLException("[" + this.getClass().getSimpleName() + "] Could not resolve the SQL dialect. "
          + "The 'versionString' property is not set.\n"
          + "For most dialects this property is not relevant and you can specify an empty string. "
          + "The typical exception to the rule is for MariaDB implementations where only the version string can identify it from MySQL (but not the version number).");
    }
    if (this.majorVersion == null) {
      throw new SQLException("[" + this.getClass().getSimpleName() + "] Could not resolve the SQL dialect. "
          + "The 'majorVersion' property is not set.\n" + "This property helps to identify the SQL subdialect. "
          + "For example, the Oracle 12.1 SQL subdialect implements OFFSET differently compared to older versions.");
    }
    if (this.minorVersion == null) {
      throw new SQLException("[" + this.getClass().getSimpleName() + "] Could not resolve the SQL dialect. "
          + "The 'minorVersion' property is not set.\n" + "This property helps to identify the SQL subdialect. "
          + "For example, the Oracle 12.1 SQL subdialect implements OFFSET differently compared to older versions.");
    }

    return Dialect.resolveDialect(this.dialect, this.databaseName, this.versionString, this.majorVersion,
        this.minorVersion);

  }

  private SQLDialect resolveFromDatasource() throws SQLException {
    Connection conn = this.dataSource.getConnection();
    DatabaseMetaData dm = conn.getMetaData();

    String name = dm.getDatabaseProductName();
    String version = dm.getDatabaseProductVersion();
    int majorVersion = dm.getDatabaseMajorVersion();
    int minorVersion = dm.getDatabaseMinorVersion();

    String uName = name.toUpperCase();

    if (name.equalsIgnoreCase("ORACLE")) {
      return new OracleDialect(name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("HSQL")) {
      return new HyperSQLDialect(name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("H2")) {
      return new H2Dialect(name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("MYSQL")) {
      String productVersion = dm.getDatabaseProductVersion().toLowerCase();
      if (productVersion != null && productVersion.contains("mariadb")) {
        return new MariaDBDialect(name, version, majorVersion, minorVersion);
      } else {
        return new MySQLDialect(name, version, majorVersion, minorVersion);
      }
    } else if (uName.startsWith("ADAPTIVE SERVER ENTERPRISE")) {
      return new SybaseASEDialect(name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("DB2")) {
      return new DB2Dialect(name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("POSTGRESQL")) {
      return new PostgreSQLDialect(name, version, majorVersion, minorVersion);
    } else if (name.startsWith("Microsoft SQL Server")) {
      return new SQLServerDialect(name, version, majorVersion, minorVersion);
    } else if (uName.startsWith("APACHE DERBY")) {
      return new DerbyDialect(name, version, majorVersion, minorVersion);
    } else {
      throw new SQLException("[" + this.getClass().getSimpleName() + "] Could not resolve the SQL dialect. "
          + "The database product reported by the JDBC driver '" + name + "' is not supported by " + TOOL_NAME + ".");
    }
  }

}
