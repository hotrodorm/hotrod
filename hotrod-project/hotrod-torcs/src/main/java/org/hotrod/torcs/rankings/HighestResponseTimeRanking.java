package org.hotrod.torcs.rankings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
  public synchronized void consume(final Query q) {

    // 1. Maybe it's already in the ranking

    RankingEntry hrtq = this.bySQL.get(q.getSQL());
    if (hrtq != null) {

      if (q.getResponseTime() > hrtq.getMaxTime()) {
        upgradePosition(hrtq);
        hrtq.maxTime = q.getResponseTime();
      }

    } else { // 2. New query (not in the ranking)

      insert(hrtq);

    }

  }

  private void upgradePosition(final RankingEntry hrtq) {
    boolean searching = true;
    for (RankingEntry q : this.sorted) {
      if (searching) {
        if (q.maxTime < hrtq.maxTime) {
          this.sorted.remove(hrtq.pos);
          this.sorted.add(q.pos, hrtq);
          hrtq.pos = q.pos;
          q.pos++;
          searching = false;
        }
      } else {
        q.pos++;
      }
    }
  }

  private void insert(final RankingEntry hrtq) {
    boolean searching = true;
    for (RankingEntry q : this.sorted) {
      if (searching) {
        if (q.maxTime < hrtq.maxTime) {
          this.sorted.add(q.pos, hrtq);
          hrtq.pos = q.pos;
          q.pos++;
          searching = false;
        }
      } else {
        q.pos++;
      }
    }
    if (!searching && this.sorted.size() > this.size) {
      this.sorted.remove(this.size);
    }
  }

  @Override
  public List<RankingEntry> getRanking() {
    return this.sorted;
  }

}
