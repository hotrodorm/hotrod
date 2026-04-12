package app;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.OrderTypeADAO;
import app.persistence.dao.OrderTypeADAO.OrderTypeATable;
import app.persistence.model.OrderTypeA;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private OrderTypeADAO orderTypeADAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args).close();
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
      demoCRUD();
      demoLiveSQL();
    };
  }

  private void demoCRUD() {
    OrderTypeA ota = this.orderTypeADAO.select(3);
    System.out.println("Order Case for Order Type A #3: " + ota.getOrderCase());
  }

  private void demoLiveSQL() {
    System.out.println("List all OrderType which Order Case starts with 'B':");

    OrderTypeATable t = this.orderTypeADAO.newTable();
    List<Row> rows = this.sql.select(t.id, t.orderCase).from(t).where(t.orderCase.like("B%")).execute();
    for (Row r : rows) {
      System.out.println(r);
    }

  }

}
