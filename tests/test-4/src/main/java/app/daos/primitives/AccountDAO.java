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

import app.daos.primitives.AbstractAccountVO;
import app.daos.AccountVO;

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
public class AccountDAO implements Serializable, ApplicationContextAware {

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

  // select by primary key

  public app.daos.AccountVO selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    app.daos.AccountVO vo = new app.daos.AccountVO();
    vo.setId(id);
    return this.sqlSession.selectOne("app.daos.primitives.account.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<app.daos.AccountVO> selectByExample(final app.daos.primitives.AbstractAccountVO example, final AccountOrderBy... orderBies)
      {
    DaoWithOrder<app.daos.primitives.AbstractAccountVO, AccountOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("app.daos.primitives.account.selectByExample", dwo);
  }

  public Cursor<app.daos.AccountVO> selectByExampleCursor(final app.daos.primitives.AbstractAccountVO example, final AccountOrderBy... orderBies)
      {
    DaoWithOrder<app.daos.primitives.AbstractAccountVO, AccountOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<app.daos.AccountVO>(this.sqlSession.selectCursor("app.daos.primitives.account.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<app.daos.AccountVO> selectByCriteria(final AccountDAO.AccountTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<app.daos.AccountVO>(from, this.liveSQLDialect, this.sqlSession,
        predicate, "app.daos.primitives.account.selectByCriteria");
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public app.daos.AccountVO insert(final app.daos.primitives.AbstractAccountVO vo) {
    String id = "app.daos.primitives.account.insert";
    this.sqlSession.insert(id, vo);
    app.daos.AccountVO mo = new app.daos.AccountVO();
    mo.setId(vo.getId());
    mo.setName(vo.getName());
    return mo;
  }

  // update by PK

  public int update(final app.daos.AccountVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.update("app.daos.primitives.account.updateByPK", vo);
  }

  // delete by PK

  public int delete(final app.daos.AccountVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.delete("app.daos.primitives.account.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final app.daos.primitives.AbstractAccountVO example, final app.daos.primitives.AbstractAccountVO updateValues) {
    UpdateByExampleDao<app.daos.primitives.AbstractAccountVO> fvd = //
      new UpdateByExampleDao<app.daos.primitives.AbstractAccountVO>(example, updateValues);
    return this.sqlSession.update("app.daos.primitives.account.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final app.daos.primitives.AbstractAccountVO example) {
    return this.sqlSession.delete("app.daos.primitives.account.deleteByExample", example);
  }

  // DAO ordering

  public enum AccountOrderBy implements OrderBy {

    ID("schema2.account", "id", true), //
    ID$DESC("schema2.account", "id", false), //
    NAME("schema2.account", "name", true), //
    NAME$DESC("schema2.account", "name", false), //
    NAME$CASEINSENSITIVE("schema2.account", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("schema2.account", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("schema2.account", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("schema2.account", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("schema2.account", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("schema2.account", "lower(name), name", true);

    private AccountOrderBy(final String tableName, final String columnName,
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

  public static AccountTable newTable() {
    return new AccountTable();
  }

  public static AccountTable newTable(final String alias) {
    return new AccountTable(alias);
  }

  public static class AccountTable extends Table {

    // Properties

    public NumberColumn id;
    public StringColumn name;

    // Constructors

    AccountTable() {
      super(null, "SCHEMA2", "ACCOUNT", "Table", null);
      initialize();
    }

    AccountTable(final String alias) {
      super(null, "SCHEMA2", "ACCOUNT", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "ID", "id");
      this.name = new StringColumn(this, "NAME", "name");
    }

  }

}