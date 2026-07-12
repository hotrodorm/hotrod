package app;

import java.util.List;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.TDAO;
import app.persistence.dao.TDAO.TTable;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private TDAO tDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args).close();
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
      demoLiveSQL();
      System.out.println("[ Example complete ]");
    };
  }

  private void demoLiveSQL() {

    System.out.println("Employees with last names that include smith from branches of type 2, 6, or 7:");

    TTable t = this.tDAO.newTable();

    List<Row> rows = this.sql.select().from(t).orderBy(t.id).offset(1).limit(2).execute();

    for (Row r : rows) {
      System.out.println(r);
    }

  }

}
