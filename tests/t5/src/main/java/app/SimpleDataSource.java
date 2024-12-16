package app;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hsqldb.cmdline.SqlFile;

public class SimpleDataSource implements DataSource {

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Connection getConnection() throws SQLException {
//  return getOracleConnection();
//  return getDB2Connection();
//  return getPostgreSQLConnection();
//  return getSQLServerConnection();
//  return getMySQLConnection();
//  return getMariaDBConnection();
//  return getSybaseASEDBConnection();
  return getH2Connection();
//  return getHyperSQLConnection();
//  return getDerbyConnection();
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }


  private static Connection getOracleConnection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "user1");
    connectionProps.put("password", "pass1");
    return DriverManager.getConnection("jdbc:oracle:thin:@192.168.56.95:1521:orcl", connectionProps);
  }

  private static Connection getDB2Connection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "user1");
    connectionProps.put("password", "pass1");
    return DriverManager.getConnection("jdbc:db2://192.168.56.44:50000/empusa", connectionProps);
  }

  private static Connection getPostgreSQLConnection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "user1");
    connectionProps.put("password", "pass1");
    return DriverManager.getConnection("jdbc:postgresql://192.168.56.200:5416/hotrod", connectionProps);
  }

  private static Connection getSQLServerConnection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "admin");
    connectionProps.put("password", "admin");
    return DriverManager.getConnection("jdbc:sqlserver://192.168.56.51:1433;encrypt=true;trustServerCertificate=true",
        connectionProps);
  }

  private static Connection getMySQLConnection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "user1");
    connectionProps.put("password", "pass1");
    return DriverManager.getConnection("jdbc:mysql://192.168.56.200:3820/hotrod", connectionProps);
  }

  private static Connection getMariaDBConnection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "user1");
    connectionProps.put("password", "pass1");
    return DriverManager.getConnection("jdbc:mysql://192.168.56.200:3111/hotrod", connectionProps);
  }

  private static Connection getSybaseASEDBConnection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "sa");
    connectionProps.put("password", "pass12");
    return DriverManager.getConnection("jdbc:sybase:Tds:192.168.56.52:5000", connectionProps);
  }

  private static Connection getH2Connection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "sa");
    connectionProps.put("password", "");
    return DriverManager.getConnection("jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './build-h2.sql';DB_CLOSE_DELAY=-1",
        connectionProps);
  }

  private static Connection getHyperSQLConnection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "SA");
    connectionProps.put("password", "");
    Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:db1", connectionProps);

    try {
      SqlFile sf = new SqlFile(new File("./build-hypersql.sql"));
      sf.setConnection(conn);
      sf.execute();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return conn;
  }

  private static Connection getDerbyConnection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "schema1");
    connectionProps.put("password", "b");
    return DriverManager.getConnection("jdbc:derby://192.168.56.26:1527/hotrod", connectionProps);
  }

  
}
