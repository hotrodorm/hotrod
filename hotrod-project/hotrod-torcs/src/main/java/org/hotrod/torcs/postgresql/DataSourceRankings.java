package org.hotrod.torcs.postgresql;

import java.util.List;

import org.hotrod.torcs.rankings.Query;
import org.hotrod.torcs.rankings.Ranking;

public class DataSourceRankings {

  private List<Ranking> rankings;

  public DataSourceRankings() {
    RankingsFactory rf = new RankingsFactory();
    this.rankings = rf.getRankings();
  }

  public void record(Query q) {
    this.rankings.stream().forEach(r -> r.add(q));
  }

  public List<Ranking> getRankings() {
    return rankings;
  }

}
