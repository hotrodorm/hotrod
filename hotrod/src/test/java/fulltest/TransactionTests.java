package fulltest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.hotrod.runtime.tx.TxDemarcator;
import org.hotrod.runtime.tx.TxManager;

import hotrod.test.generation.AccountDAO;

public class TransactionTests {

  public static void main(final String[] args) throws IOException, SQLException {
    countProperties();
  }

  private static void countProperties() throws SQLException {

    noTransactions();
    // customMapperSqlSession();
    // customMapperTxManager();
    // simpleTx();
    // customTransaction();
    // interlacedTransactions();

  }

  private static void noTransactions() throws SQLException {
    int deleted;
    Random random = new Random();

    // 1. No transaction used (simplest): every delete(), insert() and update()
    // commits right away.

    System.out.println("[ 1. Simple Test with no transaction ]");

    AccountDAO a1 = new AccountDAO();
    a1.setId(24);
    System.out.println("Will delete.");
    deleted = a1.delete();
    System.out.println("Deleted account? " + (deleted > 0 ? "Yes" : "No"));

    a1.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    a1.setCurrentBalance(200 + random.nextInt(100));
    System.out.println("Will insert.");
    a1.insert();
    System.out.println("a1.getId()=" + a1.getId());

    AccountDAO example = new AccountDAO();
    example.setCurrentBalance(252);
    List<AccountDAO> accounts = AccountDAO.selectByExample(example);
    System.out.println("There are now " + accounts.size() + " account(s):");
    for (AccountDAO a : accounts) {
      System.out.println("#" + a.getId() + " $" + a.getCurrentBalance() + " (" + a.getCreatedOn() + ")");
    }

  }

  private static void customMapperSqlSession() throws SQLException {
    // Custom mapper using a SqlSession.

    System.out.println("[ 2. Custom mapper using a SqlSession ]");

    SqlSession sqlSession = null;
    try {
      sqlSession = AccountDAO.getSqlSessionFactory().openSession();

      List<AccountDAO> activeAccounts = sqlSession
          .selectList("hotrod.test.generation.primitives.account.custom.selectActive");
      for (AccountDAO a : activeAccounts) {
        System.out.println("Active account #: " + a.getId() + ", $" + a.getCurrentBalance());
      }

    } finally {
      if (sqlSession != null) {
        sqlSession.close();
      }
    }
  }

  private static void customMapperTxManager() throws SQLException {

    // Custom mapper using a transaction manager.

    System.out.println("[ 3. Custom mapper using a transaction manager ]");

    TxManager txManager = null;
    try {

      txManager = AccountDAO.getTxManager();
      SqlSession sqlSession = txManager.getSqlSession();

      List<AccountDAO> activeAccounts = sqlSession
          .selectList("hotrod.test.generation.primitives.account.custom.selectActive");
      for (AccountDAO a : activeAccounts) {
        System.out.println("Active account #: " + a.getId() + ", $" + a.getCurrentBalance());
      }

    } finally {
      if (txManager != null) {
        txManager.close();
      }
    }
  }

  private static void simpleTx() throws SQLException {

    // 4. Single transaction (simple): all delete(), insert() and update() in
    // between a begin() and commit() are executed inside a transaction.

    System.out.println("[ 4. Single transaction (simple) ]");

    TxManager txManager = null;
    try {

      txManager = AccountDAO.getTxManager();
      txManager.begin();

      AccountDAO a1 = new AccountDAO();
      a1.setCreatedOn(new Timestamp(System.currentTimeMillis()));
      a1.setCurrentBalance(350);
      a1.insert();
      System.out.println("Account #1 created with id=" + a1.getId());

      // if (a1.getId() != null) {
      // throw new NullPointerException();
      // }

      AccountDAO a2 = new AccountDAO();
      a2.setCreatedOn(new Timestamp(System.currentTimeMillis()));
      a2.setCurrentBalance(350);
      a2.insert();
      System.out.println("Account #2 created with id=" + a2.getId());

      txManager.commit();

    } finally {
      if (txManager != null) {
        txManager.close();
      }
    }

  }

  private static void customTransaction() throws SQLException {
    // 5. Single custom transaction: all delete(), insert() and update() in
    // between a begin(), commit() or rollback() are executed inside a
    // transaction. The transaction is created in a custom way.

    System.out.println("[ 5. Single custom transaction ]");

    TxManager txManager = null;
    try {

      txManager = AccountDAO.getTxManager();
      SqlSession sqlSession = txManager.getSqlSessionFactory().openSession(TransactionIsolationLevel.SERIALIZABLE);
      txManager.begin(sqlSession);

      AccountDAO a1 = new AccountDAO();
      a1.setCreatedOn(new Timestamp(System.currentTimeMillis()));
      a1.setCurrentBalance(350);
      a1.insert();
      System.out.println("Account #1 created with id=" + a1.getId());

      // if (a1.getId() != null) {
      // throw new NullPointerException();
      // }

      AccountDAO a2 = new AccountDAO();
      a2.setCreatedOn(new Timestamp(System.currentTimeMillis()));
      a2.setCurrentBalance(350);
      a2.insert();
      System.out.println("Account #2 created with id=" + a2.getId());

      txManager.commit();

    } finally {
      txManager.close();
    }

  }

  private static void interlacedTransactions() throws SQLException {

    // 6. Multiple transactions (and complex combinations): delete(), insert()
    // and update() can participate in separate transactions, using separate
    // SqlSession objects. Each one delimits transactions using begin(),
    // commit() and rollback().

    // This is heavily dependent on the RDBMS and the JDBC driver. Sometimes
    // interlaced transactions are real, sometimes simulated under the hood,
    // sometime not supported at all.

    System.out.println("[ 6. Multiple transactions ]");

    TxManager txManager = null;
    SqlSession sqlSession1 = null;
    SqlSession sqlSession2 = null;

    try {

      txManager = AccountDAO.getTxManager();

      sqlSession1 = txManager.getSqlSessionFactory().openSession(TransactionIsolationLevel.SERIALIZABLE);

      sqlSession2 = txManager.getSqlSessionFactory().openSession(TransactionIsolationLevel.SERIALIZABLE);

      Connection conn = sqlSession1.getConnection();
      System.out.println("We can even use the JDBC connection directly: conn=" + conn);

      AccountDAO a1 = new AccountDAO();
      a1.setCurrentBalance(23);
      a1.setCreatedOn(new Timestamp(System.currentTimeMillis()));
      a1.insert(sqlSession1);
      int account1 = a1.getId();
      System.out.println("Account #1 created with id=" + a1.getId());

      AccountDAO a2 = new AccountDAO();
      a2.setCurrentBalance(67);
      a2.setCreatedOn(new Timestamp(System.currentTimeMillis()));
      a2.insert(sqlSession2);
      System.out.println("Account #2 created with id=" + a2.getId());

      a1.setCurrentBalance(45);
      a1.insert(sqlSession1);
      System.out.println("Account #3 created with id=" + a1.getId());
      int account2 = a1.getId();

      a2.setCurrentBalance(89);
      a2.insert(sqlSession2);
      System.out.println("Account #4 created with id=" + a2.getId());

      sqlSession1.rollback();
      System.out.println(
          "Transaction 1 was rolled back: account " + account1 + " and " + account2 + " were not actually created.");

      sqlSession2.commit();

    } finally {
      try {
        sqlSession1.close();
      } finally {
        sqlSession2.close();
      }
    }
  }

  public static abstract class Demarcator {

    public Demarcator(final TxDemarcator td) throws SQLException {
      TxManager txManager = null;
      try {

        txManager = getTxManager(td);
        SqlSession sqlSession = txManager.getSqlSessionFactory().openSession(TransactionIsolationLevel.SERIALIZABLE);

        statements(sqlSession);

      } finally {
        if (txManager != null) {
          txManager.close();
        }
      }

    }

    public abstract void statements(final SqlSession sqlSession);

    private TxManager getTxManager(final TxDemarcator td) throws SQLException {
      try {
        Method m = td.getClass().getMethod("getTxManager");
        TxManager txManager = (TxManager) m.invoke(null);
        return txManager;
      } catch (SecurityException e) {
        throw new SQLException(e);
      } catch (IllegalArgumentException e) {
        throw new SQLException(e);
      } catch (NoSuchMethodException e) {
        throw new SQLException(e);
      } catch (IllegalAccessException e) {
        throw new SQLException(e);
      } catch (InvocationTargetException e) {
        throw new SQLException(e);
      }
    }

  }

  private static void extraSimpleTx() throws SQLException {

    TxManager txManager = null;
    try {

      txManager = AccountDAO.getTxManager();
      txManager.begin();

      AccountDAO a1 = AccountDAO.select(1404);
      a1.setCurrentBalance(350);
      a1.update();

      AccountDAO a2 = AccountDAO.select(2780);
      a2.setCurrentBalance(100);
      a2.update();

      txManager.commit();

    } finally {
      if (txManager != null) {
        txManager.close();
      }
    }

  }

}
