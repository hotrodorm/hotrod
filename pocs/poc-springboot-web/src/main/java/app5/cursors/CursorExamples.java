package app5.cursors;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app5.persistence.ExpensiveProductVO;
import app5.persistence.HistoricPriceVO;
import app5.persistence.ProductVO;
import app5.persistence.primitives.HistoricPriceDAO.HistoricPriceOrderBy;
import app5.persistence.primitives.ProductDAO;
import app5.persistence.primitives.ProductDAO.ProductTable;

@Component
public class CursorExamples {

  @Autowired
  private ProductsManager productsManager;

  @Autowired
  private ProductDAO productDAO;

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
    for (ProductInterface p : this.productsManager.getProductsCriteria()) {
      System.out.println(" * p=" + p);
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

    List<Row> rows = sql.select().from(p).join(q, q.id.eq(p.id))
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
  public void findExpensiveProductsCursor() {
    for (ExpensiveProductVO p : this.productsManager.getExpensiveProducts()) {
      System.out.println(" - local: " + p);
    }
  }

  @Transactional
  public void find2ExpensiveProductsCursor() {
    ProductTable p = ProductDAO.newTable("p");
    Cursor<Row> c = sql.select().from(p).where(p.name.like("A%")).executeCursor();
    for (Map<String, Object> row: c) {
      System.out.println(row);
    }

  }

  @Transactional
  public void selectChildrenFKCursor() {
    System.out.println("* Retrieving children using FK & cursor...");
    long id2 = 3L;
    ProductVO p2 = this.productDAO.selectByPK(id2);
    Cursor<HistoricPriceVO> hprices2 = this.productDAO.selectChildrenHistoricPriceOf(p2).fromId()
        .cursorToProductId(HistoricPriceOrderBy.FROM_DATE);
    int total = 0;
    for (HistoricPriceVO h : hprices2) {
      System.out.println("id=" + id2 + " $" + h.getPrice() + " - " + h.getFromDate());
      total++;
    }
    System.out.println("Total of " + total + " historic prices for product " + id2 + ".");

  }

}
