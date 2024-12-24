package app;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.spring.SpringBeanObjectFactory;
import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan(basePackageClasses = LiveSQL.class)
public class App {

  private static final Logger log = Logger.getLogger(App.class.getName());

  static {
    JULCustomFormatter.initialize(Level.FINER);
  }

  @Autowired
  private AccountDAO accountDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      log.info("[ Starting... ]");
      test();
      log.info("[ Ending ]");
    };
  }

  private void test() throws SQLException, DynamicExpressionException {
    Account a = new Account();
    a.setId(400);
    a.setName("ACC123");
    a.setType(null);
    a.setBalance(500);

    this.accountDAO.insert(a);
    System.out.println("> Account inserted -- id=" + a.getId());

    Account filter = new Account();
    filter.setType("PEN");
    int cnt = this.accountDAO.delete(filter);
    System.out.println("> Accounts deleted: " + cnt);

  }

}
