package app;

import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.assembler.QueryAssembler;
import org.hotrod.livesql.Row;
import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.spring.SpringBeanObjectFactory;
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

@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses = LiveSQL.class)
@ComponentScan(basePackageClasses = SpringBeanObjectFactory.class)
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

  @Autowired
  private QueryAssembler assembler;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      log.info("[ Starting... ]");
      test();
//      testLiveSQL();
      log.info("[ Ending ]");
    };
  }

  private void testLiveSQL() throws SQLException, DynamicExpressionException {

//    System.out.println(">> Will run LiveSQL");
//    List<Row> rows = this.sql.select(sql.val(7).mult(3).as("answer")).execute();
//    System.out.println(">> LiveSQL executed");
//    for (Row row : rows) {
//      System.out.println("Row=" + row);
//    }

    System.out.println(">> Will run LiveSQL");
    Row row = this.sql.select(sql.val(7).mult(3).as("answer")).executeOne();
    System.out.println("Row=" + row);
  }

  private void test() throws SQLException, DynamicExpressionException {

    System.out.println("this.assembler=" + this.assembler);

    Account a = this.accountDAO.select(112);
    System.out.println("--> a=" + a);

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
////    List<Account> accounts = this.accountDAO.select(filter);
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
