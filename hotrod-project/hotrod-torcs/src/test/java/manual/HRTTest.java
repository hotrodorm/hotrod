package manual;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.rankings.HighestResponseTimeRanking;

public class HRTTest {

  private static final int RANKING_SIZE = 2;

  public static void main(final String[] args) {

    QueryExecution q1 = new QueryExecution(null, "sql1", null, 30, null);
    QueryExecution q2 = new QueryExecution(null, "sql2", null, 10, null);
    QueryExecution q3 = new QueryExecution(null, "sql3", null, 20, null);

    HighestResponseTimeRanking rk = new HighestResponseTimeRanking(RANKING_SIZE);

    rk.apply(q1);
    rk.apply(q2);
    rk.apply(q3);

    System.out.println("--- Torcs Ranking TOP " + RANKING_SIZE + " ---");
    rk.getRanking().stream().forEach(q -> System.out.println(q));
    System.out.println("");

    rk.apply(new QueryExecution(null, "sql1", null, 5, null));
    rk.apply(new QueryExecution(null, "sql3", null, 20, null));
    rk.apply(new QueryExecution(null, "sql2", null, 25, null));

    System.out.println("--- Torcs Ranking TOP " + RANKING_SIZE + " ---");
    rk.getRanking().stream().forEach(q -> System.out.println(q));

//    --- Ranking B ---
//    #1: 1 exe, 0 errors, avg 40 ms, σ 0 [40-40 ms], last executed: Mon Nov 06 21:25:20 EST 2023, last exception: N/A -- sql4
//    #2: 1 exe, 0 errors, avg 30 ms, σ 0 [30-30 ms], last executed: Mon Nov 06 21:25:20 EST 2023, last exception: N/A -- sql1
//    #2: 1 exe, 0 errors, avg 20 ms, σ 0 [20-20 ms], last executed: Mon Nov 06 21:25:20 EST 2023, last exception: N/A -- sql3
  }

}
