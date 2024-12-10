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
    Account filter = new Account(123, null, "XX", null);
//    Account newValues = new Account(400, "YYY", "INV", 707);
//    Account filter = new Account(123, null, null, null);
    Account newValues = new Account(null, null, null, 707);

    int rows = c5.update(conn, filter, newValues);
    System.out.println("Updated rows: " + rows);

    rows = c5.delete(conn, filter);
    System.out.println("Deleted rows: " + rows);

  }

  private static Connection getConnection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "sa");
    connectionProps.put("password", "");
    return DriverManager.getConnection("jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './build.sql';DB_CLOSE_DELAY=-1",
        connectionProps);
  }

}
