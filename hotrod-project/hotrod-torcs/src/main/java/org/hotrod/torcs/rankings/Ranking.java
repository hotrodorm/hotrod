package org.hotrod.torcs.rankings;

import java.util.Collection;

import org.hotrod.torcs.QuerySampleConsumer;

public abstract class Ranking extends QuerySampleConsumer {

  public abstract Collection<RankingEntry> getRanking();

}
