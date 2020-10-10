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

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {

      // Testing Log4j2 configuration

      log.trace("Starting TRACE"); // should not be shown
      log.debug("Starting DEBUG"); // should be shown
      log.info("Starting INFO"); // should be shown

      // Using DAO

      log.info("Getting products using DAO...");
      List<ProductVO> products = this.productDAO.selectByExample(new ProductVO());
      log.info(" - Found " + products.size() + " product(s)");

      // Using LiveSQL

      log.info("Counting products using LiveSQL...");
      ProductTable p = ProductDAO.newTable();
      List<Map<String, Object>> result = sql.select(sql.count().as("cnt")).from(p).execute();
      long count = (long) result.get(0).get("cnt");
      log.info(" - Total of " + count + " product(s)");

      log.info("Complete");

    };
  }

}
