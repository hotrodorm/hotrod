package com.app;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.app.mysql.IngredientImpl;
import com.app.mysql.primitives.IngredientDAO;
import com.app.postgresql.primitives.InvoiceDAO;
import com.app.postgresql.primitives.InvoiceDAO.InvoiceTable;

@Configuration
@ComponentScan(basePackageClasses = App.class)
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
@SpringBootApplication
public class App {

  @Autowired
  private IngredientDAO ingredientDAO;

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

    // Search database 1 for the ingredient "Cabbage"

    IngredientImpl example = new IngredientImpl();
    example.setName("Cabbage");
    List<IngredientImpl> ingredients = this.ingredientDAO.selectByExample(example);
    for (IngredientImpl i : ingredients) {
      System.out.println("Ingredient Name: " + i.getName() + " (id: " + i.getId() + ")");
    }

    // Search database 2 for invoices for $1000 or more

    InvoiceTable i = InvoiceDAO.newTable();

    List<Map<String, Object>> l = this.sql.select().from(i).where(i.amount.ge(1000)).execute();

    System.out.println("Invoices for $1000 or more:");
    for (Map<String, Object> r : l) {
      System.out.println(r);
    }

  }

}
