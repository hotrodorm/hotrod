// Autogenerated by HotRod -- Do not edit.

package app5.persistence.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.OrderBy;
import org.hotrod.runtime.interfaces.Selectable;


import app5.persistence.CheapProductVOVO;
import app5.persistence.ProductVO;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.metadata.NumberColumn;
import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.metadata.DateTimeColumn;
import org.hotrod.runtime.livesql.metadata.BooleanColumn;
import org.hotrod.runtime.livesql.metadata.ByteArrayColumn;
import org.hotrod.runtime.livesql.metadata.ObjectColumn;
import org.hotrod.runtime.livesql.metadata.Table;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.select.CriteriaWherePhase;
import org.hotrod.runtime.livesql.metadata.View;

import org.springframework.stereotype.Component;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Component
public class MyQueriesDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Autowired
  private SQLDialect sqlDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // select method: findExpensiveProducts

  /*
  * The SQL statement for this method is:


      
      
      select name, price, #{title} as kind
      from product
      where price > #{minPrice}
    

  */


  public static class ParamFindExpensiveProducts {
    java.lang.Integer minPrice;
    java.lang.String title;
  }

  public Cursor<CheapProductVOVO> findExpensiveProducts(final java.lang.Integer minPrice, final java.lang.String title) {
    ParamFindExpensiveProducts param0 = new ParamFindExpensiveProducts();
    param0.minPrice = minPrice;
    param0.title = title;
    return     new MyBatisCursor<CheapProductVOVO>(this.sqlSession.selectCursor("app5.persistence.primitives.myQueriesDAO.select_findExpensiveProducts", param0));
  }

  // select method: findCheapestProducts

  /*
  * The SQL statement for this method is:


      select
        ... structured columns here...
      from product p
      where p.price between 0 and 3
    

  */


  public List<ProductVO> findCheapestProducts() {
    return this.sqlSession.selectList("app5.persistence.primitives.myQueriesDAO.select_findCheapestProducts");
  }

}