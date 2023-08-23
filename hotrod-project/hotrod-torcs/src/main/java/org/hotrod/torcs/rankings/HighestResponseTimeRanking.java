package org.hotrod.torcs.rankings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HighestResponseTimeRanking implements Ranking {

  private static final int DEFAULT_LIMIT = 3;
  private static final int MIN_LIMIT = 1;
  private static final int MAX_LIMIT = 100;

  private int limit;
  private ArrayList<Query> list = new ArrayList<>();

  public HighestResponseTimeRanking() {
    this.limit = DEFAULT_LIMIT;
  }

  public HighestResponseTimeRanking(int limit) {
    if (limit < MIN_LIMIT) {
      throw new RuntimeException("limit must be greater or equal to " + MIN_LIMIT + " but it's " + limit + ".");
    }
    if (limit > MAX_LIMIT) {
      throw new RuntimeException("limit must be less than or equal to " + MAX_LIMIT + " but it's " + limit + ".");
    }
    this.limit = limit;
    clear();
  }

  @Override
  public String getTitle() {
    return "Highest Response Time";
  }

  @Override
  public void clear() {
    this.list.clear();
  }

  @Override
  public synchronized void add(Query q) {
    int pos = findPosition(q);
    // TODO implement add+remove into a single change for highest performance
    this.list.add(pos, q);

    if (this.list.size() > this.limit) {
      this.list.remove(this.limit);
    }
  }

  private int findPosition(Query q) {
    // TODO: Implement binary search for highest performance
    int pos = 0;
    Iterator<Query> it = this.list.iterator();
    Query c;
    while (it.hasNext() && (c = it.next()) != null && c.getResponseTime() >= q.getResponseTime()) {
      pos++;
    }
    return pos;
  }

  @Override
  public List<Query> list() {
    return this.list;
  }

}
