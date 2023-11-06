package org.hotrod.torcs.rankings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HighestResponseTimeRanking extends Ranking {

  private static final int DEFAULT_SIZE = 10;
  private static final int MIN_SIZE = 1;
  private static final int MAX_SIZE = 100;

  private int size;

  public HighestResponseTimeRanking() {
    this.size = DEFAULT_SIZE;
  }

  public HighestResponseTimeRanking(int size) {
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
    return "Highest Response Time";
  }

  @Override
  public synchronized void reset() {
    this.sorted.clear();
  }

  /**
   * <pre>
   * pos   RT SQL
   * --- ---- -----------------------------
   *   1  500 select 1
   *   2  200 select 2
   *   3   70 select 3
   *   4   40 select 4
   * </pre>
   */

  private ArrayList<RankingEntry> sorted = new ArrayList<>();
  private HashMap<String, RankingEntry> bySQL = new HashMap<>();

  @Override
  public synchronized void consume(final QueryExecution execution) {

    RankingEntry entry = this.bySQL.get(execution.getSQL());

    if (entry != null) { // 1. It's already in the ranking
      System.out.println(">>> Entry already in the ranking.");

      entry.apply(execution);
      if (execution.getResponseTime() > entry.getMaxTime()) {
        upgradePosition(entry);
      }

    } else { // 2. New query (not in the ranking)
      System.out.println(">>> Entry is new.");

      entry = new RankingEntry(execution);
      if (insert(entry)) {
        this.bySQL.put(execution.getSQL(), entry);
      }
      System.out.println(">>> Entry was not inserted");

    }

    System.out
        .println(">>> [Stats] this.sorted.size()=" + this.sorted.size() + " this.bySQL.size()=" + this.bySQL.size());

  }

  private void upgradePosition(final RankingEntry entry) {
    boolean searching = true;
    for (RankingEntry current : this.sorted) {
      if (searching) {
        if (current.maxTime < entry.maxTime) {
          this.sorted.remove(entry.pos);
          this.sorted.add(current.pos, entry);
          entry.pos = current.pos;
          current.pos++;
          searching = false;
        }
      } else {
        current.pos++;
      }
    }
  }

  private boolean insert(final RankingEntry entry) {
    if (this.sorted.isEmpty()) {
      entry.pos = 0;
      this.sorted.add(entry.pos, entry);
      return true;
    } else {
      boolean inserted = false;
      for (RankingEntry current : this.sorted) {
        if (!inserted) {
          if (entry.maxTime > current.maxTime) {
            entry.pos = current.pos;
            this.sorted.add(entry.pos, entry);
            current.pos++;
            inserted = true;
            break;
          }
        } else {
          current.pos++;
        }
      }

      if (inserted) {
        if (this.sorted.size() > this.size) {
          RankingEntry removed = this.sorted.remove(this.size);
          this.bySQL.remove(removed.getSQL());
        }
      } else {
        if (this.sorted.size() < this.size) {
          entry.pos = this.sorted.size();
          this.sorted.add(entry);
          inserted = true;
        }
      }

      return inserted;

    }
  }

  @Override
  public List<RankingEntry> getRanking() {
    return this.sorted;
  }

}
