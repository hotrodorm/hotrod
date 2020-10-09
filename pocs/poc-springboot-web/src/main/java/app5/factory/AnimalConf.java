package app5.factory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app5.persistence.ProductVO;
import app5.persistence.primitives.ProductDAO;

@Configuration
public class AnimalConf {

  @Autowired
  private ProductDAO productDAO;

  @Bean
  public Animal myObject() {
    List<ProductVO> products = this.productDAO.selectByExample(new ProductVO());
    if (products.isEmpty()) {
      return new Ant();
    } else {
      return new Duck();
    }
  }

}
