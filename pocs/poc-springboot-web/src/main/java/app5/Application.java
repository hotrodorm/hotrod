package app5;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import app5.factory.Animal;
import app5.persistence.ProductVO;
import app5.persistence.primitives.ProductDAO;
import app5.persistence.primitives.ProductDAO.ProductTable;

@Configuration
@ComponentScan(basePackageClasses = Application.class)
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Application {

  private static final Logger log = LogManager.getLogger(Application.class);

  @Autowired
  private ProductDAO productDAO;

  @Autowired
  private LiveSQL sql;

  @Autowired
  private Animal animal;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {

      log.info("[Starting INFO]");
      log.debug("[Starting DEBUG]");
      log.trace("[Starting TRACE]");
      System.out.println("[ Starting... ]");

      this.animal.talk();
      this.animal.salute();

      System.out.println("[ Now will read database ]");

      List<ProductVO> products = this.productDAO.selectByExample(new ProductVO());
      System.out.println("[ Found " + products.size() + " product(s) ]");

      countProducts();

      System.out.println("[ Completed ]");
      log.info("[Complete]");

    };
  }

  private void countProducts() {
    System.out.println("Counting products (liveSQL)...");
    try {
      ProductTable p = ProductDAO.newTable("p");

      List<Map<String, Object>> result = this.sql.select(sql.count().as("cnt")).from(p).execute();

      Map<String, Object> row0 = result.get(0);

      long c = (long) row0.get("cnt");

      System.out.println(" - Total of " + c + " product(s)");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
