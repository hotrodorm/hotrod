package org.hotrod.torcs.rankings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.hotrod.torcs.QueryExecution;

public class HighestFrequencyRanking extends Ranking {

  private static final int DEFAULT_SIZE = 30;
  private static final int MIN_SIZE = 1;
  private static final int MAX_SIZE = 1000;

  private int size;

  public HighestFrequencyRanking() {
    this.size = DEFAULT_SIZE;
  }

  public HighestFrequencyRanking(int size) {
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

    System.out.println("execution=" + execution + " - execution.dsr=" + execution.dsr);

    RankingEntry entry = this.cacheByDSSQL.get(this.getCacheId(execution));

    if (entry != null) { // 1. It's already in the ranking
      upgradePosition(entry, entry.executions + 1);
      entry.apply(execution);
    } else { // 2. New query (not in the ranking)
      entry = new RankingEntry(execution);
      if (insert(entry)) {
        this.cacheByDSSQL.put(this.getCacheId(execution), entry);
      }
    }

  }

  private void upgradePosition(final RankingEntry entry, final int newExecutions) {
//    System.out.println("### Upgrading entry: " + entry);
    boolean searching = true;

    ListIterator<RankingEntry> lit = this.sorted.listIterator();
//    int pos = 0;
    while (lit.hasNext()) {
      RankingEntry current = lit.next();
//      System.out
//          .println("-- Walking (searching=" + searching + "): " + current.getSQL() + " -- maxTime=" + current.maxTime);
      if (searching) {
        if (newExecutions > current.executions) {
//          System.out.println("current.pos == 0: " + (current.pos == 0) + "  current == entry: " + (current == entry));
          if (current == entry) {
            return;
          } else {
            lit.previous();
            lit.add(entry);
            lit.next();
            searching = false;
          }
        }
      } else {
        if (current == entry) {
          lit.remove();
          return;
        }
      }
    }

  }

  private boolean insert(final RankingEntry entry) {
    if (this.sorted.size() < this.size) {
      this.sorted.add(entry);
      return true;
    }
    return false;
  }

  @Override
  public List<RankingEntry> getEntries() {
    return this.sorted.stream().map(e -> e.cloneEntry()).collect(Collectors.toList());
  }

}
