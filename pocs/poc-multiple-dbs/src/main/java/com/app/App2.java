package com.app;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.app.mysql.primitives.IngredientDAO;
import com.app.mysql.primitives.IngredientVO;
import com.app.postgresql.primitives.InvoiceDAO;
import com.app.postgresql.primitives.InvoiceVO;

@Configuration
@ComponentScan(basePackageClasses = App2.class)
@SpringBootApplication
public class App2 {

  @Autowired
  @Qualifier("dataSource1")
  private DataSource datasource1;

  @Autowired
  @Qualifier("dataSource2")
  private DataSource datasource2;

  @Autowired
  private IngredientDAO ingredientDAO;

  @Autowired
  private InvoiceDAO invoiceDAO;

  public static void main(String[] args) {
    System.out.println("=== App Starting ===");
    SpringApplication.run(App2.class, args);
    System.out.println("=== App Complete ===");
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
      searching();
      System.out.println("[ Example complete ]");
    };
  }

  private void searching() throws SQLException {

    IngredientVO ig = this.ingredientDAO.selectByPK(123);
    System.out.println("Ingredient #123: " + ig);

    InvoiceVO i = this.invoiceDAO.selectByPK(1015);
    System.out.println("Invoice #1015: " + i);

  }

}
