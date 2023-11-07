package manual;

import org.hotrod.torcs.rankings.HighestResponseTimeRanking;
import org.hotrod.torcs.rankings.QueryExecution;

public class HRTTest {

  public static void main(final String[] args) {

    QueryExecution q1 = new QueryExecution("sql1", 30, null);
    QueryExecution q2 = new QueryExecution("sql2", 10, null);
    QueryExecution q3 = new QueryExecution("sql3", 20, null);

    HighestResponseTimeRanking rk = new HighestResponseTimeRanking(4);

    rk.consume(q1);
    rk.consume(q2);
    rk.consume(q3);

    System.out.println("--- Ranking A ---");
    rk.getRanking().stream().forEach(q -> System.out.println(q));
    System.out.println("");

    rk.consume(new QueryExecution("sql4", 40, null));
    rk.consume(new QueryExecution("sql5", 60, null));
    rk.consume(new QueryExecution("sql1", 55, null));

    System.out.println("--- Ranking B ---");
    rk.getRanking().stream().forEach(q -> System.out.println(q));

    
//    --- Ranking B ---
//    #1: 1 exe, 0 errors, avg 40 ms, σ 0 [40-40 ms], last executed: Mon Nov 06 21:25:20 EST 2023, last exception: N/A -- sql4
//    #2: 1 exe, 0 errors, avg 30 ms, σ 0 [30-30 ms], last executed: Mon Nov 06 21:25:20 EST 2023, last exception: N/A -- sql1
//    #2: 1 exe, 0 errors, avg 20 ms, σ 0 [20-20 ms], last executed: Mon Nov 06 21:25:20 EST 2023, last exception: N/A -- sql3
  }

}
