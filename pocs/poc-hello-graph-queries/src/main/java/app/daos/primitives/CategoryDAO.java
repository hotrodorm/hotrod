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

import app.daos.primitives.Category;
import app.daos.CategoryVO;

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
public class CategoryDAO implements Serializable, ApplicationContextAware {

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

  public app.daos.CategoryVO parseRow(Map<String, Object> m) {
    return parseRow(m, null, null);
  }

  public app.daos.CategoryVO parseRow(Map<String, Object> m, String prefix) {
    return parseRow(m, prefix, null);
  }

  public app.daos.CategoryVO parseRow(Map<String, Object> m, String prefix, String suffix) {
    app.daos.CategoryVO mo = this.applicationContext.getBean(app.daos.CategoryVO.class);
    String p = prefix == null ? "": prefix;
    String s = suffix == null ? "": suffix;
    mo.setId(CastUtil.toInteger((Number) m.get(p + "id" + s)));
    mo.setName((java.lang.String) m.get(p + "name" + s));
    return mo;
  }

  // select by primary key

  public app.daos.CategoryVO select(final java.lang.Integer id) {
    if (id == null)
      return null;
    app.daos.CategoryVO vo = new app.daos.CategoryVO();
    vo.setId(id);
    return this.sqlSession.selectOne("mappers.category.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<app.daos.CategoryVO> select(final app.daos.primitives.Category example, final CategoryOrderBy... orderBies)
      {
    DaoWithOrder<app.daos.primitives.Category, CategoryOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("mappers.category.selectByExample", dwo);
  }

  public Cursor<app.daos.CategoryVO> selectCursor(final app.daos.primitives.Category example, final CategoryOrderBy... orderBies)
      {
    DaoWithOrder<app.daos.primitives.Category, CategoryOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<app.daos.CategoryVO>(this.sqlSession.selectCursor("mappers.category.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<app.daos.CategoryVO> select(final CategoryDAO.CategoryTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<app.daos.CategoryVO>(this.context, "mappers.category.selectByCriteria",
        from, predicate);
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public app.daos.CategoryVO insert(final app.daos.primitives.Category vo) {
    String id = "mappers.category.insert";
    this.sqlSession.insert(id, vo);
    app.daos.CategoryVO mo = springBeanObjectFactory.create(app.daos.CategoryVO.class);
    mo.setId(vo.getId());
    mo.setName(vo.getName());
    return mo;
  }

  // update by PK

  public int update(final app.daos.CategoryVO vo) {
    if (vo.getId() == null) return 0;
    return this.sqlSession.update("mappers.category.updateByPK", vo);
  }

  // delete by PK

  public int delete(final java.lang.Integer id) {
    if (id == null) return 0;
    app.daos.CategoryVO vo = new app.daos.CategoryVO();
    vo.setId(id);
    if (vo.getId() == null) return 0;
    return this.sqlSession.delete("mappers.category.deleteByPK", vo);
  }

  // update by example

  public int update(final app.daos.primitives.Category example, final app.daos.primitives.Category updateValues) {
    UpdateByExampleDao<app.daos.primitives.Category> fvd = //
      new UpdateByExampleDao<app.daos.primitives.Category>(example, updateValues);
    return this.sqlSession.update("mappers.category.updateByExample", fvd);
  }

  // update by criteria

  public UpdateSetCompletePhase update(final app.daos.primitives.Category updateValues, final CategoryDAO.CategoryTable tableOrView, final Predicate predicate) {
    Map<String, Object> values = new HashMap<>();
    if (updateValues.getId() != null) values.put("id", updateValues.getId());
    if (updateValues.getName() != null) values.put("name", updateValues.getName());
    return new UpdateSetCompletePhase(this.context, "mappers.category.updateByCriteria", tableOrView,  predicate, values);
  }


  // delete by example

  public int delete(final app.daos.primitives.Category example) {
    return this.sqlSession.delete("mappers.category.deleteByExample", example);
  }

  // delete by criteria

  public DeleteWherePhase delete(final CategoryDAO.CategoryTable from, final Predicate predicate) {
    return new DeleteWherePhase(this.context, "mappers.category.deleteByCriteria", from, predicate);
  }

  // DAO ordering

  public enum CategoryOrderBy implements OrderBy {

    ID("category", "id", true), //
    ID$DESC("category", "id", false), //
    NAME("category", "name", true), //
    NAME$DESC("category", "name", false), //
    NAME$CASEINSENSITIVE("category", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("category", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("category", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("category", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("category", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("category", "lower(name), name", true);

    private CategoryOrderBy(final String tableName, final String columnName,
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

  public static CategoryTable newTable() {
    return new CategoryTable();
  }

  public static CategoryTable newTable(final String alias) {
    return new CategoryTable(alias);
  }

  public static class CategoryTable extends Table {

    // Properties

    public NumberColumn id;
    public StringColumn name;

    // Getters

    public AllColumns star() {
      return new AllColumns(this.id, this.name);
    }

    // Constructors

    CategoryTable() {
      super(null, null, "CATEGORY", "Table", null);
      initialize();
    }

    CategoryTable(final String alias) {
      super(null, null, "CATEGORY", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      super.columns = new ArrayList<>();
      this.id = new NumberColumn(this, "ID", "id", "INTEGER", 32, 0);
      super.columns.add(this.id);
      this.name = new StringColumn(this, "NAME", "name", "CHARACTER VARYING", 10, 0);
      super.columns.add(this.name);
    }

  }

}
