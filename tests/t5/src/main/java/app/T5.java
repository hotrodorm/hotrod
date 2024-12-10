package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.hotrod.dynamic.DynamicExpressionException;

import app.gen.Account;
import app.gen.AccountDAO;

public class T5 {

  static {
    JULCustomFormatter.initialize(Level.INFO);
  }

  public static void main(String[] args) throws SQLException, DynamicExpressionException {

    Connection conn = getConnection();

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

//    long id = c5.selectIdentityPostFetch(conn);
//    System.out.println("id: " + id);

//    rows = c5.delete(conn, filter);
//    System.out.println("Deleted rows: " + rows);

//    List<Account> accounts = c5.select(conn, filter);
//    System.out.println("=== Rows (" + accounts.size() + ") ===");
//    accounts.forEach(r -> System.out.println("r: " + r));
  }

  private static Connection getConnection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "sa");
    connectionProps.put("password", "");
    return DriverManager.getConnection("jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './build.sql';DB_CLOSE_DELAY=-1",
        connectionProps);
  }

}
