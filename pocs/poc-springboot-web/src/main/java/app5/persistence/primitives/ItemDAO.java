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

import app5.persistence.primitives.AbstractItemVO;
import app5.persistence.ItemVO;

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
public class ItemDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Autowired
  private LiveSQLDialect liveSQLDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // no select by PK generated, since the table does not have a PK.

  // select by unique indexes: no unique indexes found -- skipped

  // select by example

  public List<app5.persistence.ItemVO> selectByExample(final app5.persistence.primitives.AbstractItemVO example, final ItemOrderBy... orderBies)
      {
    DaoWithOrder<app5.persistence.primitives.AbstractItemVO, ItemOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("app5.persistence.primitives.item.selectByExample", dwo);
  }

  public Cursor<app5.persistence.ItemVO> selectByExampleCursor(final app5.persistence.primitives.AbstractItemVO example, final ItemOrderBy... orderBies)
      {
    DaoWithOrder<app5.persistence.primitives.AbstractItemVO, ItemOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<app5.persistence.ItemVO>(this.sqlSession.selectCursor("app5.persistence.primitives.item.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<app5.persistence.ItemVO> selectByCriteria(final ItemDAO.ItemTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<app5.persistence.ItemVO>(from, this.liveSQLDialect, this.sqlSession,
        predicate, "app5.persistence.primitives.item.selectByCriteria");
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public app5.persistence.ItemVO insert(final app5.persistence.primitives.AbstractItemVO vo) {
    String id = "app5.persistence.primitives.item.insert";
    this.sqlSession.insert(id, vo);
    app5.persistence.ItemVO mo = new app5.persistence.ItemVO();
    mo.setId(vo.getId());
    mo.setDescription(vo.getDescription());
    mo.setPrice(vo.getPrice());
    mo.setCreatedOn(vo.getCreatedOn());
    mo.setActive(vo.getActive());
    mo.setIcon(vo.getIcon());
    mo.setStoreCode(vo.getStoreCode());
    return mo;
  }

  // no update by PK generated, since the table does not have a PK.

  // no delete by PK generated, since the table does not have a PK.

  // update by example

  public int updateByExample(final app5.persistence.primitives.AbstractItemVO example, final app5.persistence.primitives.AbstractItemVO updateValues) {
    UpdateByExampleDao<app5.persistence.primitives.AbstractItemVO> fvd = //
      new UpdateByExampleDao<app5.persistence.primitives.AbstractItemVO>(example, updateValues);
    return this.sqlSession.update("app5.persistence.primitives.item.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final app5.persistence.primitives.AbstractItemVO example) {
    return this.sqlSession.delete("app5.persistence.primitives.item.deleteByExample", example);
  }

  // DAO ordering

  public enum ItemOrderBy implements OrderBy {

    ID("item", "id", true), //
    ID$DESC("item", "id", false), //
    DESCRIPTION("item", "description", true), //
    DESCRIPTION$DESC("item", "description", false), //
    DESCRIPTION$CASEINSENSITIVE("item", "lower(description)", true), //
    DESCRIPTION$CASEINSENSITIVE_STABLE_FORWARD("item", "lower(description), description", true), //
    DESCRIPTION$CASEINSENSITIVE_STABLE_REVERSE("item", "lower(description), description", false), //
    DESCRIPTION$DESC_CASEINSENSITIVE("item", "lower(description)", false), //
    DESCRIPTION$DESC_CASEINSENSITIVE_STABLE_FORWARD("item", "lower(description), description", false), //
    DESCRIPTION$DESC_CASEINSENSITIVE_STABLE_REVERSE("item", "lower(description), description", true), //
    PRICE("item", "price", true), //
    PRICE$DESC("item", "price", false), //
    CREATED_ON("item", "created_on", true), //
    CREATED_ON$DESC("item", "created_on", false), //
    ACTIVE("item", "active", true), //
    ACTIVE$DESC("item", "active", false), //
    ICON("item", "icon", true), //
    ICON$DESC("item", "icon", false), //
    STORE_CODE("item", "store_code", true), //
    STORE_CODE$DESC("item", "store_code", false);

    private ItemOrderBy(final String tableName, final String columnName,
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

  public static ItemTable newTable() {
    return new ItemTable();
  }

  public static ItemTable newTable(final String alias) {
    return new ItemTable(alias);
  }

  public static class ItemTable extends Table {

    // Properties

    public NumberColumn id;
    public StringColumn description;
    public NumberColumn price;
    public DateTimeColumn createdOn;
    public BooleanColumn active;
    public ByteArrayColumn icon;
    public ObjectColumn storeCode;

    // Constructors

    ItemTable() {
      super(null, null, "item", "Table", null);
      initialize();
    }

    ItemTable(final String alias) {
      super(null, null, "item", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.description = new StringColumn(this, "description", "description");
      this.price = new NumberColumn(this, "price", "price");
      this.createdOn = new DateTimeColumn(this, "created_on", "createdOn");
      this.active = new BooleanColumn(this, "active", "active");
      this.icon = new ByteArrayColumn(this, "icon", "icon");
      this.storeCode = new ObjectColumn(this, "store_code", "storeCode");
    }

  }

}
