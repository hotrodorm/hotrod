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

import app5.persistence.primitives.AbstractAccountVO;
import app5.persistence.AccountVO;
import app5.persistence.TransactionVO;
import app5.persistence.primitives.TransactionDAO.TransactionOrderBy;
import app5.persistence.primitives.TransactionDAO;

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

  @Lazy
  @Autowired
  private TransactionDAO transactionDAO;

  @Autowired
  private LiveSQLDialect liveSQLDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // select by primary key

  public app5.persistence.AccountVO selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    app5.persistence.AccountVO vo = new app5.persistence.AccountVO();
    vo.setId(id);
    return this.sqlSession.selectOne("app5.persistence.primitives.account.selectByPK", vo);
  }

  // select by unique indexes

  public app5.persistence.AccountVO selectByUIName(final java.lang.String name) {
    if (name == null)
      return null;
    app5.persistence.AccountVO vo = new app5.persistence.AccountVO();
    vo.setName(name);
    return this.sqlSession.selectOne("app5.persistence.primitives.account.selectByUIName", vo);
  }

  // select by example

  public List<app5.persistence.AccountVO> selectByExample(final app5.persistence.primitives.AbstractAccountVO example, final AccountOrderBy... orderBies)
      {
    DaoWithOrder<app5.persistence.primitives.AbstractAccountVO, AccountOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("app5.persistence.primitives.account.selectByExample", dwo);
  }

  public Cursor<app5.persistence.AccountVO> selectByExampleCursor(final app5.persistence.primitives.AbstractAccountVO example, final AccountOrderBy... orderBies)
      {
    DaoWithOrder<app5.persistence.primitives.AbstractAccountVO, AccountOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<app5.persistence.AccountVO>(this.sqlSession.selectCursor("app5.persistence.primitives.account.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<app5.persistence.AccountVO> selectByCriteria(final AccountDAO.AccountTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<app5.persistence.AccountVO>(from, this.liveSQLDialect, this.sqlSession,
        predicate, "app5.persistence.primitives.account.selectByCriteria");
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs

  public SelectChildrenTransactionPhase selectChildrenTransactionOf(final AccountVO vo) {
    return new SelectChildrenTransactionPhase(vo);
  }

  public class SelectChildrenTransactionPhase {

    private AccountVO vo;

    SelectChildrenTransactionPhase(final AccountVO vo) {
      this.vo = vo;
    }

    public SelectChildrenTransactionFromIdPhase fromId() {
      return new SelectChildrenTransactionFromIdPhase(this.vo);
    }

  }

  public class SelectChildrenTransactionFromIdPhase {

    private AccountVO vo;

    SelectChildrenTransactionFromIdPhase(final AccountVO vo) {
      this.vo = vo;
    }

    public List<TransactionVO> toAccountId(final TransactionOrderBy... orderBies) {
      TransactionVO example = new TransactionVO();
      example.setAccountId(this.vo.getId());
      return transactionDAO.selectByExample(example, orderBies);
    }

    public Cursor<TransactionVO> cursorToAccountId(final TransactionOrderBy... orderBies) {
      TransactionVO example = new TransactionVO();
      example.setAccountId(this.vo.getId());
      return transactionDAO.selectByExampleCursor(example, orderBies);
    }

  }

  // insert

  public app5.persistence.AccountVO insert(final app5.persistence.primitives.AbstractAccountVO vo) {
    return insert(vo, false);
  }

  public app5.persistence.AccountVO insert(final app5.persistence.primitives.AbstractAccountVO vo, final boolean retrieveDefaults) {
    String id = retrieveDefaults ? "app5.persistence.primitives.account.insertRetrievingDefaults" : "app5.persistence.primitives.account.insert";
    int rows = this.sqlSession.insert(id, vo);
    app5.persistence.AccountVO mo = new app5.persistence.AccountVO();
    mo.setId(vo.getId());
    mo.setName(vo.getName());
    mo.setType(vo.getType());
    mo.setCurrentBalance(vo.getCurrentBalance());
    mo.setCreatedOn(vo.getCreatedOn());
    mo.setActive(vo.getActive());
    return mo;
  }

  // update by PK

  public int update(final app5.persistence.AccountVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.update("app5.persistence.primitives.account.updateByPK", vo);
  }

  // delete by PK

  public int delete(final app5.persistence.AccountVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.delete("app5.persistence.primitives.account.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final app5.persistence.primitives.AbstractAccountVO example, final app5.persistence.primitives.AbstractAccountVO updateValues) {
    UpdateByExampleDao<app5.persistence.primitives.AbstractAccountVO> fvd = //
      new UpdateByExampleDao<app5.persistence.primitives.AbstractAccountVO>(example, updateValues);
    return this.sqlSession.update("app5.persistence.primitives.account.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final app5.persistence.primitives.AbstractAccountVO example) {
    return this.sqlSession.delete("app5.persistence.primitives.account.deleteByExample", example);
  }

  // DAO ordering

  public enum AccountOrderBy implements OrderBy {

    ID("account", "id", true), //
    ID$DESC("account", "id", false), //
    NAME("account", "name", true), //
    NAME$DESC("account", "name", false), //
    NAME$CASEINSENSITIVE("account", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("account", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("account", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("account", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("account", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("account", "lower(name), name", true), //
    TYPE("account", "type", true), //
    TYPE$DESC("account", "type", false), //
    TYPE$CASEINSENSITIVE("account", "lower(type)", true), //
    TYPE$CASEINSENSITIVE_STABLE_FORWARD("account", "lower(type), type", true), //
    TYPE$CASEINSENSITIVE_STABLE_REVERSE("account", "lower(type), type", false), //
    TYPE$DESC_CASEINSENSITIVE("account", "lower(type)", false), //
    TYPE$DESC_CASEINSENSITIVE_STABLE_FORWARD("account", "lower(type), type", false), //
    TYPE$DESC_CASEINSENSITIVE_STABLE_REVERSE("account", "lower(type), type", true), //
    CURRENT_BALANCE("account", "current_balance", true), //
    CURRENT_BALANCE$DESC("account", "current_balance", false), //
    CREATED_ON("account", "created_on", true), //
    CREATED_ON$DESC("account", "created_on", false), //
    ACTIVE("account", "active", true), //
    ACTIVE$DESC("account", "active", false);

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
    public StringColumn type;
    public NumberColumn currentBalance;
    public DateTimeColumn createdOn;
    public NumberColumn active;

    // Constructors

    AccountTable() {
      super(null, null, "account", "Table", null);
      initialize();
    }

    AccountTable(final String alias) {
      super(null, null, "account", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.name = new StringColumn(this, "name", "name");
      this.type = new StringColumn(this, "type", "type");
      this.currentBalance = new NumberColumn(this, "current_balance", "currentBalance");
      this.createdOn = new DateTimeColumn(this, "created_on", "createdOn");
      this.active = new NumberColumn(this, "active", "active");
    }

  }

}
