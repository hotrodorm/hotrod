package org.hotrod.torcs.rankings;

public class RankingEntry {

  int pos;

  private String sql;

  long minTime;
  long maxTime;
  long sum;
  long sumSQ;

  int executions;
  int errors;
  long lastExecuted;

  Throwable lastException;

  public RankingEntry(final Query query) {
  }

  // Setters

  void setPos(int pos) {
    this.pos = pos;
  }

  // Getters

  public int getPos() {
    return pos;
  }

  public String getSql() {
    return sql;
  }

  public long getMinTime() {
    return minTime;
  }

  public long getMaxTime() {
    return maxTime;
  }

  public long getSum() {
    return sum;
  }

  public long getSumSQ() {
    return sumSQ;
  }

  public int getExecutions() {
    return executions;
  }

  public int getErrors() {
    return errors;
  }

  public long getLastExecuted() {
    return lastExecuted;
  }

  public Throwable getLastException() {
    return lastException;
  }

}
