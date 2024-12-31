package test;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.hotrod.dynamicsql.DynamicExpressionException;

import app.JULCustomFormatter;
import gen.Account;
import gen.AccountDAO;

public class T5 {

  static {
    JULCustomFormatter.initialize(Level.FINE);
  }

  public static void main(String[] args) throws SQLException, DynamicExpressionException {
    DataSource ds = new SimpleDataSource();
    mainTests(ds);
//    dynTests(ds);
  }

  private static void dynTests(DataSource dataSource) throws DynamicExpressionException, SQLException {

//    // Test CHOOSE
//    AccountDAO c5 = new AccountDAO();
//    Account filter = new Account(null, null, null, null);
//    c5.testChoose(filter);

//    // Test TRIM
//    AccountDAO c5 = new AccountDAO();
//    Account filter = new Account(null, null, null, null);
//    c5.testTrim(filter);

//    // Test FOREACH
//  String[] tags = new String[] { "tag-07", "tag-20", "tag-105" };
//  List<Integer> codes = Arrays.asList(new Integer[] { 1015, 1020, 2024 });
//  Data data = new Data("Daguerrotype", 56000, tags, codes);
//    AccountDAO c5 = new AccountDAO();
//    c5.testForeach(data);

//    // Test BIND
//    String[] tags = new String[] { "tag-07", "tag-20", "tag-105" };
//    List<Integer> codes = Arrays.asList(new Integer[] { 1015, 1020, 2024 });
//    Data data = new Data("CH", 56000, tags, codes);
//    AccountDAO c5 = new AccountDAO();
//    c5.testBind(dataSource, data);

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

  private static void mainTests(DataSource dataSource) throws DynamicExpressionException, SQLException {
    AccountDAO c5 = new AccountDAO();

//    Account filter = new Account(123, "AK", "XX", 5001);
//    Account filter = new Account(null, null, "CHK", null);
//    Account newValues = new Account(400, "YYY", "INV", 707);
//    Account filter = new Account(123, null, null, null);
//    Account newValues = new Account(null, null, "SAV", 707);
    Account entity = new Account(null, "YYY", "INV", 1707);

//    int rows = c5.update(dataSource.getConnection(), filter, newValues);
//    System.out.println("Updated rows: " + rows);

//    long seq = c5.selectSequencePreFetch(conn);
//    System.out.println("seq: " + seq);

    c5.insert(dataSource.getConnection(), entity);
    System.out.println("Insert id=" + entity.getId());

//    entity.setId(null);
//    c5.insert(conn, entity);
//    System.out.println("Insert id=" + entity.getId());

//    long id = c5.selectIdentityPostFetch(conn);
//    System.out.println("id: " + id);

//    rows = c5.delete(conn, filter);
//    System.out.println("Deleted rows: " + rows);

//    List<Account> accounts = c5.select(dataSource, filter);
//    System.out.println("=== Rows (" + accounts.size() + ") ===");
//    accounts.forEach(r -> System.out.println("r: " + r));
  }

}
