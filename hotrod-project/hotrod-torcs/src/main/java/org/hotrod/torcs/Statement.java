package org.hotrod.torcs;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.hotrodorm.hotrod.utils.XUtil;

public class Statement {

  private String actualSQL;

  private long minTime;
  private long maxTime;

  private long totalExecutions;
  private long executionErrors;
  private long sum;
  private long sumSQ;

  private long lastExecuted;

  private long lastExceptionTimestamp;
  private Throwable lastException;

  Statement(final String sql) {
    this.actualSQL = sql;

    this.minTime = 0;
    this.maxTime = 0;

    this.totalExecutions = 0;
    this.executionErrors = 0;
    this.sum = 0;
    this.sumSQ = 0;

    this.lastExecuted = 0;
    this.lastExceptionTimestamp = 0;
    this.lastException = null;
  }

  void record(final long elapsedTime, final Throwable t) {
    this.lastExecuted = System.currentTimeMillis();
    if (this.totalExecutions == 0 || elapsedTime < this.minTime) {
      this.minTime = elapsedTime;
    }
    if (this.totalExecutions == 0 || elapsedTime > this.maxTime) {
      this.maxTime = elapsedTime;
    }
    this.totalExecutions++;
    if (t != null) {
      this.executionErrors++;
      this.lastExceptionTimestamp = this.lastExecuted;
      this.lastException = t;
    }
    this.sum += elapsedTime;
    this.sumSQ += elapsedTime * elapsedTime;

  }

  // toString()

  public String toString() {
    String le = this.lastExecuted == 0 ? "never" : new Date(this.lastExecuted).toString();
    if (this.lastException == null) {
      return "" + this.totalExecutions + " exe" + ", " + this.executionErrors + " errors" + ", avg "
          + (this.sum / this.totalExecutions) + " ms, \u03c3 " + Math.round(this.getTimeStandardDeviation()) + " ["
          + this.minTime + "-" + this.maxTime + " ms], last executed: " + le + ", last exception: never -- "
          + this.actualSQL;
    } else {
      return "" + this.totalExecutions + " exe" + ", " + this.executionErrors + " errors" + ", avg "
          + (this.sum / this.totalExecutions) + " ms, \u03c3 " + Math.round(this.getTimeStandardDeviation()) + " ["
          + this.minTime + "-" + this.maxTime + " ms], last executed: " + le + ", last exception at "
          + new Date(this.lastExceptionTimestamp) + ": " + XUtil.trim(this.lastException) + "\n" + this.actualSQL;
    }
  }

  // Getters

  public String getActualSQL() {
    return actualSQL;
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

  public long getLastExecuted() {
    return lastExecuted;
  }

  public long getLastExceptionTimestamp() {
    return lastExceptionTimestamp;
  }

  public Throwable getLastException() {
    return lastException;
  }

  // Extra getters

  public long getAverageTime() {
    return this.totalExecutions == 0 ? -1 : this.sum / this.totalExecutions;
  }

  /**
   * See Welford's online algorithm:
   * https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Online_algorithm
   * 
   * @return the standard deviation
   */
  public double getTimeStandardDeviation() {
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
