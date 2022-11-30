package com.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.mybatis.spring.annotation.MapperScan;
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
import com.app.mysql.primitives.IngredientDAO.IngredientTable;
import com.app.mysql.primitives.IngredientVO;
import com.app.postgresql.primitives.InvoiceDAO;
import com.app.postgresql.primitives.InvoiceDAO.InvoiceTable;
import com.app.postgresql.primitives.InvoiceVO;

@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses = App2.class)

//@ComponentScan(basePackageClasses = LiveSQL.class)
//@MapperScan(basePackageClasses = LiveSQL.class)

public class App2 {

  @Autowired
  @Qualifier("liveSQL1")
  private LiveSQL sql1;

  @Autowired
  @Qualifier("liveSQL2")
  private LiveSQL sql2;

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

    // DAO 1
    {
      IngredientVO ig = this.ingredientDAO.selectByPK(123);
      System.out.println("Ingredient #123: " + ig);
    }

    // DAO2
    {
      InvoiceVO i = this.invoiceDAO.selectByPK(1015);
      System.out.println("Invoice #1015: " + i);
    }

    // LiveSQL1
    {
      IngredientTable e = IngredientDAO.newTable();
      List<Map<String, Object>> l = this.sql1.select().from(e).where(e.name.like("%bb%")).execute();
      System.out.println("Ingredients with two 'b':");
      for (Map<String, Object> r : l) {
        System.out.println(r);
      }
    }

    // LiveSQL2
    {
      InvoiceTable i = InvoiceDAO.newTable();
      List<Map<String, Object>> l = this.sql2.select().from(i).where(i.amount.ge(1000)).execute();
      System.out.println("Invoices for more than $1000:");
      for (Map<String, Object> r : l) {
        System.out.println(r);
      }
    }

  }

}
