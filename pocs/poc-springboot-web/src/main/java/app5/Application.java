package app5;

import java.util.List;

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

import app5.cursors.CursorExamples;
import app5.persistence.HistoricPriceVO;
import app5.persistence.ProductVO;
import app5.persistence.primitives.HistoricPriceDAO;
import app5.persistence.primitives.HistoricPriceDAO.HistoricPriceOrderBy;
import app5.persistence.primitives.ProductDAO;

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
  private HistoricPriceDAO historicPriceDAO;

  @Autowired
  private CursorExamples cursorExamples;

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

      log.info("* Getting products using DAO...");
      List<ProductVO> products = this.productDAO.selectByExample(new ProductVO());
      log.info("Found " + products.size() + " product(s)");

      // Retrieving children using FK
      // Enable classic FK navigation by adding the tag <classic-fk-navigation /> in
      // hotrod.xml

      log.info("* Retrieving children using FK...");
      long id = 3L;
      ProductVO p = this.productDAO.selectByPK(id);
      List<HistoricPriceVO> hprices = this.productDAO.selectChildrenHistoricPriceOf(p).fromId()
          .toProductId(HistoricPriceOrderBy.FROM_DATE);
      log.info("Total of " + hprices.size() + " historic prices for product " + id + ".");

      // Retrieving parent using FK
      // Enable classic FK navigation by adding the tag <classic-fk-navigation /> in
      // hotrod.xml

      log.info("* Retrieving parent using FK...");
      HistoricPriceVO h = hprices.get(0);
      ProductVO product = this.historicPriceDAO.selectParentProductOf(h).fromProductId().toId();
      log.info("Parent historic price is: " + product.getName());

      // Compute average price of products

      log.info("* Compute average price of products...");
      int avgPrice = this.cursorExamples.computeAvgPriceProducts();
      log.info("Average price: " + avgPrice);

      // All done

      log.info("* End of example");

    };
  }

}
