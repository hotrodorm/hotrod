package app5.cursors;

import org.hotrod.runtime.cursors.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app5.persistence.CheapProductVOVO;
import app5.persistence.ProductVO;
import app5.persistence.primitives.MyQueriesDAO;
import app5.persistence.primitives.ProductDAO;
import app5.persistence.primitives.ProductDAO.ProductTable;

@Component
public class ProductsManager {

  @Autowired
  private ProductDAO productDAO;

  @Autowired
  private MyQueriesDAO myQueriesDAO;

  // To make the cursor crash, force the end of the transaction using:
  // @Transactional(propagation=Propagation.REQUIRES_NEW)
  @Transactional
  public Cursor<ProductVO> getProducts() {
    return this.productDAO.selectByExampleCursor(new ProductVO());
  }

  // To make the cursor crash, force the end of the transaction using:
  // @Transactional(propagation=Propagation.REQUIRES_NEW)
  @Transactional
  public Cursor<ProductVO> getProductsCriteria() {
    ProductTable p = ProductDAO.newTable();
    return this.productDAO.selectByCriteria(p, p.price.ge(0)).executeCursor();
  }

  @Transactional
  public Cursor<CheapProductVOVO> getCheapProducts() {
    this.myQueriesDAO.findCheapestProducts();
    return this.myQueriesDAO.findCheapProducts(20);
  }

}
