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

import app.daos.reporting.primitives.AbstractInvoiceLineVO;
import app.daos.reporting.InvoiceLineVO;

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
public class InvoiceLineDAO implements Serializable, ApplicationContextAware {

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

  public app.daos.reporting.InvoiceLineVO parseRow(Map<String, Object> m) {
    return parseRow(m, null, null);
  }

  public app.daos.reporting.InvoiceLineVO parseRow(Map<String, Object> m, String prefix) {
    return parseRow(m, prefix, null);
  }

  public app.daos.reporting.InvoiceLineVO parseRow(Map<String, Object> m, String prefix, String suffix) {
    app.daos.reporting.InvoiceLineVO mo = this.applicationContext.getBean(app.daos.reporting.InvoiceLineVO.class);
    String p = prefix == null ? "": prefix;
    String s = suffix == null ? "": suffix;
    mo.setInvoiceId(CastUtil.toInteger((Number) m.get(p + "invoiceId" + s)));
    mo.setProductId(CastUtil.toInteger((Number) m.get(p + "productId" + s)));
    mo.setLineTotal(CastUtil.toInteger((Number) m.get(p + "lineTotal" + s)));
    return mo;
  }

  // no select by PK generated, since the table does not have a PK.

  // select by unique indexes: no unique indexes found -- skipped

  // select by example

  public List<app.daos.reporting.InvoiceLineVO> select(final app.daos.reporting.primitives.AbstractInvoiceLineVO example, final InvoiceLineOrderBy... orderBies)
      {
    DaoWithOrder<app.daos.reporting.primitives.AbstractInvoiceLineVO, InvoiceLineOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("mappers.reporting.invoiceLine.selectByExample", dwo);
  }

  public Cursor<app.daos.reporting.InvoiceLineVO> selectCursor(final app.daos.reporting.primitives.AbstractInvoiceLineVO example, final InvoiceLineOrderBy... orderBies)
      {
    DaoWithOrder<app.daos.reporting.primitives.AbstractInvoiceLineVO, InvoiceLineOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<app.daos.reporting.InvoiceLineVO>(this.sqlSession.selectCursor("mappers.reporting.invoiceLine.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<app.daos.reporting.InvoiceLineVO> select(final InvoiceLineDAO.InvoiceLineTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<app.daos.reporting.InvoiceLineVO>(this.context, "mappers.reporting.invoiceLine.selectByCriteria",
        from, predicate);
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public app.daos.reporting.InvoiceLineVO insert(final app.daos.reporting.primitives.AbstractInvoiceLineVO vo) {
    String id = "mappers.reporting.invoiceLine.insert";
    this.sqlSession.insert(id, vo);
    app.daos.reporting.InvoiceLineVO mo = springBeanObjectFactory.create(app.daos.reporting.InvoiceLineVO.class);
    mo.setInvoiceId(vo.getInvoiceId());
    mo.setProductId(vo.getProductId());
    mo.setLineTotal(vo.getLineTotal());
    return mo;
  }

  // no update by PK generated, since the table does not have a PK.

  // no delete by PK generated, since the table does not have a PK.

  // update by example

  public int update(final app.daos.reporting.primitives.AbstractInvoiceLineVO example, final app.daos.reporting.primitives.AbstractInvoiceLineVO updateValues) {
    UpdateByExampleDao<app.daos.reporting.primitives.AbstractInvoiceLineVO> fvd = //
      new UpdateByExampleDao<app.daos.reporting.primitives.AbstractInvoiceLineVO>(example, updateValues);
    return this.sqlSession.update("mappers.reporting.invoiceLine.updateByExample", fvd);
  }

  // update by criteria

  public UpdateSetCompletePhase update(final app.daos.reporting.primitives.AbstractInvoiceLineVO updateValues, final InvoiceLineDAO.InvoiceLineTable tableOrView, final Predicate predicate) {
    Map<String, Object> values = new HashMap<>();
    if (updateValues.getInvoiceId() != null) values.put("\"INVOICE_ID\"", updateValues.getInvoiceId());
    if (updateValues.getProductId() != null) values.put("\"PRODUCT_ID\"", updateValues.getProductId());
    if (updateValues.getLineTotal() != null) values.put("\"LINE_TOTAL\"", updateValues.getLineTotal());
    return new UpdateSetCompletePhase(this.context, "mappers.reporting.invoiceLine.updateByCriteria", tableOrView,  predicate, values);
  }


  // delete by example

  public int delete(final app.daos.reporting.primitives.AbstractInvoiceLineVO example) {
    return this.sqlSession.delete("mappers.reporting.invoiceLine.deleteByExample", example);
  }

  // delete by criteria

  public DeleteWherePhase delete(final InvoiceLineDAO.InvoiceLineTable from, final Predicate predicate) {
    return new DeleteWherePhase(this.context, "mappers.reporting.invoiceLine.deleteByCriteria", from, predicate);
  }

  // DAO ordering

  public enum InvoiceLineOrderBy implements OrderBy {

    INVOICE_ID("invoice_line", "\"INVOICE_ID\"", true), //
    INVOICE_ID$DESC("invoice_line", "\"INVOICE_ID\"", false), //
    PRODUCT_ID("invoice_line", "\"PRODUCT_ID\"", true), //
    PRODUCT_ID$DESC("invoice_line", "\"PRODUCT_ID\"", false), //
    LINE_TOTAL("invoice_line", "\"LINE_TOTAL\"", true), //
    LINE_TOTAL$DESC("invoice_line", "\"LINE_TOTAL\"", false);

    private InvoiceLineOrderBy(final String tableName, final String columnName,
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

  public static InvoiceLineTable newTable() {
    return new InvoiceLineTable();
  }

  public static InvoiceLineTable newTable(final String alias) {
    return new InvoiceLineTable(alias);
  }

  public static class InvoiceLineTable extends Table {

    // Properties

    public final NumberColumn invoiceId = new NumberColumn(this, "INVOICE_ID", "invoiceId", "INTEGER", 32, 0, org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.of(java.lang.Integer.class, TypeSource.ENTITY_COLUMN));
    public final NumberColumn productId = new NumberColumn(this, "PRODUCT_ID", "productId", "INTEGER", 32, 0, org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.of(java.lang.Integer.class, TypeSource.ENTITY_COLUMN));
    public final NumberColumn lineTotal = new NumberColumn(this, "LINE_TOTAL", "lineTotal", "INTEGER", 32, 0, org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.of(java.lang.Integer.class, TypeSource.ENTITY_COLUMN));

    // Getters

    public AllColumns star() {
      return new AllColumns(this.invoiceId, this.productId, this.lineTotal);
    }

    // Constructors

    InvoiceLineTable() {
      super(null, null, Name.of("INVOICE_LINE", false), "Table", null);
      initializeColumns();
    }

    InvoiceLineTable(final String alias) {
      super(null, null, Name.of("INVOICE_LINE", false), "Table", alias);
      initializeColumns();
    }

    // Initialization

    private void initializeColumns() {
      super.add(this.invoiceId);
      super.add(this.productId);
      super.add(this.lineTotal);
    }

  }

}
