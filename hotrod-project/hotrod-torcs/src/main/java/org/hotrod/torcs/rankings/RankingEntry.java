package org.hotrod.torcs.rankings;

import java.util.Date;

import org.hotrod.torcs.QuerySample;

public class RankingEntry {

  private String sql;
  private String compactSQL;

  long minTime = 0;
  long maxTime = 0;
  long sum = 0;
  long sumSQ = 0;

  int executions = 0;
  int errors = 0;
  long lastExecuted = 0;

  Throwable lastException = null;
  long lastExceptionTimestamp = 0;

  public RankingEntry(final QuerySample execution) {
    this.sql = execution.sql;
    this.compactSQL = QuerySample.compactSQL(this.sql);
    apply(execution);
  }

  public void apply(final QuerySample execution) {
    this.lastExecuted = System.currentTimeMillis();
    long elapsedTime = execution.getResponseTime();
    if (this.executions == 0 || elapsedTime < this.minTime) {
      this.minTime = elapsedTime;
    }
    if (this.executions == 0 || elapsedTime > this.maxTime) {
      this.maxTime = elapsedTime;
    }
    this.executions++;
    if (execution.exception != null) {
      this.errors++;
      this.lastExceptionTimestamp = this.lastExecuted;
      this.lastException = execution.exception;
    }
    this.sum += elapsedTime;
    this.sumSQ += elapsedTime * elapsedTime;
  }

  // Setters

  // Getters

  public String getSQL() {
    return sql;
  }

  public String getCompactSQL() {
    return compactSQL;
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

  public long getLastExceptionTimestamp() {
    return lastExceptionTimestamp;
  }

  public String toString() {
    String le = this.lastExecuted == 0 ? "never" : new Date(this.lastExecuted).toString();
    if (this.lastException == null) {
      return this.executions + " exe" + ", " + this.errors + " errors" + ", avg " + (this.sum / this.executions)
          + " ms, \u03c3 " + Math.round(this.getTimeStandardDeviation()) + " [" + this.minTime + "-" + this.maxTime
          + " ms], last executed: " + le + ", last exception: N/A -- " + this.compactSQL;
    } else {
      return this.executions + " exe" + ", " + this.errors + " errors" + ", avg " + (this.sum / this.executions)
          + " ms, \u03c3 " + Math.round(this.getTimeStandardDeviation()) + " [" + this.minTime + "-" + this.maxTime
          + " ms], last executed: " + le + ", last exception at " + new Date(this.lastExceptionTimestamp) + ": "
          + this.lastException.getClass().getName() + " -- " + this.compactSQL;
    }
  }

  /**
   * See Welford's online algorithm:
   * https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Online_algorithm
   * 
   * @return the standard deviation
   */
  public double getTimeStandardDeviation() {
    return this.executions < 2 ? 0
        : Math.sqrt( //
            (this.sumSQ - 1.0 * this.sum * this.sum / this.executions) //
                / //
                (this.executions - 0));
  }

}
