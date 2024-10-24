package org.hotrod.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.Constants;
import org.hotrod.database.adapters.ApacheDerbyAdapter;
import org.hotrod.database.adapters.DB2Adapter;
import org.hotrod.database.adapters.H2Adapter;
import org.hotrod.database.adapters.HyperSQLAdapter;
import org.hotrod.database.adapters.MariaDBAdapter;
import org.hotrod.database.adapters.MySQLAdapter;
import org.hotrod.database.adapters.OracleAdapter;
import org.hotrod.database.adapters.PostgreSQLAdapter;
import org.hotrod.database.adapters.SAPASEAdapter;
import org.hotrod.database.adapters.SQLServerAdapter;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnrecognizedDatabaseException;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

public final class DatabaseAdapterFactory {

  private static final Logger log = LogManager.getLogger(DatabaseAdapterFactory.class);

  public static DatabaseAdapter getAdapter(final DatabaseLocation loc)
      throws UnrecognizedDatabaseException, UncontrolledException {
    log.debug("init.");

    Connection conn = null;

    try {

      try {
        conn = loc.getConnection();
      } catch (SQLException e) {
        throw new UncontrolledException("Could not connect to database at URL: " + loc.getUrl(), e);
      }
      return getAdapter(conn);

    } catch (SQLException e) {
      throw new UncontrolledException("Could not retrieve metadata from database at URL: " + loc.getUrl(), e);

    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          log.warn("Could not close connection to database at URL: " + loc.getUrl(), e);
        }
      }
    }
  }

  public static DatabaseAdapter getAdapter(final Connection conn)
      throws UncontrolledException, UnrecognizedDatabaseException, SQLException {
    DatabaseMetaData dm;
    try {
      dm = conn.getMetaData();
    } catch (SQLException e) {
      throw new UncontrolledException("Could not retrieve database information from the JDBC driver.", e);
    }

    String name;
    try {
      name = dm.getDatabaseProductName();
    } catch (SQLException e) {
      throw new UncontrolledException("Could not retrieve the database product name from the JDBC driver.", e);
    }

    if (name == null) {
      throw new UnrecognizedDatabaseException("Could not resolve the database adapter. "
          + "The JDBC driver did not provide " + "the database product name.");
    }

    String uName = name.toUpperCase();

    if (name.equalsIgnoreCase("ORACLE")) {
      return new OracleAdapter(dm);
    } else if (uName.startsWith("HSQL")) {
      return new HyperSQLAdapter(dm);
    } else if (uName.startsWith("H2")) {
      return new H2Adapter(dm);
    } else if (uName.startsWith("MYSQL")) {
      String productVersion = dm.getDatabaseProductVersion().toLowerCase();
      if (productVersion != null && productVersion.contains("mariadb")) {
        return new MariaDBAdapter(dm);
      } else {
        return new MySQLAdapter(dm);
      }
    } else if (uName.startsWith("ADAPTIVE SERVER ENTERPRISE")) {
      return new SAPASEAdapter(dm);
    } else if (uName.startsWith("DB2")) {
      return new DB2Adapter(dm);
    } else if (uName.startsWith("POSTGRESQL")) {
      return new PostgreSQLAdapter(dm);
    } else if (name.startsWith("Microsoft SQL Server")) {
      return new SQLServerAdapter(dm);
    } else if (uName.startsWith("APACHE DERBY")) {
      return new ApacheDerbyAdapter(dm);
    } else {
      throw new UnrecognizedDatabaseException(
          "Could not resolve the database adapter. " + "The product name reported by the JDBC driver '" + name
              + "' is not supported by " + Constants.TOOL_NAME + ".");
    }
  }

}
