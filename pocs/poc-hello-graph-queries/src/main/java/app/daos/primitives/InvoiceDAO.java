// Autogenerated by HotRod -- Do not edit.

package app.daos.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import app.daos.primitives.Invoice;
import app.daos.InvoiceVO;

import java.lang.Override;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.spring.SpringBeanObjectFactory;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.util.CastUtil;
import javax.annotation.PostConstruct;
import org.hotrod.runtime.livesql.metadata.Column;
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
public class InvoiceDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Autowired
  private LiveSQLDialect liveSQLDialect;

  @Autowired
  private LiveSQLMapper liveSQLMapper;

  @Autowired
  private SpringBeanObjectFactory springBeanObjectFactory;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    this.sqlSession.getConfiguration().setObjectFactory(this.springBeanObjectFactory);
  }

  private LiveSQLContext context;

  @PostConstruct
  public void initializeContext() {
    this.context = new LiveSQLContext(this.liveSQLDialect, this.sqlSession, this.liveSQLMapper);
  }

  // Row Parser

  public app.daos.InvoiceVO parseRow(Map<String, Object> m) {
    return parseRow(m, null, null);
  }

  public app.daos.InvoiceVO parseRow(Map<String, Object> m, String prefix) {
    return parseRow(m, prefix, null);
  }

  public app.daos.InvoiceVO parseRow(Map<String, Object> m, String prefix, String suffix) {
    app.daos.InvoiceVO mo = this.applicationContext.getBean(app.daos.InvoiceVO.class);
    String p = prefix == null ? "": prefix;
    String s = suffix == null ? "": suffix;
    mo.setId(CastUtil.toInteger((Number) m.get(p + "id" + s)));
    mo.setCustomerId(CastUtil.toInteger((Number) m.get(p + "customerId" + s)));
    mo.setPurchaseDate((java.sql.Date) m.get(p + "purchaseDate" + s));
    mo.setPaid(CastUtil.toInteger((Number) m.get(p + "paid" + s)));
    return mo;
  }

  // select by primary key

  public app.daos.InvoiceVO select(final java.lang.Integer id) {
    if (id == null)
      return null;
    app.daos.InvoiceVO vo = new app.daos.InvoiceVO();
    vo.setId(id);
    return this.sqlSession.selectOne("mappers.invoice.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<app.daos.InvoiceVO> select(final app.daos.primitives.Invoice example, final InvoiceOrderBy... orderBies)
      {
    DaoWithOrder<app.daos.primitives.Invoice, InvoiceOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("mappers.invoice.selectByExample", dwo);
  }

  public Cursor<app.daos.InvoiceVO> selectCursor(final app.daos.primitives.Invoice example, final InvoiceOrderBy... orderBies)
      {
    DaoWithOrder<app.daos.primitives.Invoice, InvoiceOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<app.daos.InvoiceVO>(this.sqlSession.selectCursor("mappers.invoice.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<app.daos.InvoiceVO> select(final InvoiceDAO.InvoiceTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<app.daos.InvoiceVO>(this.context, "mappers.invoice.selectByCriteria",
        from, predicate);
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public app.daos.InvoiceVO insert(final app.daos.primitives.Invoice vo) {
    String id = "mappers.invoice.insert";
    this.sqlSession.insert(id, vo);
    app.daos.InvoiceVO mo = springBeanObjectFactory.create(app.daos.InvoiceVO.class);
    mo.setId(vo.getId());
    mo.setCustomerId(vo.getCustomerId());
    mo.setPurchaseDate(vo.getPurchaseDate());
    mo.setPaid(vo.getPaid());
    return mo;
  }

  // update by PK

  public int update(final app.daos.InvoiceVO vo) {
    if (vo.getId() == null) return 0;
    return this.sqlSession.update("mappers.invoice.updateByPK", vo);
  }

  // delete by PK

  public int delete(final java.lang.Integer id) {
    if (id == null) return 0;
    app.daos.InvoiceVO vo = new app.daos.InvoiceVO();
    vo.setId(id);
    if (vo.getId() == null) return 0;
    return this.sqlSession.delete("mappers.invoice.deleteByPK", vo);
  }

  // update by example

  public int update(final app.daos.primitives.Invoice example, final app.daos.primitives.Invoice updateValues) {
    UpdateByExampleDao<app.daos.primitives.Invoice> fvd = //
      new UpdateByExampleDao<app.daos.primitives.Invoice>(example, updateValues);
    return this.sqlSession.update("mappers.invoice.updateByExample", fvd);
  }

  // update by criteria

  public UpdateSetCompletePhase update(final app.daos.primitives.Invoice updateValues, final InvoiceDAO.InvoiceTable tableOrView, final Predicate predicate) {
    Map<String, Object> values = new HashMap<>();
    if (updateValues.getId() != null) values.put("id", updateValues.getId());
    if (updateValues.getCustomerId() != null) values.put("customer_id", updateValues.getCustomerId());
    if (updateValues.getPurchaseDate() != null) values.put("purchase_date", updateValues.getPurchaseDate());
    if (updateValues.getPaid() != null) values.put("paid", updateValues.getPaid());
    return new UpdateSetCompletePhase(this.context, "mappers.invoice.updateByCriteria", tableOrView,  predicate, values);
  }


  // delete by example

  public int delete(final app.daos.primitives.Invoice example) {
    return this.sqlSession.delete("mappers.invoice.deleteByExample", example);
  }

  // delete by criteria

  public DeleteWherePhase delete(final InvoiceDAO.InvoiceTable from, final Predicate predicate) {
    return new DeleteWherePhase(this.context, "mappers.invoice.deleteByCriteria", from, predicate);
  }

  // DAO ordering

  public enum InvoiceOrderBy implements OrderBy {

    ID("invoice", "id", true), //
    ID$DESC("invoice", "id", false), //
    CUSTOMER_ID("invoice", "customer_id", true), //
    CUSTOMER_ID$DESC("invoice", "customer_id", false), //
    PURCHASE_DATE("invoice", "purchase_date", true), //
    PURCHASE_DATE$DESC("invoice", "purchase_date", false), //
    PAID("invoice", "paid", true), //
    PAID$DESC("invoice", "paid", false);

    private InvoiceOrderBy(final String tableName, final String columnName,
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

  public static InvoiceTable newTable() {
    return new InvoiceTable();
  }

  public static InvoiceTable newTable(final String alias) {
    return new InvoiceTable(alias);
  }

  public static class InvoiceTable extends Table {

    // Properties

    public NumberColumn id;
    public NumberColumn customerId;
    public DateTimeColumn purchaseDate;
    public NumberColumn paid;

    // Getters

    public AllColumns star() {
      return new AllColumns(this.id, this.customerId, this.purchaseDate, this.paid);
    }

    // Constructors

    InvoiceTable() {
      super(null, null, "INVOICE", "Table", null);
      initialize();
    }

    InvoiceTable(final String alias) {
      super(null, null, "INVOICE", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      super.columns = new ArrayList<>();
      this.id = new NumberColumn(this, "ID", "id", "INTEGER", 32, 0);
      super.columns.add(this.id);
      this.customerId = new NumberColumn(this, "CUSTOMER_ID", "customerId", "INTEGER", 32, 0);
      super.columns.add(this.customerId);
      this.purchaseDate = new DateTimeColumn(this, "PURCHASE_DATE", "purchaseDate", "DATE", 10, 0);
      super.columns.add(this.purchaseDate);
      this.paid = new NumberColumn(this, "PAID", "paid", "INTEGER", 32, 0);
      super.columns.add(this.paid);
    }

  }

}