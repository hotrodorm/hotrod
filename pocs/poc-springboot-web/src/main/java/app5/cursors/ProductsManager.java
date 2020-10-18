package app5.cursors;

import org.hotrod.runtime.cursors.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app5.persistence.ProductVO;
import app5.persistence.primitives.ProductDAO;

@Component
public class ProductsManager {

  @Autowired
  private ProductDAO productDAO;

//  @Transactional(propagation=Propagation.REQUIRES_NEW)
  @Transactional
  public Cursor<ProductVO> getExpensiveProducts() {
    return this.productDAO.selectByExampleCursor(new ProductVO());
  }

}
