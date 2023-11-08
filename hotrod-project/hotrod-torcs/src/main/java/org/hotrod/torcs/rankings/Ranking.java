package org.hotrod.torcs.rankings;

import java.util.Collection;

import org.hotrod.torcs.QuerySampleObserver;

public abstract class Ranking extends QuerySampleObserver {

  public abstract Collection<RankingEntry> getRanking();

}
