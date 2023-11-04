package org.hotrod.torcs.rankings;

import java.util.List;

import org.hotrod.torcs.QueryConsumer;

public abstract class Ranking extends QueryConsumer {

  public abstract void reset();

  public abstract List<RankingEntry> getRanking();

}
