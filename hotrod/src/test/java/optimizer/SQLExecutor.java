package optimizer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLExecutor {

  // static final String JDBC_DRIVER = "org.h2.Driver";
  // static final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
  // static final String USER = "sa";
  // static final String PASS = "";

  private static String JDBC_DRIVER = null;
  private static String DB_URL = null;
  private static String USER = null;
  private static String PASS = null;

  private static Connection connection = null;

  public static Connection getConnection() throws SQLException {
    if (connection == null) {
      synchronized (SQLExecutor.class) {
        if (connection == null) {
          initializeConnection();
        }
      }
    }
    return connection;
  }

  private static void initializeConnection() throws SQLException {
    try {
      Class.forName(JDBC_DRIVER);
    } catch (ClassNotFoundException e) {
      throw new SQLException(e);
    }
    connection = DriverManager.getConnection(DB_URL, USER, PASS);
  }

  public static void executeUpdate(final String sql) throws SQLException {
    Connection conn = null;
    PreparedStatement st = null;
    try {
      conn = SQLExecutor.getConnection();
      st = conn.prepareStatement(sql);
      st.execute();
    } finally {
      if (st != null) {
        st.close();
      }
    }
  }

  public static void setJDBCDriver(String jDBC_DRIVER) {
    JDBC_DRIVER = jDBC_DRIVER;
    System.out.println("Driver=" + jDBC_DRIVER);
  }

  public static void setDatabaseUrl(String dB_URL) {
    DB_URL = dB_URL;
    System.out.println("URL=" + dB_URL);
  }

  public static void setUsername(String uSER) {
    USER = uSER;
    System.out.println("username=" + USER);
  }

  public static void setPassword(String pASS) {
    PASS = pASS;
    System.out.println("password=" + PASS);
  }

}
