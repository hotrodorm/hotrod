package app;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hsqldb.cmdline.SqlFile;

import app.gen.Account;
import app.gen.AccountDAO;

public class T5 {

  static {
    JULCustomFormatter.initialize(Level.INFO);
  }

  public static void main(String[] args) throws SQLException, DynamicExpressionException {

    Connection conn = getConnection();

//    mainTests(conn);
    dynTests(conn);

  }

  private static void dynTests(Connection conn) throws DynamicExpressionException, SQLException {

//    // Test CHOOSE
//    AccountDAO c5 = new AccountDAO();
//    Account filter = new Account(null, null, null, null);
//    c5.testChoose(filter);

//    // Test TRIM
//    AccountDAO c5 = new AccountDAO();
//    Account filter = new Account(null, null, null, null);
//    c5.testTrim(filter);

    // Test FOREACH
    AccountDAO c5 = new AccountDAO();
    String[] tags = new String[] { "tag-07", "tag-20", "tag-105" };
    List<Integer> codes = Arrays.asList(new Integer[] { 1015, 1020, 2024 });
    Data data = new Data("Daguerrotype", 56000, tags, codes);
    c5.testForeach(data);

  }

  public static class Data {

    private String name;
    private Integer price;
    private String[] tags;
    private List<Integer> codes;

    public Data(String name, Integer price, String[] tags, List<Integer> codes) {
      this.name = name;
      this.price = price;
      this.tags = tags;
      this.codes = codes;
    }

    public String getName() {
      return name;
    }

    public Integer getPrice() {
      return price;
    }

    public String[] getTags() {
      return tags;
    }

    public List<Integer> getCodes() {
      return codes;
    }

  }

  private static void mainTests(Connection conn) throws DynamicExpressionException, SQLException {
    AccountDAO c5 = new AccountDAO();

//    Account filter = new Account(123, "AK", "XX", 5001);
    Account filter = new Account(124, null, "CHK", null);
//    Account newValues = new Account(400, "YYY", "INV", 707);
//    Account filter = new Account(123, null, null, null);
    Account newValues = new Account(null, null, "SAV", 707);
    Account entity = new Account(null, "YYY", "INV", 1707);

//    int rows = c5.update(conn, filter, newValues);
//    System.out.println("Updated rows: " + rows);

//    long seq = c5.selectSequencePreFetch(conn);
//    System.out.println("seq: " + seq);

    c5.insert(conn, entity);
    System.out.println("Insert id=" + entity.getId());

    entity.setId(null);
    c5.insert(conn, entity);
    System.out.println("Insert id=" + entity.getId());

//    long id = c5.selectIdentityPostFetch(conn);
//    System.out.println("id: " + id);

//    rows = c5.delete(conn, filter);
//    System.out.println("Deleted rows: " + rows);

//    List<Account> accounts = c5.select(conn, filter);
//    System.out.println("=== Rows (" + accounts.size() + ") ===");
//    accounts.forEach(r -> System.out.println("r: " + r));
  }

  private static Connection getConnection() throws SQLException {
//    return getOracleConnection();
//    return getDB2Connection();
//    return getPostgreSQLConnection();
//    return getSQLServerConnection();
//    return getMySQLConnection();
//    return getMariaDBConnection();
//    return getSybaseASEDBConnection();
    return getH2Connection();
//    return getHyperSQLConnection();
//    return getDerbyConnection();
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
