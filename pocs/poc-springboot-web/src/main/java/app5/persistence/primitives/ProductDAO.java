// Autogenerated by HotRod -- Do not edit.

package app5.persistence.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.OrderBy;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.NumberColumn;
import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.metadata.Table;
import org.hotrod.runtime.livesql.queries.select.CriteriaWherePhase;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("productDAO")
public class ProductDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Value("#{sqlDialectFactory.sqlDialect}")
  private SQLDialect sqlDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // select by primary key

  public app5.persistence.ProductVO selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    app5.persistence.ProductVO vo = new app5.persistence.ProductVO();
    vo.setId(id);
    return this.sqlSession.selectOne("app5.persistence.primitives.product.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<app5.persistence.ProductVO> selectByExample(final app5.persistence.ProductVO example,
      final ProductOrderBy... orderBies) {
    DaoWithOrder<app5.persistence.ProductVO, ProductOrderBy> dwo = //
        new DaoWithOrder<app5.persistence.ProductVO, ProductOrderBy>(example, orderBies);
    return this.sqlSession.selectList("app5.persistence.primitives.product.selectByExample", dwo);
  }

  public Cursor<app5.persistence.ProductVO> selectByExampleCursor(final app5.persistence.ProductVO example,
      final ProductOrderBy... orderBies) {
    DaoWithOrder<app5.persistence.ProductVO, ProductOrderBy> dwo = //
        new DaoWithOrder<app5.persistence.ProductVO, ProductOrderBy>(example, orderBies);
    return this.sqlSession.selectCursor("app5.persistence.primitives.product.selectByExample", dwo);
  }

  // select by criteria

  public CriteriaWherePhase<app5.persistence.ProductVO> selectByCriteria(final ProductDAO.ProductTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<app5.persistence.ProductVO>(from, this.sqlDialect, this.sqlSession, predicate,
        "app5.persistence.primitives.product.selectByCriteria");
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public int insert(final app5.persistence.ProductVO vo) {
    String id = "app5.persistence.primitives.product.insert";
    return this.sqlSession.insert(id, vo);
  }

  // update by PK

  public int update(final app5.persistence.ProductVO vo) {
    if (vo.id == null)
      return 0;
    return this.sqlSession.update("app5.persistence.primitives.product.updateByPK", vo);
  }

  // delete by PK

  public int delete(final app5.persistence.ProductVO vo) {
    if (vo.id == null)
      return 0;
    return this.sqlSession.delete("app5.persistence.primitives.product.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final app5.persistence.ProductVO example, final app5.persistence.ProductVO updateValues) {
    UpdateByExampleDao<app5.persistence.ProductVO> fvd = //
        new UpdateByExampleDao<app5.persistence.ProductVO>(example, updateValues);
    return this.sqlSession.update("app5.persistence.primitives.product.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final app5.persistence.ProductVO example) {
    return this.sqlSession.delete("app5.persistence.primitives.product.deleteByExample", example);
  }

  // DAO ordering

  public enum ProductOrderBy implements OrderBy {

    ID("product", "id", true), //
    ID$DESC("product", "id", false), //
    NAME("product", "name", true), //
    NAME$DESC("product", "name", false), //
    NAME$CASEINSENSITIVE("product", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("product", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("product", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("product", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("product", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("product", "lower(name), name", true), //
    PRICE("product", "price", true), //
    PRICE$DESC("product", "price", false);

    private ProductOrderBy(final String tableName, final String columnName, boolean ascending) {
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

  public static ProductTable newTable() {
    return new ProductTable();
  }

  public static ProductTable newTable(final String alias) {
    return new ProductTable(alias);
  }

  public static class ProductTable extends Table {

    // Properties

    public NumberColumn id;
    public StringColumn name;
    public NumberColumn price;

    // Constructors

    ProductTable() {
      super(null, null, "product", "Table", null);
      initialize();
    }

    ProductTable(final String alias) {
      super(null, null, "product", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.name = new StringColumn(this, "name", "name");
      this.price = new NumberColumn(this, "price", "price");
    }

  }

}