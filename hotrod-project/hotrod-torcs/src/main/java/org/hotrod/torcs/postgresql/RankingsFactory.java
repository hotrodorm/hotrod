package org.hotrod.torcs.postgresql;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.torcs.rankings.HighestResponseTimeRanking;
import org.hotrod.torcs.rankings.Ranking;

public class RankingsFactory {

  public List<Ranking> getRankings() {
    return Stream.of(new HighestResponseTimeRanking()).collect(Collectors.toList());
  }

}
