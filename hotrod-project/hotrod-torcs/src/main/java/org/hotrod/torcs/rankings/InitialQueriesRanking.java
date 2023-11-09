package org.hotrod.torcs.rankings;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.hotrod.torcs.QueryExecution;

public class InitialQueriesRanking extends Ranking {

  private static final int DEFAULT_SIZE = 10;
  private static final int MIN_SIZE = 1;
  private static final int MAX_SIZE = 1000;

  private int size;

  public InitialQueriesRanking() {
    this.size = DEFAULT_SIZE;
  }

  public InitialQueriesRanking(int size) {
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
    reset();
  }

  @Override
  public String getTitle() {
    return "Initial Queries (max: " + this.size + ")";
  }

  @Override
  public synchronized void reset() {
    this.bySQL.clear();
  }

  private LinkedHashMap<String, RankingEntry> bySQL = new LinkedHashMap<>();

  @Override
  public synchronized void apply(final QueryExecution execution) {
    RankingEntry entry = this.bySQL.get(execution.getSQL());
    if (entry != null) {
      entry.apply(execution);
    } else {
      if (this.bySQL.size() < this.size) {
        entry = new RankingEntry(execution);
        this.bySQL.put(execution.getSQL(), entry);
      }
    }
  }

  // Default ranking, sorted by execution order

  @Override
  public Collection<RankingEntry> getRanking() {
    return this.bySQL.values().stream().map(e -> e.clone()).collect(Collectors.toList());
  }

  // Ranking entries by other orderings

  public Collection<RankingEntry> getRankingByHighestResponseTime() {
    return this.bySQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getMaxTime(), b.getMaxTime())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingByHighestAvgResponseTime() {
    return this.bySQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getAverageTime(), b.getAverageTime())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingByMostExecuted() {
    return this.bySQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getExecutions(), b.getExecutions())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingByTotalElapsedTime() {
    return this.bySQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getTotalElapsedTime(), b.getTotalElapsedTime())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingByMostRecentlyExecuted() {
    return this.bySQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getLastExecutionAt(), b.getLastExecutionAt())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingByMostErrors() {
    return this.bySQL.values().stream().map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getErrors(), b.getErrors())).collect(Collectors.toList());
  }

  public Collection<RankingEntry> getRankingErrorsByMostRecent() {
    return this.bySQL.values().stream().filter(a -> a.getErrors() > 0).map(e -> e.clone())
        .sorted((a, b) -> -Long.compare(a.getLastExecutionAt(), b.getLastExecutionAt())).collect(Collectors.toList());
  }

}
