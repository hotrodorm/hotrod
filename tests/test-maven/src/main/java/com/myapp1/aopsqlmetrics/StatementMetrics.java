package com.myapp1.aopsqlmetrics;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StatementMetrics {

  private String actualSQL;
  private String compactedSQL;

  private long minTime;
  private long maxTime;

  private long totalExecutions;
  private long executionErrors;
  private long sum;
  private long sumSQ;

  public StatementMetrics(final String sql) {
    this.actualSQL = sql;
    this.compactedSQL = compact(sql);

    this.minTime = 0;
    this.maxTime = 0;

    this.totalExecutions = 0;
    this.executionErrors = 0;
    this.sum = 0;
    this.sumSQ = 0;
  }

  public void record(final long elapsedTime, final boolean succeeded) {
    if (this.totalExecutions == 0 || elapsedTime < this.minTime) {
      this.minTime = elapsedTime;
    }
    if (this.totalExecutions == 0 || elapsedTime > this.maxTime) {
      this.maxTime = elapsedTime;
    }
    this.totalExecutions++;
    if (!succeeded) {
      this.executionErrors++;
    }
    this.sum += elapsedTime;
    this.sumSQ += elapsedTime * elapsedTime;
  }

  public String toString() {
    DecimalFormat df = new DecimalFormat("0.00");
    return "" + this.totalExecutions + " exe"
        + (this.executionErrors != 0 ? (" (" + this.executionErrors + " errors)") : "") + ", avg "
        + (this.sum / this.totalExecutions) + " ms (\u03c3 " + df.format(this.getTimeStandardDeviation()) + ") ["
        + this.minTime + "-" + this.maxTime + " ms] -- " + this.compactedSQL;
  }

  // Getters

  public String getActualSQL() {
    return actualSQL;
  }

  public String getCompactedSQL() {
    return compactedSQL;
  }

  public long getMinTime() {
    return minTime;
  }

  public long getMaxTime() {
    return maxTime;
  }

  public long getTotalExecutions() {
    return totalExecutions;
  }

  public long getExecutionErrors() {
    return executionErrors;
  }

  // Extra getters

  public long getTimeAverage() {
    return this.totalExecutions == 0 ? -1 : this.sum / this.totalExecutions;
  }

  public double getTimeStandardDeviation() {
    // https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Online_algorithm
    return this.totalExecutions < 2 ? 0
        : Math.sqrt( //
            (this.sumSQ - 1.0 * this.sum * this.sum / this.totalExecutions) //
                / //
                (this.totalExecutions - 0));
  }

  // Utility

  private String compact(final String txt) {
    return txt == null ? null
        : Arrays.stream(txt.split("\n")).map(s -> s.trim()).filter(s -> !s.isEmpty()).collect(Collectors.joining(" "));
  }

}
