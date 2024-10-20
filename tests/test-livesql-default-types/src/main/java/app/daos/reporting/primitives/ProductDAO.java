// Autogenerated by HotRod -- Do not edit.

package app.daos.reporting.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import app.daos.reporting.primitives.AbstractProductVO;
import app.daos.reporting.ProductVO;

import java.lang.Override;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.spring.SpringBeanObjectFactory;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.TypeSource;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.util.CastUtil;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.queries.typesolver.TypeSolver;
import org.hotrod.runtime.livesql.metadata.NumberColumn;
import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.metadata.DateTimeColumn;
import org.hotrod.runtime.livesql.metadata.BooleanColumn;
import org.hotrod.runtime.livesql.metadata.ByteArrayColumn;
import org.hotrod.runtime.livesql.metadata.ObjectColumn;
import org.hotrod.runtime.livesql.metadata.Table;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.AllColumns;
import org.hotrod.runtime.livesql.queries.select.CriteriaWherePhase;
import org.hotrod.runtime.livesql.queries.DeleteWherePhase;
import org.hotrod.runtime.livesql.queries.UpdateSetCompletePhase;
import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.metadata.View;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Component
public class ProductDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Autowired
  private LiveSQLDialect liveSQLDialect;

  @Autowired
  private LiveSQLMapper liveSQLMapper;

  @Autowired
  private SpringBeanObjectFactory springBeanObjectFactory;

  @Autowired
  private DataSource dataSource;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    this.sqlSession.getConfiguration().setObjectFactory(this.springBeanObjectFactory);
  }

  private LiveSQLContext context;

  @Value("${use.plain.jdbc:false}")
  private boolean usePlainJDBC;

  @PostConstruct
  public void initializeContext() {
    this.context = new LiveSQLContext(this.liveSQLDialect, this.sqlSession, this.liveSQLMapper, this.usePlainJDBC, this.dataSource, new TypeSolver(null, this.liveSQLDialect));
  }

  // Row Parser

  public app.daos.reporting.ProductVO parseRow(Map<String, Object> m) {
    return parseRow(m, null, null);
  }

  public app.daos.reporting.ProductVO parseRow(Map<String, Object> m, String prefix) {
    return parseRow(m, prefix, null);
  }

  public app.daos.reporting.ProductVO parseRow(Map<String, Object> m, String prefix, String suffix) {
    app.daos.reporting.ProductVO mo = this.applicationContext.getBean(app.daos.reporting.ProductVO.class);
    String p = prefix == null ? "": prefix;
    String s = suffix == null ? "": suffix;
    mo.setId(CastUtil.toInteger((Number) m.get(p + "id" + s)));
    mo.setType((java.lang.String) m.get(p + "type" + s));
    mo.setShipping(CastUtil.toInteger((Number) m.get(p + "shipping" + s)));
    return mo;
  }

  // no select by PK generated, since the table does not have a PK.

  // select by unique indexes: no unique indexes found -- skipped

  // select by example

  public List<app.daos.reporting.ProductVO> select(final app.daos.reporting.primitives.AbstractProductVO example, final ProductOrderBy... orderBies)
      {
    DaoWithOrder<app.daos.reporting.primitives.AbstractProductVO, ProductOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("mappers.reporting.product.selectByExample", dwo);
  }

  public Cursor<app.daos.reporting.ProductVO> selectCursor(final app.daos.reporting.primitives.AbstractProductVO example, final ProductOrderBy... orderBies)
      {
    DaoWithOrder<app.daos.reporting.primitives.AbstractProductVO, ProductOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<app.daos.reporting.ProductVO>(this.sqlSession.selectCursor("mappers.reporting.product.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<app.daos.reporting.ProductVO> select(final ProductDAO.ProductTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<app.daos.reporting.ProductVO>(this.context, "mappers.reporting.product.selectByCriteria",
        from, predicate);
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public app.daos.reporting.ProductVO insert(final app.daos.reporting.primitives.AbstractProductVO vo) {
    String id = "mappers.reporting.product.insert";
    this.sqlSession.insert(id, vo);
    app.daos.reporting.ProductVO mo = springBeanObjectFactory.create(app.daos.reporting.ProductVO.class);
    mo.setId(vo.getId());
    mo.setType(vo.getType());
    mo.setShipping(vo.getShipping());
    return mo;
  }

  // no update by PK generated, since the table does not have a PK.

  // no delete by PK generated, since the table does not have a PK.

  // update by example

  public int update(final app.daos.reporting.primitives.AbstractProductVO example, final app.daos.reporting.primitives.AbstractProductVO updateValues) {
    UpdateByExampleDao<app.daos.reporting.primitives.AbstractProductVO> fvd = //
      new UpdateByExampleDao<app.daos.reporting.primitives.AbstractProductVO>(example, updateValues);
    return this.sqlSession.update("mappers.reporting.product.updateByExample", fvd);
  }

  // update by criteria

  public UpdateSetCompletePhase update(final app.daos.reporting.primitives.AbstractProductVO updateValues, final ProductDAO.ProductTable tableOrView, final Predicate predicate) {
    Map<String, Object> values = new HashMap<>();
    if (updateValues.getId() != null) values.put("\"ID\"", updateValues.getId());
    if (updateValues.getType() != null) values.put("\"TYPE\"", updateValues.getType());
    if (updateValues.getShipping() != null) values.put("\"SHIPPING\"", updateValues.getShipping());
    return new UpdateSetCompletePhase(this.context, "mappers.reporting.product.updateByCriteria", tableOrView,  predicate, values);
  }


  // delete by example

  public int delete(final app.daos.reporting.primitives.AbstractProductVO example) {
    return this.sqlSession.delete("mappers.reporting.product.deleteByExample", example);
  }

  // delete by criteria

  public DeleteWherePhase delete(final ProductDAO.ProductTable from, final Predicate predicate) {
    return new DeleteWherePhase(this.context, "mappers.reporting.product.deleteByCriteria", from, predicate);
  }

  // DAO ordering

  public enum ProductOrderBy implements OrderBy {

    ID("product", "\"ID\"", true), //
    ID$DESC("product", "\"ID\"", false), //
    TYPE("product", "\"TYPE\"", true), //
    TYPE$DESC("product", "\"TYPE\"", false), //
    TYPE$CASEINSENSITIVE("product", "lower(\"TYPE\")", true), //
    TYPE$CASEINSENSITIVE_STABLE_FORWARD("product", "lower(\"TYPE\"), \"TYPE\"", true), //
    TYPE$CASEINSENSITIVE_STABLE_REVERSE("product", "lower(\"TYPE\"), \"TYPE\"", false), //
    TYPE$DESC_CASEINSENSITIVE("product", "lower(\"TYPE\")", false), //
    TYPE$DESC_CASEINSENSITIVE_STABLE_FORWARD("product", "lower(\"TYPE\"), \"TYPE\"", false), //
    TYPE$DESC_CASEINSENSITIVE_STABLE_REVERSE("product", "lower(\"TYPE\"), \"TYPE\"", true), //
    SHIPPING("product", "\"SHIPPING\"", true), //
    SHIPPING$DESC("product", "\"SHIPPING\"", false);

    private ProductOrderBy(final String tableName, final String columnName,
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

  public static ProductTable newTable() {
    return new ProductTable();
  }

  public static ProductTable newTable(final String alias) {
    return new ProductTable(alias);
  }

  public static class ProductTable extends Table {

    // Properties

    public final NumberColumn id = new NumberColumn(this, "ID", "id", "INTEGER", 32, 0, org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.of(java.lang.Integer.class, TypeSource.ENTITY_COLUMN));
    public final StringColumn type = new StringColumn(this, "TYPE", "type", "CHARACTER VARYING", 6, 0, org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.of(java.lang.String.class, TypeSource.ENTITY_COLUMN));
    public final NumberColumn shipping = new NumberColumn(this, "SHIPPING", "shipping", "INTEGER", 32, 0, org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.of(java.lang.Integer.class, TypeSource.ENTITY_COLUMN));

    // Getters

    public AllColumns star() {
      return new AllColumns(this.id, this.type, this.shipping);
    }

    // Constructors

    ProductTable() {
      super(null, null, Name.of("PRODUCT", false), "Table", null);
      initializeColumns();
    }

    ProductTable(final String alias) {
      super(null, null, Name.of("PRODUCT", false), "Table", alias);
      initializeColumns();
    }

    // Initialization

    private void initializeColumns() {
      super.add(this.id);
      super.add(this.type);
      super.add(this.shipping);
    }

  }

}
