package org.hotrod.torcs.rankings;

import java.util.List;

import org.hotrod.torcs.QuerySampleConsumer;

public abstract class Ranking extends QuerySampleConsumer {

  public abstract void reset();

  public abstract List<RankingEntry> getRanking();

}
