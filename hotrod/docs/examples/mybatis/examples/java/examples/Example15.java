package examples;

import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.hotrod.runtime.tx.TxManager;

import daos.BranchDAO;

/**
 * Example 15 - Transactions & Isolation Levels
 * 
 * @author Vladimir Alarcon
 * 
 */
public class Example15 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 15 - Transactions & Isolation Levels ===");

    // 1. No Transaction

    transferNoTransaction(101, 102, 500);
    System.out.println(" ");
    System.out.println("1. No Transaction.");

    // 2. Standard Transactions

    transferStandardTransaction(101, 102, 500);
    System.out.println(" ");
    System.out.println("2. Standard Transactions.");

    // 3. Custom Transactions (with isolation level)
    // Case #3 is for documentation purposes only and does not work in H2 1.3.x
    // Case #3 should work on advanced RDBMS such as DB2 or Oracle.

    transferCustomTransaction(101, 102, 500);
    System.out.println(" ");
    System.out.println("3. Custom Transactions (with isolation level).");

    // 4. Interlaced Transactions
    // Case #4 is for documentation purposes only and does not work in H2 1.3.x
    // Case #4 should work on advanced RDBMS such as DB2 or Oracle.

    transferInterlacedTransactions(101, 102, 500, 13, 104, 200);
    System.out.println(" ");
    System.out.println("4. Interlaced Transactions.");

    System.out.println(" ");
    System.out.println("=== Example 15 Complete ===");

  }

  /**
   * No Transaction
   * 
   * There is no safety in term of transactions.
   * 
   * By default there are no transactions in place if not specified and the
   * persistence works in auto-commit mode. If there's a failure between both
   * updates the tables may end up in the wrong state.
   * 
   * Cash may disappear since one update completed and the second never was
   * executed!
   */
  public static void transferNoTransaction(final Integer from, final Integer to, final Integer amount)
      throws SQLException {

    BranchDAO b1 = BranchDAO.select(from);
    BranchDAO b2 = BranchDAO.select(to);

    b1.setCurrentCash(b1.getCurrentCash() - amount);
    b1.update(); // updates branch #1

    b2.setCurrentCash(b2.getCurrentCash() + amount);
    b2.update(); // updates branch #2

  }

  /**
   * Standard Transaction
   * 
   * The transaction is safe. If there's a failure between both updates the
   * whole transaction is rolled back and no changes are performed. The database
   * reverts to the initial consistent state. There's no lost cash under
   * hardware or software failures.
   */
  public static void transferStandardTransaction(final Integer from, final Integer to, final Integer amount)
      throws SQLException {

    TxManager txManager = null;
    try {

      txManager = BranchDAO.getTxManager();
      txManager.begin();

      BranchDAO b1 = BranchDAO.select(from);
      BranchDAO b2 = BranchDAO.select(to);

      b1.setCurrentCash(b1.getCurrentCash() - amount);
      b1.update(); // updates branch #1

      b2.setCurrentCash(b2.getCurrentCash() + amount);
      b2.update(); // updates branch #2

      txManager.commit();

    } finally { // Always free resources in the finally clause!
      if (txManager != null) {
        txManager.close();
      }
    }

  }

  /**
   * Transaction with specified Isolation Level
   * 
   * In this case, the MyBatis SqlSession object is retrieved and customized to
   * set the Isolation Level.
   * 
   * The transaction is started in a custom way.
   * 
   * The transaction is safe. If there's a failure between both updates the
   * whole transaction is rolled back and no changes are performed. The database
   * reverts to the initial consistent state.
   * 
   * There's no lost cash under hardware or software failures.
   */
  public static void transferCustomTransaction(final Integer from, final Integer to, final Integer amount)
      throws SQLException {

    System.out.println("[ 5. Single custom transaction ]");

    TxManager txManager = null;
    try {

      txManager = BranchDAO.getTxManager();
      SqlSession sqlSession = txManager.getSqlSessionFactory().openSession(TransactionIsolationLevel.SERIALIZABLE);
      txManager.begin(sqlSession);

      BranchDAO b1 = BranchDAO.select(from);
      BranchDAO b2 = BranchDAO.select(to);

      b1.setCurrentCash(b1.getCurrentCash() - amount);
      b1.update(); // updates branch #1

      b2.setCurrentCash(b2.getCurrentCash() + amount);
      b2.update(); // updates branch #2

      txManager.commit();

    } finally { // Always free resources in the finally clause!
      txManager.close();
    }

  }

  /**
   * Interlaced Transactions
   * 
   * These are multiple simultaneous transactions on the same JDBC Connection
   * object. Each one uses separate SqlSession objects, and demarcates
   * transactions using the begin(), commit() and/or rollback() methods.
   *
   * This functionality is heavily dependent on the RDBMS and the JDBC driver.
   * Sometimes interlaced transactions are genuine, sometimes are simulated
   * under the hood, sometimes are not supported at all.
   * 
   * Both transactions are safe. If there's a failure between the updates the
   * uncommitted transactions are rolled back and no changes are performed on
   * their behalf. The database reverts to the initial consistent state.
   * Committed transactions are not reverted.
   * 
   * There's no lost cash under hardware or software failures.
   */
  public static void transferInterlacedTransactions(final Integer from1, final Integer to1, final Integer amount1,
      final Integer from2, final Integer to2, final Integer amount2) throws SQLException {

    TxManager txManager = null;
    SqlSession sqlSession1 = null;
    SqlSession sqlSession2 = null;

    try {

      txManager = BranchDAO.getTxManager();
      sqlSession1 = txManager.getSqlSessionFactory().openSession();
      sqlSession2 = txManager.getSqlSessionFactory().openSession();

      BranchDAO b1 = BranchDAO.select(sqlSession1, from1);
      BranchDAO b2 = BranchDAO.select(sqlSession1, to1);

      BranchDAO b3 = BranchDAO.select(sqlSession2, from2);
      BranchDAO b4 = BranchDAO.select(sqlSession2, to2);

      b1.setCurrentCash(b1.getCurrentCash() - amount1);
      b1.update(sqlSession1); // updates branch #1

      b3.setCurrentCash(b3.getCurrentCash() - amount2);
      b3.update(sqlSession2); // updates branch #3

      b2.setCurrentCash(b2.getCurrentCash() + amount1);
      b2.update(sqlSession1); // updates branch #2

      sqlSession1.commit(); // first commit

      b4.setCurrentCash(b4.getCurrentCash() + amount2);
      b4.update(sqlSession2); // updates branch #4

      sqlSession2.commit(); // second commit

    } finally { // Always free resources in the finally clause!
      // Under a failure a SqlSession.close() automatically rolls back.
      try {
        sqlSession1.close();
      } finally {
        try {
          sqlSession2.close();
        } finally {
          txManager.close();
        }
      }
    }

  }

}
