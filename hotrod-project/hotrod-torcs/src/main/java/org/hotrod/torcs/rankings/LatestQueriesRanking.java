package org.hotrod.torcs.rankings;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.hotrod.torcs.QueryExecution;

public class LatestQueriesRanking extends Ranking {

  private static final int DEFAULT_SIZE = 10;
  private static final int MIN_SIZE = 1;
  private static final int MAX_SIZE = 1000;

  private int size;

  public LatestQueriesRanking() {
    this.size = DEFAULT_SIZE;
  }

  public LatestQueriesRanking(int size) {
    setSize(size);
  }

  public synchronized void setSize(int size) {
    if (size < MIN_SIZE) {
      throw new RuntimeException("Ranking size must be greater or equal to " + MIN_SIZE + " but it's " + size + ".");
    }
    if (size > MAX_SIZE) {
      throw new RuntimeException("Ranking size must be less than or equal to " + MAX_SIZE + " but it's " + size + ".");
    }
    this.size = size;
    restart();
  }

  @Override
  public String getTitle() {
    return "Latest Queries (max: " + this.size + ")";
  }

  @Override
  public synchronized void restart() {
    this.cacheByDSSQL.clear();
  }

  private LinkedHashMap<String, RankingEntry> cacheByDSSQL = new LinkedHashMap<String, RankingEntry>() {
    private static final long serialVersionUID = 1L;

    protected boolean removeEldestEntry(final Map.Entry<String, RankingEntry> eldest) {
      return size() > size;
    }
  };

  private String getCacheId(final QueryExecution execution) {
    return "ds" + execution.dsr.getId() + ":" + execution.sql;
  }

  @Override
  public synchronized void apply(final QueryExecution execution) {
    RankingEntry entry = this.cacheByDSSQL.get(this.getCacheId(execution));
    if (entry != null) {
      entry.apply(execution);
    } else {
      entry = new RankingEntry(execution);
      this.cacheByDSSQL.put(this.getCacheId(execution), entry);
    }
  }

  // Default ranking, sorted by execution order

  @Override
  public Collection<RankingEntry> getRanking() {
    return this.cacheByDSSQL.values().stream().map(e -> e.clone()).collect(Collectors.toList());
  }

  // Ranking entries by other orderings

  public Collection<RankingEntry> getRankingByHighestResponseTime() {
    return this.cacheByDSSQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getMaxTime(), b.getMaxTime())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingByHighestAvgResponseTime() {
    return this.cacheByDSSQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getAverageTime(), b.getAverageTime())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingByMostExecuted() {
    return this.cacheByDSSQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getExecutions(), b.getExecutions())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingByTotalElapsedTime() {
    return this.cacheByDSSQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getTotalElapsedTime(), b.getTotalElapsedTime())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingByMostRecentlyExecuted() {
    return this.cacheByDSSQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getLastExecutionAt(), b.getLastExecutionAt())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingByMostErrors() {
    return this.cacheByDSSQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getErrors(), b.getErrors())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingErrorsByMostRecent() {
    return this.cacheByDSSQL.values().stream().filter(a -> a.getErrors() > 0).map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getLastExecutionAt(), b.getLastExecutionAt())).collect(Collectors.toList());
  }

}
