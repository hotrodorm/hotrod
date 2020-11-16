package app5.cursors;

import java.io.IOException;
import java.util.Iterator;

import org.hotrod.runtime.cursors.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app5.persistence.CheapProductVO;
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
  public Cursor<ProductInterface> getProductsCriteria() {
    ProductTable p = ProductDAO.newTable();
    Cursor<ProductVO> productsCursor = this.productDAO.selectByCriteria(p, p.price.ge(0)).executeCursor();
    Cursor<ProductInterface> cpi = new CursorOverInterface<ProductInterface, ProductVO>(productsCursor);
    return cpi;
  }

  public class CursorOverInterface<I, C extends I> implements Cursor<I> {

    private Cursor<C> cursor;

    public CursorOverInterface(final Cursor<C> cursor) {
      this.cursor = cursor;
    }

    @Override
    public void close() throws IOException {
      this.cursor.close();
    }

    @Override
    public Iterator<I> iterator() {
      return new CursorIterator<I, C>(this.cursor.iterator());
    }

  }

  public class CursorIterator<I, C extends I> implements Iterator<I> {

    private Iterator<C> it;

    public CursorIterator(final Iterator<C> it) {
      this.it = it;
    }

    @Override
    public boolean hasNext() {
      return this.it.hasNext();
    }

    @Override
    public I next() {
      return this.it.next();
    }

  }

//  public class CursorIterator<T> implements Iterator<T> {
//
//    private Iterator<T> it;
//
//    public CursorIterator(final Iterator<T> it) {
//      this.it = it;
//    }
//
//    @Override
//    public boolean hasNext() {
//      return this.it.hasNext();
//    }
//
//    @Override
//    public T next() {
//      return this.it.next();
//    }
//
//  }

  @Transactional
  public Cursor<CheapProductVO> getCheapProducts() {
    this.myQueriesDAO.findCheapestProducts();
    return this.myQueriesDAO.findExpensiveProducts(20, "Lease");
  }

}
