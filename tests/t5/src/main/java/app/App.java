package app;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.assembler.QueryAssembler;
import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import app.daos.Account;
import app.daos.primitives.AccountDAO;
import app.daos.primitives.AccountDAO.AccountTable;

@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses = LiveSQL.class)
@ComponentScan(basePackageClasses = QueryAssembler.class)
@ComponentScan(basePackageClasses = AccountDAO.class)
public class App {

  private static final Logger log = Logger.getLogger(App.class.getName());

  static {
//    JULCustomFormat4ter.initialize(Level.FINER);
  }

  @Autowired
  private AccountDAO accountDAO;

//  @Autowired
//  private ReportingDAO reportingDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      log.info("[ Starting... ]");
//      test();
//      testLiveSQL();
      testOptimisticLocking();
      log.info("[ Ending ]");
    };
  }

  private void testOptimisticLocking() throws SQLException, DynamicExpressionException {
//    testOLInsert();
//    testOLInsertByExample() ;
//    testOLDeleteUpdate();
    testOLUpdateDelete();
  }

  private void testOLInsert() throws DynamicExpressionException, SQLException {
    Account a = new Account();
    a.setName("1010-4");
    a.setType("CHK");
    a.setBalance(100);
    a.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    a.setActive(true);
    a.setVersion(1);
    this.accountDAO.insert(a);
    System.out.println("--> a=" + a);

    Account b = this.accountDAO.select(a.getId());
    System.out.println("b=" + b);
  }

  private void testOLInsertByExample() throws DynamicExpressionException, SQLException {
    Account a = new Account();
    a.setName("1010-4");
    a.setType("CHK");
    a.setBalance(100);
    a.setActive(true);
    a.setVersion(1);
    this.accountDAO.insertByExample(a);
    System.out.println("--> a=" + a);

    Account b = this.accountDAO.select(a.getId());
    System.out.println("b=" + b);
  }

  private void testOLDeleteUpdate() throws DynamicExpressionException, SQLException {
    Account b = this.accountDAO.select(112);

    Account a = this.accountDAO.select(112);
    System.out.println("--> a=" + a);

    this.accountDAO.deleteWOL(b);
    System.out.println("Deleted b.");

    a.setBalance(a.getBalance() + 100);
    this.accountDAO.update(a);
    System.out.println("Updated a.");
  }

  private void testOLUpdateDelete() throws DynamicExpressionException, SQLException {
    Account b = this.accountDAO.select(112);

    Account a = this.accountDAO.select(112);
    System.out.println("--> a=" + a);

    a.setBalance(a.getBalance() + 100);
    this.accountDAO.update(a);
    System.out.println("Updated a.");

    this.accountDAO.deleteWOL(b);
    System.out.println("Deleted b.");
  }

  private void testLiveSQL() throws SQLException, DynamicExpressionException {

//    System.out.println(">> Will run LiveSQL");
//    Row row = this.sql.select(sql.val(7).mult(3).as("answer")).executeOne();
//    System.out.println("Row=" + row);

    System.out.println("Will UPDATE by criteria.");
    AccountTable a = this.accountDAO.newTable();
    Account updateValues = new Account();
    updateValues.setBalance(777);
    int count = this.accountDAO.update(updateValues, a, a.balance.lt(150)).execute();
    System.out.println("UPDATE by criteria complete: count=" + count);

    List<Account> accounts = this.accountDAO.select(a, sql.TRUE).execute();
    accounts.forEach(r -> System.out.println("r=" + r));

//    System.out.println("Will select by criteria." );
//    AccountTable a = this.accountDAO.newTable();
//    List<Account> accounts = this.accountDAO.select(a, a.type.eq("CHK")).orderBy(a.balance.desc()).execute();
//    for (Account r : accounts) {
//      System.out.println("account=" + r);
//    }
//    System.out.println("Select by criteria complete." );

//    System.out.println("Will DELETE by criteria.");
//    AccountTable a = this.accountDAO.newTable();
//    int count = this.accountDAO.delete(a, a.type.eq("SAV")).execute();
//    System.out.println("DELETE by criteria complete: count=" + count);

//    List<Row> rows = this.sql.select().from(a).execute();
//    for (Row r : rows) {
//      System.out.println("Row=" + r);
//    }
  }

  private void test() throws SQLException, DynamicExpressionException {

//    Account a = this.accountDAO.select(112);
//    System.out.println("--> a=" + a);

//    List<Integer> ids = Arrays.asList(123, 789, 112, 4);

//    int rows = this.reportingDAO.activateBigAccounts(125L, ids);
//      int rows=  this.reportingDAO.activateBigAccounts2(125L, ids, "type = 'CHK'");

    // List<BigAccount> bas = this.reportingDAO.findBigAccounts();
//    for (BigAccount ba : bas) {
//      System.out.println("--> " + ba);
//    }

//    List<Account> bas = this.accountDAO.findBigAccounts();
//    for (Account ba : bas) {
//      System.out.println("--> " + ba);
//    }

    // int rows = this.accountDAO.activateBigAccounts(null);
//    System.out.println("--> rows=" + rows);

//    Account filter = new Account();
//    filter.setType("CHK");
//
//    List<Account> accounts = this.accountDAO.select(filter, AccountOrderBy.ID, AccountOrderBy.NAME$DESC);
//    for (Account a : accounts) {
//      System.out.println("--> " + a);
//    }

//  List<Account> accounts = this.accountDAO.select1();
//  for (Account a : accounts) {
//    System.out.println("--> " + a);
//  }

//    Account a = new Account();
//    a.setId(400);
//    a.setName("ACC123");
//    a.setType(null);
//    a.setBalance(true);
//
//    this.accountDAO.insert(a);
//    System.out.println("> Account inserted -- id=" + a.getId());
//
//    Account filter = new Account();
//    filter.setType("PEN");
//    int cnt = this.accountDAO.delete(filter);
//    System.out.println("> Accounts deleted: " + cnt);

  }

}
