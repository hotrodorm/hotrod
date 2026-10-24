package org.hotrod.torcs.rankings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.torcs.QueryExecution;

public class HighestFrequencyRanking extends Ranking {

  private static final int DEFAULT_SIZE = 30;
  private static final int MIN_SIZE = 1;
  private static final int MAX_SIZE = 1000;
  private static final int DEFAULT_MAX_CACHE_SIZE = 500;

  private int size;
  private final int maxCacheSize;

  public HighestFrequencyRanking() {
    this(DEFAULT_SIZE, DEFAULT_MAX_CACHE_SIZE);
  }

  public HighestFrequencyRanking(int size) {
    this(size, DEFAULT_MAX_CACHE_SIZE);
  }

  public HighestFrequencyRanking(int size, int maxCacheSize) {
    if (maxCacheSize < MIN_SIZE) {
      throw new RuntimeException(
          "Maximum cache size must be greater or equal to " + MIN_SIZE + " but it's " + maxCacheSize + ".");
    }
    this.maxCacheSize = maxCacheSize;
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
    return "Highest Frequency (max: " + this.size + ")";
  }

  @Override
  public synchronized void executeReset() {
    this.sorted.clear();
    this.cacheByDSSQL.clear();
  }

  private ArrayList<RankingEntry> sorted = new ArrayList<>();
  private HashMap<String, RankingEntry> cacheByDSSQL = new HashMap<>();

  private String getCacheId(final QueryExecution execution) {
    return "ds" + execution.dsr.getId() + ":" + execution.sql;
  }

  @Override
  public synchronized void apply(final QueryExecution execution) {
    RankingEntry entry = this.cacheByDSSQL.get(this.getCacheId(execution));

    if (entry == null) {
      entry = new RankingEntry(execution);
      this.cacheByDSSQL.put(this.getCacheId(execution), entry);
      this.sorted.add(entry);
    } else {
      entry.apply(execution);
    }

    this.sorted.sort(Comparator.comparingInt(RankingEntry::getExecutions).reversed());
    trimCache();
  }

  private void trimCache() {
    while (this.sorted.size() > this.maxCacheSize) {
      RankingEntry removed = this.sorted.remove(this.sorted.size() - 1);
      this.cacheByDSSQL.remove(this.getCacheId(removed));
    }
  }

  private String getCacheId(final RankingEntry entry) {
    return "ds" + entry.getDataSourceReference().getId() + ":" + entry.getSQL();
  }

  @Override
  public synchronized List<RankingEntry> getEntries() {
    return this.sorted.stream().limit(this.size).map(e -> e.cloneEntry()).collect(Collectors.toList());
  }

}
