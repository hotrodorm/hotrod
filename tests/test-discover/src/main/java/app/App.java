package app;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import app.daos.AccountVO;
import app.daos.primitives.AccountDAO;
import app.daos.primitives.CompanyDAO;
import app.daos.primitives.CompanyDAO.CompanyTable;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
public class App {

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
      System.out.println("[ Starting example ]");

      searching();
      System.out.println("[ Example complete ]");
    };
  }

  private void searching() {

    // Use CRUD to search for account #123

    Integer id = 123;
    AccountVO a = this.accountDAO.selectByPK(id);
    System.out.println("Account #" + id + " Name: " + a.getName());

    // Use LiveSQL to search for companies which name start with 'A'

    CompanyTable c = CompanyDAO.newTable();

    List<Row> rows = this.sql.select().from(c).where(c.name.like("A%")).execute();

    System.out.println("Companies with names that start with 'A':");
    for (Map<String, Object> r : rows) {
      System.out.println(r);
    }

  }

}
