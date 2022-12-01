package com.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.hotrod.runtime.livesql.Row;

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

import com.app.mysql.primitives.IngredientDAO;
import com.app.mysql.primitives.IngredientDAO.IngredientTable;
import com.app.mysql.primitives.IngredientVO;

@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses = App1.class)

@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
public class App1 {

  @Autowired
  private IngredientDAO ingredientDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    System.out.println("=== App Starting ===");
    SpringApplication.run(App1.class, args);
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

    // DAO
    {
      IngredientVO ig = this.ingredientDAO.selectByPK(123);
      System.out.println("Ingredient #123: " + ig);
    }

    // LiveSQL
    {
      IngredientTable e = IngredientDAO.newTable();
      List<Row> l = this.sql.select().from(e).where(e.name.like("%bb%")).execute();
      System.out.println("Ingredients with two 'b':");
      for (Map<String, Object> r : l) {
        System.out.println(r);
      }
    }

  }

}
