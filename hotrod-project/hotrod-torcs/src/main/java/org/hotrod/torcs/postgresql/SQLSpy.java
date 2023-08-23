package org.hotrod.torcs.postgresql;

import org.hotrod.torcs.rankings.Query;

public class SQLSpy {

  public static void main(String[] args) {

    DataSourceRankings rankings = new DataSourceRankings();

    rankings.record(makeQuery("select 1", 100, "plan1"));
    rankings.record(makeQuery("select 5", 500, "plan5"));
    rankings.record(makeQuery("select 4a", 400, "plan4a"));
    rankings.record(makeQuery("select 3a", 300, "plan3a"));
    rankings.record(makeQuery("select 2", 200, "plan2"));
    rankings.record(makeQuery("select 3b", 300, "plan3b"));
    rankings.record(makeQuery("select 4b", 400, "plan4b"));

    rankings.getRankings().forEach(r -> {
      System.out.println("Ranking: " + r.getTitle());
      r.list().forEach(q -> System.out.println(".. " + q));
    });

  }

  private static Query makeQuery(String q, int rt, String p) {
    return new Query() {
      public String getSQL() {
        return q;
      }

      @Override
      public int getResponseTime() {
        return rt;
      }

      @Override
      public String getPlan() {
        return p;
      }

      public String toString() {
        return q + ":" + rt + "(" + p + ")";
      }

    };
  }

}
