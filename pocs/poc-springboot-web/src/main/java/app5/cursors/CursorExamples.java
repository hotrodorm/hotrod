package app5.cursors;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app5.persistence.CheapProductVOVO;
import app5.persistence.ProductVO;
import app5.persistence.primitives.ProductDAO;
import app5.persistence.primitives.ProductDAO.ProductTable;

@Component
public class CursorExamples {

  @Autowired
  private ProductsManager productsManager;

  @Autowired
  private LiveSQL sql;

  @Transactional
  public int computeAvgPriceProducts() {
    int total = 0;
    int count = 0;
    for (ProductVO p : this.productsManager.getProducts()) {
      if (p.getPrice() != null) {
        total = total + p.getPrice();
        count++;
      }
    }
    return total / count;
  }

  @Transactional
  public int computeAvgPriceProductsCriteria() {
    int total = 0;
    int count = 0;
    for (ProductVO p : this.productsManager.getProductsCriteria()) {
      if (p.getPrice() != null) {
        total = total + p.getPrice();
        count++;
      }
    }
    return total / count;
  }

  @Transactional
  public int computeAvgPriceProductsLiveSQL() {
    ProductTable p = ProductDAO.newTable("p");
    ProductTable q = ProductDAO.newTable("q");

    List<Map<String, Object>> rows = sql.select().from(p).join(q, q.id.eq(p.id))
        .where(p.price.ge(0).or(p.name.like("%"))).execute();

    int total = 0;
    int count = 0;
    for (Map<String, Object> r : rows) {
      Integer price = (int) r.get("price");
      if (price != null) {
        total = total + price;
        count++;
      }
    }
    return total / count;
  }

  @Transactional
  public void findCheapProductsCursor() {
    for (CheapProductVOVO p : this.productsManager.getCheapProducts()) {
      System.out.println(" - local: " + p);
    }
  }

}
