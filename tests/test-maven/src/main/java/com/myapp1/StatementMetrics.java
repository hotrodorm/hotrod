package com.myapp1;

import java.util.Arrays;
import java.util.stream.Collectors;

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

  public long getMaxTime() {
    return this.maxElapsed;
  }

  public long getAvgTime() {
    return this.executions == 0 ? -1 : this.sumElapsed / this.executions;
  }

  public long getExecutions() {
    return executions;
  }

  // Utility

  private String compact(final String txt) {
    return Arrays.stream(txt.split("\n")).map(s -> s.trim()).filter(s -> !s.isEmpty()).collect(Collectors.joining(" "));
  }

}
