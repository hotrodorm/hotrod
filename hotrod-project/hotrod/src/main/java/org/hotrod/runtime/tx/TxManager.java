package org.hotrod.runtime.tx;

import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class TxManager {

  private SqlSessionFactory sqlSessionFactory;

  public TxManager(final SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  private static final ThreadLocal<HotRodSqlSession> HOTROD_SQL_SESSION = //
      new ThreadLocal<HotRodSqlSession>() {

        @Override
        public HotRodSqlSession initialValue() {
          return null;
        }

      };

  private HotRodSqlSession getHotRodSqlSession() throws SQLException {
    return prepare(null);
  }

  private HotRodSqlSession getHotrodSqlSession(final SqlSession sqlSession) throws SQLException {
    return prepare(sqlSession);
  }

  private HotRodSqlSession prepare(final SqlSession sqlSession) throws SQLException {
    try {
      HotRodSqlSession es = HOTROD_SQL_SESSION.get();
      if (es == null) {
        SqlSession newSqlSession = sqlSession != null ? sqlSession : this.sqlSessionFactory.openSession();
        es = new HotRodSqlSession(newSqlSession);
        HOTROD_SQL_SESSION.set(es);
      } else if (sqlSession != null) {
        es.sqlSession = sqlSession;
        es.isTransactionOngoing = true;
      } else if (es.sqlSession == null) {
        es.sqlSession = this.sqlSessionFactory.openSession();
      }
      return es;
    } catch (Exception e) {
      throw new SQLException(e);
    }
  }

  // Transaction control

  public void begin() throws SQLException {
    HotRodSqlSession es = getHotRodSqlSession();
    es.setTransactionOngoing(true);
  }

  public void begin(final SqlSession sqlSession) throws SQLException {
    HotRodSqlSession es = getHotrodSqlSession(sqlSession);
    es.setTransactionOngoing(true);
  }

  public boolean isTransactionOngoing() throws SQLException {
    return getHotRodSqlSession().isTransactionOngoing();
  }

  public void commit() throws SQLException {
    getSqlSession().commit();
    markTransactionAsEnded();
  }

  public SqlSession getSqlSession() throws SQLException {
    return getHotRodSqlSession().getSqlSession();
  }

  public void rollback() throws SQLException {
    getSqlSession().rollback();
    markTransactionAsEnded();
  }

  public void close() throws SQLException {
    try {
      HotRodSqlSession es = getHotRodSqlSession();
      SqlSession sqlSession = es.getSqlSession();
      if (sqlSession != null) {
        sqlSession.close();
        es.sqlSession = null;
      }
      markTransactionAsEnded();
    } catch (Exception e) {
      throw new SQLException(e);
    }
  }

  private static void markTransactionAsEnded() {
    HotRodSqlSession es = HOTROD_SQL_SESSION.get();
    es.setTransactionOngoing(false);
  }

  // Helper class

  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

  public static class HotRodSqlSession {

    private SqlSession sqlSession;
    private boolean isTransactionOngoing;

    public HotRodSqlSession(final SqlSession sqlSession) {
      this.sqlSession = sqlSession;
      this.isTransactionOngoing = false;
    }

    public boolean isTransactionOngoing() {
      return isTransactionOngoing;
    }

    void setTransactionOngoing(boolean isTransactionOngoing) {
      this.isTransactionOngoing = isTransactionOngoing;
    }

    public SqlSession getSqlSession() {
      return sqlSession;
    }

  }

}
