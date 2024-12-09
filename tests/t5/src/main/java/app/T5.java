package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.hotrod.dynamic.DynamicExpressionException;

import app.gen.Account;
import app.gen.AccountDAO;

public class T5 {

  public static void main(String[] args) throws SQLException, DynamicExpressionException {

    Connection conn = getConnection();

    AccountDAO c5 = new AccountDAO();

    Account account = new Account(123, "1015", "CHK", 500);
    int rows = c5.update(conn, account);

    System.out.println("Updated rows: " + rows);

  }

  private static Connection getConnection() throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "sa");
    connectionProps.put("password", "");
    return DriverManager.getConnection("jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './build.sql';DB_CLOSE_DELAY=-1",
        connectionProps);
  }

}
