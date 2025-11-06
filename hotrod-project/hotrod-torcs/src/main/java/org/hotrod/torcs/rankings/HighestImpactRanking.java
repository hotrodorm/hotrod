package org.hotrod.torcs.rankings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.hotrod.torcs.QueryExecution;

public class HighestImpactRanking extends Ranking {

  private static final int DEFAULT_SIZE = 30;
  private static final int MIN_SIZE = 1;
  private static final int MAX_SIZE = 1000;

  private int size;

  public HighestImpactRanking() {
    this.size = DEFAULT_SIZE;
  }

  public HighestImpactRanking(int size) {
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
    return "Highest Impact (max: " + this.size + ")";
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

  private String getCacheId(final RankingEntry entry) {
    return "ds" + entry.getDataSourceReference().getId() + ":" + entry.getSQL();
  }

  @Override
  public synchronized void apply(final QueryExecution execution) {

    System.out.println("execution=" + execution + " - execution.dsr=" + execution.dsr);

    RankingEntry entry = this.cacheByDSSQL.get(this.getCacheId(execution));

    if (entry != null) { // 1. It's already in the ranking
      upgradePosition(entry, entry.sumElapsed + execution.responseTime);
      entry.apply(execution);
    } else { // 2. New query (not in the ranking)
      entry = new RankingEntry(execution);
      if (insert(entry)) {
        this.cacheByDSSQL.put(this.getCacheId(execution), entry);
      }
    }

  }

  private void upgradePosition(final RankingEntry entry, final long sumElapsed) {
//    System.out.println("### Upgrading entry: " + entry);
    boolean searching = true;

    ListIterator<RankingEntry> lit = this.sorted.listIterator();
//    int pos = 0;
    while (lit.hasNext()) {
      RankingEntry current = lit.next();
//      System.out
//          .println("-- Walking (searching=" + searching + "): " + current.getSQL() + " -- maxTime=" + current.maxTime);
      if (searching) {
        if (sumElapsed > current.sumElapsed) {
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
    if (this.sorted.isEmpty()) {
      this.sorted.add(entry);
      return true;
    } else {
      boolean inserted = false;
      ListIterator<RankingEntry> lit = this.sorted.listIterator();
      while (lit.hasNext()) {
        RankingEntry current = lit.next();
        if (!inserted) {
          if (entry.sumElapsed > current.sumElapsed) {
            lit.previous();
            lit.add(entry);
            lit.next();
            inserted = true;
          }
        }
      }

      if (inserted) { // remove excess element
        if (this.sorted.size() > this.size) {
          RankingEntry removed = this.sorted.remove(this.size);
          this.cacheByDSSQL.remove(this.getCacheId(removed));
        }
      } else { // insert at the end
        if (this.sorted.size() < this.size) {
          this.sorted.add(entry);
          inserted = true;
        }
      }

      return inserted;

    }
  }

  @Override
  public List<RankingEntry> getEntries() {
    return this.sorted.stream().map(e -> e.cloneEntry()).collect(Collectors.toList());
  }

}
