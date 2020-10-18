package app5.cursors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app5.persistence.ProductVO;

@Component
public class CursorExamples {

  @Autowired
  private ProductsManager productsManager;

  @Transactional
  public int computeAvgPriceProducts() {
    int total = 0;
    int count = 0;
    for (ProductVO p : this.productsManager.getExpensiveProducts()) {
      if (p.getPrice() != null) {
        total = total + p.getPrice();
        count++;
      }
    }
    return total / count;
  }

}
