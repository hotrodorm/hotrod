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

import app5.persistence.HistoricPriceVO;
import app5.persistence.ProductVO;
import app5.persistence.primitives.HistoricPriceDAO;
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
  private HistoricPriceDAO historicPriceDAO;

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

      // Navigating FK to children

      ProductVO p3 = this.productDAO.selectByPK(3L);
      List<HistoricPriceVO> hprices = this.productDAO.selectChildrenHistoricPriceOf(p3).fromId().toProductId();
      log.info(" - Total of " + hprices.size() + " historic prices for product 3.");

      // Navigating FK to parent

      HistoricPriceVO h = hprices.get(0);
      ProductVO product = this.historicPriceDAO.selectParentProductOf(h).fromProductId().toId();
      log.info(" - Parent product is: " + product.getName());

      // All done

      log.info("Complete");

    };
  }

}
