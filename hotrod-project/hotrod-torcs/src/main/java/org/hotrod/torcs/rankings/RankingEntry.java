package org.hotrod.torcs.rankings;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class RankingEntry {

  int pos;

  private String sql;
  private String compressedSQL;

  long minTime = 0;
  long maxTime = 0;
  long sum = 0;
  long sumSQ = 0;

  int executions = 0;
  int errors = 0;
  long lastExecuted = 0;

  Throwable lastException = null;
  long lastExceptionTimestamp = 0;

  public RankingEntry(final QueryExecution execution) {
    this.sql = execution.sql;
    this.compressedSQL = this.compressSQL(this.sql);
    apply(execution);
  }

  public void apply(final QueryExecution execution) {
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

  void setPos(int pos) {
    this.pos = pos;
  }

  // Getters

  public int getPos() {
    return pos;
  }

  public String getSQL() {
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

  public long getLastExceptionTimestamp() {
    return lastExceptionTimestamp;
  }

  public String toString() {
    String le = this.lastExecuted == 0 ? "never" : new Date(this.lastExecuted).toString();
    if (this.lastException == null) {
      return "#" + (this.pos + 1) + ": " + this.executions + " exe" + ", " + this.errors + " errors" + ", avg "
          + (this.sum / this.executions) + " ms, \u03c3 " + Math.round(this.getTimeStandardDeviation()) + " ["
          + this.minTime + "-" + this.maxTime + " ms], last executed: " + le + ", last exception: N/A -- "
          + this.compressedSQL;
    } else {
      return "#" + (this.pos + 1) + ": " + this.executions + " exe" + ", " + this.errors + " errors" + ", avg "
          + (this.sum / this.executions) + " ms, \u03c3 " + Math.round(this.getTimeStandardDeviation()) + " ["
          + this.minTime + "-" + this.maxTime + " ms], last executed: " + le + ", last exception at "
          + new Date(this.lastExceptionTimestamp) + ": " + this.lastException.getClass().getName() + " -- "
          + this.compressedSQL;
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

  private String compressSQL(final String sql) {
    return Arrays.stream(sql.split("\n")).map(l -> l.trim()).collect(Collectors.joining(" "));
  }

}
