package manual;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hotrod.torcs.DataSourceReference;
import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.rankings.HighestFrequencyRanking;

public class HRTTest {

  private static final int RANKING_SIZE = 10;

  public static void main(final String[] args) {

    DataSource ds1 = getDataSource();
    DataSourceReference dsr1 = DataSourceReference.of(ds1);

    QueryExecution q1 = new QueryExecution(dsr1, "sql1", null, null, 30, null);
    QueryExecution q2 = new QueryExecution(dsr1, "sql2", null, null, 10, null);
    QueryExecution q3 = new QueryExecution(dsr1, "sql3", null, null, 20, null);
    QueryExecution q21 = new QueryExecution(dsr1, "sql2", null, null, 10, null);
    QueryExecution q22 = new QueryExecution(dsr1, "sql2", null, null, 10, null);
    QueryExecution q31 = new QueryExecution(dsr1, "sql3", null, null, 20, null);

    HighestFrequencyRanking rk = new HighestFrequencyRanking(RANKING_SIZE);

    rk.apply(q1);
    rk.apply(q2);
    rk.apply(q3);

    rk.apply(q21);
    rk.apply(q22);
    rk.apply(q31);

    System.out.println("--- Torcs Ranking TOP " + RANKING_SIZE + " ---");
    rk.getEntries().stream().forEach(q -> System.out.println(q));
    System.out.println("");

    rk.apply(new QueryExecution(dsr1, "sql1", null, null, 5, null));
    rk.apply(new QueryExecution(dsr1, "sql3", null, null, 20, null));
    rk.apply(new QueryExecution(dsr1, "sql2", null, null, 25, null));

    System.out.println("--- Torcs Ranking TOP " + RANKING_SIZE + " ---");
    rk.getEntries().stream().forEach(q -> System.out.println(q));

//    --- Ranking B ---
//    #1: 1 exe, 0 errors, avg 40 ms, σ 0 [40-40 ms], last executed: Mon Nov 06 21:25:20 EST 2023, last exception: N/A -- sql4
//    #2: 1 exe, 0 errors, avg 30 ms, σ 0 [30-30 ms], last executed: Mon Nov 06 21:25:20 EST 2023, last exception: N/A -- sql1
//    #2: 1 exe, 0 errors, avg 20 ms, σ 0 [20-20 ms], last executed: Mon Nov 06 21:25:20 EST 2023, last exception: N/A -- sql3
  }

  static DataSource getDataSource() {
    return new DataSource() {

      @Override
      public PrintWriter getLogWriter() throws SQLException {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public void setLogWriter(PrintWriter out) throws SQLException {
        // TODO Auto-generated method stub

      }

      @Override
      public void setLoginTimeout(int seconds) throws SQLException {
        // TODO Auto-generated method stub

      }

      @Override
      public int getLoginTimeout() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
      }

      @Override
      public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public Connection getConnection() throws SQLException {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Connection getConnection(String username, String password) throws SQLException {
        // TODO Auto-generated method stub
        return null;
      }

    };
  }

}
