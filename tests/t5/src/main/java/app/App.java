package app;

import java.sql.SQLException;
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

import app.daos.primitives.AccountDAO;

@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses = LiveSQL.class)
@ComponentScan(basePackageClasses = SpringBeanObjectFactory.class)
@MapperScan(basePackageClasses = LiveSQL.class)
public class App {

  private static final Logger log = Logger.getLogger(App.class.getName());

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
      System.out.println("[ Starting... ]");
      test();
      System.out.println("[ Ending ]");
    };
  }

  private void test() throws SQLException, DynamicExpressionException {
    int rows = this.accountDAO.delete(5);
    System.out.println("Rows deleted: " + rows);
  }

}
