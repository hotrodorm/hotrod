package com.myapp1;

public class StatementMetrics {

  private String actualSQL;
  private String compactedSQL;

  private long minElapsed;
  private long maxElapsed;

  private long executions;
  private long sumElapsed;

  public StatementMetrics(final String sql) {
    this.actualSQL = sql;
    this.compactedSQL = compact(sql);

    this.minElapsed = 0;
    this.maxElapsed = 0;

    this.executions = 0;
    this.sumElapsed = 0;
  }

  public void record(final long elapsedTime, final boolean succeeded) {
    if (this.executions == 0 || elapsedTime < this.minElapsed) {
      this.minElapsed = elapsedTime;
    }
    if (this.executions == 0 || elapsedTime > this.maxElapsed) {
      this.maxElapsed = elapsedTime;
    }
    this.executions++;
    this.sumElapsed += elapsedTime;
  }

  public String toString() {
    return "" + this.executions + " exe, avg " + (this.sumElapsed / this.executions) + " ms [" + this.minElapsed + "-"
        + this.maxElapsed + " ms] -- " + this.compactedSQL;
  }

  // Getters

  public long getExecutions() {
    return executions;
  }

  // Utility

  private String compact(final String txt) {
    String[] chunks = txt.split("\n");
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (String c : chunks) {
      String t = c.trim();
      if (!t.isEmpty()) {
        if (first) {
          first = false;
        } else {
          sb.append(" ");
        }
        sb.append(t);
      }
    }
    return sb.toString();
  }

}
