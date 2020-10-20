package org.hotrod.runtime.livesql.dialects;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrodorm.hotrod.utils.SUtil;
import org.hotrodorm.hotrod.utils.XUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SQLDialectConfiguration {

  private static final String TOOL_NAME = "HotRod";

  private static final Logger log = LogManager.getLogger(SQLDialectConfiguration.class);

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
        throw new RuntimeException("[" + SQLDialectConfiguration.class.getSimpleName()
            + "] Could not resolve the SQL dialect. Invalid property 'dialect' with value '" + dialect
            + "'. Valid values are:\n" + sb.toString());
      }

    }

  }

  @Autowired
  private DataSource dataSource = null;

  private @Value("${livesql.dialect.name:}") String dialect = null;
  private @Value("${livesql.dialect.databaseName:}") String databaseName = null;
  private @Value("${livesql.dialect.versionString:}") String versionString = null;
  private @Value("${livesql.dialect.majorVersion:}") String sMajorVersion = null;
  private @Value("${livesql.dialect.minorVersion:}") String sMinorVersion = null;

  private Integer majorVersion = null;
  private Integer minorVersion = null;

  private boolean discovered;
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

  @Bean
  public SQLDialect getSqlDialect() {
    if (this.sqlDialect == null) {
      synchronized (this) {
        if (this.sqlDialect == null) {
          try {
            resolveSQLDialect();
            log.info("Dialect " + (this.discovered ? "discovered" : "designated") + " as " + this.sqlDialect);
          } catch (RuntimeException e) {
            // Always show abridged stack trace.
            // It's very useful to debug special obscure errors.
            log.error("Could not resolve SQL Dialect", e);
            System.err.println("---\n" + XUtil.renderThrowable(e) + "\n---");
            throw e;
          }
        }
      }
    }
    return this.sqlDialect;
  }

  // Utilities

  private void resolveSQLDialect() {
    this.sqlDialect = null;
    if (!SUtil.isEmpty(this.dialect)) {
      this.discovered = false;
      this.sqlDialect = resolveDesignatedDialect();
    }
//    log.info("this.sqlDialect=" + this.sqlDialect);
//    log.info("this.dataSource=" + this.dataSource);
    if (this.sqlDialect == null) {
      if (this.dataSource != null) {
        try {
          this.discovered = true;
          this.sqlDialect = discoverFromDatasource();
        } catch (SQLException e) {
          throw new RuntimeException(e.getMessage(), e.getCause());
        }
      } else {
        throw new RuntimeException("[" + this.getClass().getSimpleName() + "] Could not resolve the SQL dialect. "
            + "It can be either be designated using the livesql.dialect.* properties, "
            + "or automatically discovered using the 'dataSource' bean.");
      }
    }
  }

  private SQLDialect resolveDesignatedDialect() {

    // log.info("dialect=" + this.dialect);
    // log.info("databaseName=" + this.databaseName);
    // log.info("versionString=" + this.versionString);
    // log.info("majorVersion=" + this.majorVersion);
    // log.info("minorVersion=" + this.minorVersion);

    if (this.databaseName == null) {
      log.warn("Could not designate the SQL dialect: "
          + "The 'livesql.dialect.databaseName' property is not set -- falling back to dialect discovery mode.\n"
          + "A sample value for this property can be retrieved from the getDatabaseProductName() method of the JDBC metadata. "
          + "For most dialects this property is not relevant and you can specify an empty string. "
          + "The typical exception to the rule is IBM DB2 where z/OS versions differ considerably from LUW versions, "
          + "even when the version numbers are similar.");
      return null;
    }

    if (this.versionString == null) {
      log.warn("Could not designate the SQL dialect. "
          + "The 'livesql.dialect.versionString' property is not set -- falling back to dialect discovery mode.\n"
          + "A sample value for this property can be retrieved from the getDatabaseProductVersion() method of the JDBC metadata. "
          + "For most dialects this property is not relevant and you can specify an empty string. "
          + "The typical exception to the rule is for MariaDB implementations where only the version string "
          + "can identify it from MySQL (but not the version number).");
      return null;
    }

    if (SUtil.isEmpty(this.sMajorVersion)) {
      log.warn("Could not designate the SQL dialect. "
          + "The 'livesql.dialect.majorVersion' property is empty or unset -- falling back to dialect discovery mode.\n"
          + "A sample value for this property can be retrieved from the getDatabaseMajorVersion() method of the JDBC metadata. "
          + "This property helps to identify the SQL subdialect: "
          + "for example, the Oracle 12.1 SQL subdialect implements OFFSET differently compared to older versions.");
      return null;
    }
    try {
      this.majorVersion = Integer.valueOf(this.sMajorVersion);
    } catch (NumberFormatException e) {
      log.warn("Could not designate the SQL dialect. "
          + "The 'livesql.dialect.majorVersion' has an invalid numeric value: " + this.sMajorVersion
          + " -- falling back to dialect discovery mode.\n"
          + "A sample value for this property can be retrieved from the getDatabaseMajorVersion() method of the JDBC metadata. "
          + "This property helps to identify the SQL subdialect: "
          + "for example, the Oracle 12.1 SQL subdialect implements OFFSET differently compared to older versions.");
      return null;
    }

    if (SUtil.isEmpty(this.sMinorVersion)) {
      log.warn("Could not designate the SQL dialect. "
          + "The 'livesql.dialect.minorVersion' property is empty or unset -- falling back to dialect discovery mode.\n"
          + "A sample value for this property can be retrieved from the getDatabaseMinorVersion() method of the JDBC metadata. "
          + "This property helps to identify the SQL subdialect: "
          + "for example, the Oracle 12.1 SQL subdialect implements OFFSET differently compared to older versions.");
      return null;
    }
    try {
      this.minorVersion = Integer.valueOf(this.sMinorVersion);
    } catch (NumberFormatException e) {
      log.warn("Could not designate the SQL dialect. "
          + "The 'livesql.dialect.minorVersion' property has an invalid numeric value: " + this.sMinorVersion
          + " -- falling back to dialect discovery mode.\n"
          + "A sample value for this property can be retrieved from the getDatabaseMinorVersion() method of the JDBC metadata. "
          + "This property helps to identify the SQL subdialect: "
          + "for example, the Oracle 12.1 SQL subdialect implements OFFSET differently compared to older versions.");
      return null;
    }

    return Dialect.resolveDialect(this.dialect, this.databaseName, this.versionString, this.majorVersion,
        this.minorVersion);

  }

  private SQLDialect discoverFromDatasource() throws SQLException {
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
