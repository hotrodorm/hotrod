// Autogenerated by HotRod -- Do not edit.

package com.myapp.daos.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import com.myapp.daos.EmployeeImpl;

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
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Component
public class EmployeeDAO implements Serializable, ApplicationContextAware {

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

  // select by primary key

  public com.myapp.daos.EmployeeImpl selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    com.myapp.daos.EmployeeImpl vo = new com.myapp.daos.EmployeeImpl();
    vo.setId(id);
    return this.sqlSession.selectOne("com.myapp.daos.primitives.employee.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<com.myapp.daos.EmployeeImpl> selectByExample(final com.myapp.daos.EmployeeImpl example, final EmployeeOrderBy... orderBies)
      {
    DaoWithOrder<com.myapp.daos.EmployeeImpl, EmployeeOrderBy> dwo = //
        new DaoWithOrder<com.myapp.daos.EmployeeImpl, EmployeeOrderBy>(example, orderBies);
    return this.sqlSession.selectList("com.myapp.daos.primitives.employee.selectByExample", dwo);
  }

  public Cursor<com.myapp.daos.EmployeeImpl> selectByExampleCursor(final com.myapp.daos.EmployeeImpl example, final EmployeeOrderBy... orderBies)
      {
    DaoWithOrder<com.myapp.daos.EmployeeImpl, EmployeeOrderBy> dwo = //
        new DaoWithOrder<com.myapp.daos.EmployeeImpl, EmployeeOrderBy>(example, orderBies);
    return new MyBatisCursor<com.myapp.daos.EmployeeImpl>(this.sqlSession.selectCursor("com.myapp.daos.primitives.employee.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<com.myapp.daos.EmployeeImpl> selectByCriteria(final EmployeeDAO.EmployeeTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<com.myapp.daos.EmployeeImpl>(from, this.sqlDialect, this.sqlSession,
        predicate, "com.myapp.daos.primitives.employee.selectByCriteria");
  }

  // insert

  public int insert(final com.myapp.daos.EmployeeImpl vo) {
    String id = "com.myapp.daos.primitives.employee.insert";
    return this.sqlSession.insert(id, vo);
  }

  // update by PK

  public int update(final com.myapp.daos.EmployeeImpl vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.update("com.myapp.daos.primitives.employee.updateByPK", vo);
  }

  // delete by PK

  public int delete(final com.myapp.daos.EmployeeImpl vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.delete("com.myapp.daos.primitives.employee.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final com.myapp.daos.EmployeeImpl example, final com.myapp.daos.EmployeeImpl updateValues) {
    UpdateByExampleDao<com.myapp.daos.EmployeeImpl> fvd = //
      new UpdateByExampleDao<com.myapp.daos.EmployeeImpl>(example, updateValues);
    return this.sqlSession.update("com.myapp.daos.primitives.employee.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final com.myapp.daos.EmployeeImpl example) {
    return this.sqlSession.delete("com.myapp.daos.primitives.employee.deleteByExample", example);
  }

  // DAO ordering

  public enum EmployeeOrderBy implements OrderBy {

    ID("employee", "id", true), //
    ID$DESC("employee", "id", false), //
    NAME("employee", "name", true), //
    NAME$DESC("employee", "name", false), //
    NAME$CASEINSENSITIVE("employee", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("employee", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("employee", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("employee", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("employee", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("employee", "lower(name), name", true);

    private EmployeeOrderBy(final String tableName, final String columnName,
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

  public static EmployeeTable newTable() {
    return new EmployeeTable();
  }

  public static EmployeeTable newTable(final String alias) {
    return new EmployeeTable(alias);
  }

  public static class EmployeeTable extends Table {

    // Properties

    public NumberColumn id;
    public StringColumn name;

    // Constructors

    EmployeeTable() {
      super(null, null, "employee", "Table", null);
      initialize();
    }

    EmployeeTable(final String alias) {
      super(null, null, "employee", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.name = new StringColumn(this, "name", "name");
    }

  }

}