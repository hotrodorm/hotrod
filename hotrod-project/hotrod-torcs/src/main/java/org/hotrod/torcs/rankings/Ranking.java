package org.hotrod.torcs.rankings;

import java.util.List;

import org.hotrod.torcs.QueryExecutionConsumer;

public abstract class Ranking extends QueryExecutionConsumer {

  public abstract void reset();

  public abstract List<RankingEntry> getRanking();

}
