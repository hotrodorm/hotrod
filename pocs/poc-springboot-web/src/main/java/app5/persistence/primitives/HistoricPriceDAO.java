// Autogenerated by HotRod -- Do not edit.

package app5.persistence.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import app5.persistence.primitives.AbstractHistoricPriceVO;
import app5.persistence.HistoricPriceVO;
import app5.persistence.ProductVO;
import app5.persistence.primitives.ProductDAO;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Component
public class HistoricPriceDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Lazy
  @Autowired
  private ProductDAO productDAO;

  @Autowired
  private LiveSQLDialect liveSQLDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // select by primary key

  public app5.persistence.HistoricPriceVO selectByPK(final java.lang.Integer productId, final java.sql.Date fromDate) {
    if (productId == null)
      return null;
    if (fromDate == null)
      return null;
    app5.persistence.HistoricPriceVO vo = new app5.persistence.HistoricPriceVO();
    vo.setProductId(productId);
    vo.setFromDate(fromDate);
    return this.sqlSession.selectOne("app5.persistence.primitives.historicPrice.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<app5.persistence.HistoricPriceVO> selectByExample(final app5.persistence.primitives.AbstractHistoricPriceVO example, final HistoricPriceOrderBy... orderBies)
      {
    DaoWithOrder<app5.persistence.primitives.AbstractHistoricPriceVO, HistoricPriceOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("app5.persistence.primitives.historicPrice.selectByExample", dwo);
  }

  public Cursor<app5.persistence.HistoricPriceVO> selectByExampleCursor(final app5.persistence.primitives.AbstractHistoricPriceVO example, final HistoricPriceOrderBy... orderBies)
      {
    DaoWithOrder<app5.persistence.primitives.AbstractHistoricPriceVO, HistoricPriceOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<app5.persistence.HistoricPriceVO>(this.sqlSession.selectCursor("app5.persistence.primitives.historicPrice.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<app5.persistence.HistoricPriceVO> selectByCriteria(final HistoricPriceDAO.HistoricPriceTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<app5.persistence.HistoricPriceVO>(from, this.liveSQLDialect, this.sqlSession,
        predicate, "app5.persistence.primitives.historicPrice.selectByCriteria");
  }

  // select parent(s) by FKs

  public SelectParentProductPhase selectParentProductOf(final HistoricPriceVO vo) {
    return new SelectParentProductPhase(vo);
  }

  public class SelectParentProductPhase {

    private HistoricPriceVO vo;

    SelectParentProductPhase(final HistoricPriceVO vo) {
      this.vo = vo;
    }

    public SelectParentProductFromProductIdPhase fromProductId() {
      return new SelectParentProductFromProductIdPhase(this.vo);
    }

    public SelectParentProductFromSkuPhase fromSku() {
      return new SelectParentProductFromSkuPhase(this.vo);
    }

  }

  public class SelectParentProductFromProductIdPhase {

    private HistoricPriceVO vo;

    SelectParentProductFromProductIdPhase(final HistoricPriceVO vo) {
      this.vo = vo;
    }

    public ProductVO toId() {
      return productDAO.selectByPK((this.vo.productId == null) ? null : Long.valueOf(this.vo.productId.longValue()));
    }

  }

  public class SelectParentProductFromSkuPhase {

    private HistoricPriceVO vo;

    SelectParentProductFromSkuPhase(final HistoricPriceVO vo) {
      this.vo = vo;
    }

    public ProductVO toSku() {
      return productDAO.selectByUISku(this.vo.sku);
    }

  }

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public app5.persistence.HistoricPriceVO insert(final app5.persistence.primitives.AbstractHistoricPriceVO vo) {
    String id = "app5.persistence.primitives.historicPrice.insert";
    this.sqlSession.insert(id, vo);
    app5.persistence.HistoricPriceVO mo = new app5.persistence.HistoricPriceVO();
    mo.setProductId(vo.getProductId());
    mo.setFromDate(vo.getFromDate());
    mo.setPrice(vo.getPrice());
    mo.setSku(vo.getSku());
    return mo;
  }

  // update by PK

  public int update(final app5.persistence.HistoricPriceVO vo) {
    if (vo.productId == null) return 0;
    if (vo.fromDate == null) return 0;
    return this.sqlSession.update("app5.persistence.primitives.historicPrice.updateByPK", vo);
  }

  // delete by PK

  public int delete(final app5.persistence.HistoricPriceVO vo) {
    if (vo.productId == null) return 0;
    if (vo.fromDate == null) return 0;
    return this.sqlSession.delete("app5.persistence.primitives.historicPrice.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final app5.persistence.primitives.AbstractHistoricPriceVO example, final app5.persistence.primitives.AbstractHistoricPriceVO updateValues) {
    UpdateByExampleDao<app5.persistence.primitives.AbstractHistoricPriceVO> fvd = //
      new UpdateByExampleDao<app5.persistence.primitives.AbstractHistoricPriceVO>(example, updateValues);
    return this.sqlSession.update("app5.persistence.primitives.historicPrice.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final app5.persistence.primitives.AbstractHistoricPriceVO example) {
    return this.sqlSession.delete("app5.persistence.primitives.historicPrice.deleteByExample", example);
  }

  // DAO ordering

  public enum HistoricPriceOrderBy implements OrderBy {

    PRODUCT_ID("historic_price", "product_id", true), //
    PRODUCT_ID$DESC("historic_price", "product_id", false), //
    FROM_DATE("historic_price", "from_date", true), //
    FROM_DATE$DESC("historic_price", "from_date", false), //
    PRICE("historic_price", "price", true), //
    PRICE$DESC("historic_price", "price", false), //
    SKU("historic_price", "sku", true), //
    SKU$DESC("historic_price", "sku", false);

    private HistoricPriceOrderBy(final String tableName, final String columnName,
        boolean ascending) {
      this.tableName = tableName;
      this.columnName = columnName;
      this.ascending = ascending;
    }

    private String tableName;
    private String columnName;
    private boolean ascending;

    public String getTableName() {
      return this.tableName;
    }

    public String getColumnName() {
      return this.columnName;
    }

    public boolean isAscending() {
      return this.ascending;
    }

  }

  // Database Table metadata

  public static HistoricPriceTable newTable() {
    return new HistoricPriceTable();
  }

  public static HistoricPriceTable newTable(final String alias) {
    return new HistoricPriceTable(alias);
  }

  public static class HistoricPriceTable extends Table {

    // Properties

    public NumberColumn productId;
    public DateTimeColumn fromDate;
    public NumberColumn price;
    public NumberColumn sku;

    // Constructors

    HistoricPriceTable() {
      super(null, null, "historic_price", "Table", null);
      initialize();
    }

    HistoricPriceTable(final String alias) {
      super(null, null, "historic_price", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.productId = new NumberColumn(this, "product_id", "productId");
      this.fromDate = new DateTimeColumn(this, "from_date", "fromDate");
      this.price = new NumberColumn(this, "price", "price");
      this.sku = new NumberColumn(this, "sku", "sku");
    }

  }

}
