// Autogenerated by EmpusaMB. Do not edit.

package com.company.daos.primitives;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.OrderBy;
import org.hotrod.runtime.interfaces.Persistable;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.tx.TxDemarcator;
import org.hotrod.runtime.tx.TxManager;

import com.company.daos.primitives.TransactionDAOPrimitives.TransactionOrderBy;
import com.company.daos.TransactionDAO;
import com.company.daos.AccountDAO;

public class TransactionDAOPrimitives
    implements Persistable<TransactionDAO, TransactionOrderBy>, 
TxDemarcator, Serializable {

  private static final long serialVersionUID = 1L;

  // DAO Properties (table columns)

  protected java.lang.Integer id = null;
  protected java.sql.Date completedAt = null;
  protected java.lang.Integer amount = null;
  protected java.lang.Integer accountId = null;

  // select by primary key

  public static TransactionDAO select(final java.lang.Integer id) throws SQLException {
    TxManager txm = null;
    try {
      txm = getTxManager();
      SqlSession sqlSession = txm.getSqlSession();
      return select(sqlSession, id);
    } finally {
      if (txm != null && !txm.isTransactionOngoing()) {
        txm.close();
      }
    }
  }

  public static TransactionDAO select(final SqlSession sqlSession, final java.lang.Integer id)
      throws SQLException {
    TransactionDAO pk = new TransactionDAO();
    pk.setId(id);
    return sqlSession.selectOne("com.company.daos.primitives.transaction.selectByPK", pk);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example (with ordering)

  @Override
  public List<TransactionDAO> select(final TransactionOrderBy... orderBies)
      throws SQLException {
    TxManager txm = null;
    try {
      txm = getTxManager();
      SqlSession sqlSession = txm.getSqlSession();
      return select(sqlSession, orderBies);
    } finally {
      if (txm != null && !txm.isTransactionOngoing()) {
        txm.close();
      }
    }
  }

  @Override
  public List<TransactionDAO> select(final SqlSession sqlSession, final TransactionOrderBy... orderBies)
      throws SQLException {
    DaoWithOrder<TransactionDAOPrimitives, TransactionOrderBy> dwo = //
    new DaoWithOrder<TransactionDAOPrimitives, TransactionOrderBy>(this, orderBies);
    return sqlSession.selectList("com.company.daos.primitives.transaction.selectByExample", dwo);
  }

  // select parents by imported FKs

  public class AccountParentSelector {

    public AccountDAO byAccountId() throws SQLException {
      TxManager txm = null;
      try {
        txm = getTxManager();
        SqlSession sqlSession = txm.getSqlSession();
        return byAccountId(sqlSession);
      } finally {
        if (txm != null && !txm.isTransactionOngoing()) {
          txm.close();
        }
      }
    }

    public AccountDAO byAccountId(final SqlSession sqlSession) throws SQLException {
      return AccountDAOPrimitives.select(sqlSession, accountId);
    }

  }

  public AccountParentSelector selectParentAccount() {
    return new AccountParentSelector();
  }

  // select children by exported FKs: no exported keys found -- skipped

  // insert

  @Override
  public int insert() throws SQLException {
    TxManager txm = null;
    try {
      txm = getTxManager();
      SqlSession sqlSession = txm.getSqlSession();
      int rows = insert(sqlSession);
      if (!txm.isTransactionOngoing()) {
        txm.commit();
      }
      return rows;
    } finally {
      if (txm != null && !txm.isTransactionOngoing()) {
        txm.close();
      }
    }
  }

  @Override
  public int insert(final SqlSession sqlSession) throws SQLException {
    return sqlSession.insert("com.company.daos.primitives.transaction.insert", this);
  }

  // update by PK

  public int update() throws SQLException {
    TxManager txm = null;
    try {
      txm = getTxManager();
      SqlSession sqlSession = txm.getSqlSession();
      int rows = update(sqlSession);
      if (!txm.isTransactionOngoing()) {
        txm.commit();
      }
      return rows;
    } finally {
      if (txm != null && !txm.isTransactionOngoing()) {
        txm.close();
      }
    }
  }

  public int update(final SqlSession sqlSession) throws SQLException {
    return sqlSession.update("com.company.daos.primitives.transaction.updateByPK", this);
  }

  // update by example

  @Override
  public int updateByExample(final TransactionDAO updateValues) throws SQLException {
    TxManager txm = null;
    try {
      txm = getTxManager();
      SqlSession sqlSession = txm.getSqlSession();
      int rows = updateByExample(sqlSession, updateValues);
      if (!txm.isTransactionOngoing()) {
        txm.commit();
      }
      return rows;
    } finally {
      if (txm != null && !txm.isTransactionOngoing()) {
        txm.close();
      }
    }
  }

  @Override
  public int updateByExample(final SqlSession sqlSession, final TransactionDAO updateValues) throws SQLException {
    UpdateByExampleDao<TransactionDAOPrimitives> fvd = //
      new UpdateByExampleDao<TransactionDAOPrimitives>(this, updateValues);
    return sqlSession.update("com.company.daos.primitives.transaction.updateByExample", fvd);
  }

  // delete by PK

  public int delete() throws SQLException {
    TxManager txm = null;
    try {
      txm = getTxManager();
      SqlSession sqlSession = txm.getSqlSession();
      int rows = delete(sqlSession);
      if (!txm.isTransactionOngoing()) {
        txm.commit();
      }
      return rows;
    } finally {
      if (txm != null && !txm.isTransactionOngoing()) {
        txm.close();
      }
    }
  }

  public int delete(final SqlSession sqlSession) throws SQLException {
    return sqlSession.delete("com.company.daos.primitives.transaction.deleteByPK", this);
  }

  // delete by example

  @Override
  public int deleteByExample() throws SQLException {
    TxManager txm = null;
    try {
      txm = getTxManager();
      SqlSession sqlSession = txm.getSqlSession();
      int rows = deleteByExample(sqlSession);
      if (!txm.isTransactionOngoing()) {
        txm.commit();
      }
      return rows;
    } finally {
      if (txm != null && !txm.isTransactionOngoing()) {
        txm.close();
      }
    }
  }

  @Override
  public int deleteByExample(final SqlSession sqlSession) throws SQLException {
    return sqlSession.delete("com.company.daos.primitives.transaction.deleteByExample", this);
  }

  // getters & setters

  public final java.lang.Integer getId() {
    return this.id;
  }

  public final void setId(final java.lang.Integer id) {
    this.id = id;
  }

  public final java.sql.Date getCompletedAt() {
    return this.completedAt;
  }

  public final void setCompletedAt(final java.sql.Date completedAt) {
    this.completedAt = completedAt;
  }

  public final java.lang.Integer getAmount() {
    return this.amount;
  }

  public final void setAmount(final java.lang.Integer amount) {
    this.amount = amount;
  }

  public final java.lang.Integer getAccountId() {
    return this.accountId;
  }

  public final void setAccountId(final java.lang.Integer accountId) {
    this.accountId = accountId;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append("[");
    sb.append(this.id + ", ");
    sb.append(this.completedAt + ", ");
    sb.append(this.amount + ", ");
    sb.append(this.accountId);
    sb.append("]");
    return sb.toString();
  }

  // DAO ordering

  public enum TransactionOrderBy implements OrderBy {

    ID("TRANSACTION", "ID", true), //
    ID$DESC("TRANSACTION", "ID", false), //
    COMPLETED_AT("TRANSACTION", "COMPLETED_AT", true), //
    COMPLETED_AT$DESC("TRANSACTION", "COMPLETED_AT", false), //
    AMOUNT("TRANSACTION", "AMOUNT", true), //
    AMOUNT$DESC("TRANSACTION", "AMOUNT", false), //
    ACCOUNT_ID("TRANSACTION", "ACCOUNT_ID", true), //
    ACCOUNT_ID$DESC("TRANSACTION", "ACCOUNT_ID", false);

    private TransactionOrderBy(final String tableName, final String columnName,
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

  // Transaction demarcation

  private static TxManager txManager = null;

  public static TxManager getTxManager() throws SQLException {
    if (txManager == null) {
      synchronized (TransactionDAOPrimitives.class) {
        if (txManager == null) {
          txManager = new TxManager(getSqlSessionFactory());
        }
      }
    }
    return txManager;
  }

  public static SqlSessionFactory getSqlSessionFactory() throws SQLException {
    return com.company.sessionfactory.SessionFactory.getInstance().getSqlSessionFactory();
  }

}