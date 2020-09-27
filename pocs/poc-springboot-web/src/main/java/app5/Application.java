package app5;

import java.util.List;

import org.hotrod.runtime.livesql.dialects.SQLDialectFactory;
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

@Configuration
@ComponentScan(basePackageClasses = Application.class)
@ComponentScan(basePackageClasses = SQLDialectFactory.class)

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Application {

  @Autowired
  private ProductDAO productDAO;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {

      System.out.println("[ Now will read database ]");

      List<ProductVO> products = this.productDAO.selectByExample(new ProductVO());
      System.out.println("[ Found " + products.size() + " product(s) ]");

      System.out.println("[ Completed ]");

    };
  }

}
