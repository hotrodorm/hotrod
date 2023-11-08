package org.hotrod.torcs.rankings;

import java.util.Date;

import org.hotrod.torcs.QuerySample;

public class RankingEntry {

  private String sql;
  private String compactSQL;

  long minTime = 0;
  long maxTime = 0;
  long sumElapsed = 0;
  long sumElapsedSQ = 0;

  int executions = 0;
  int errors = 0;
  long firstExecution = 0;
  long lastExecution = 0;

  Throwable lastException = null;
  long lastExceptionTimestamp = 0;

  public RankingEntry(final QuerySample sample) {
    this.sql = sample.sql;
    this.compactSQL = QuerySample.compactSQL(this.sql);
    this.firstExecution = System.currentTimeMillis();
    apply(sample, this.firstExecution);
  }

  private RankingEntry(final RankingEntry re) {
    this.sql = re.sql;
    this.compactSQL = re.compactSQL;

    this.minTime = re.minTime;
    this.maxTime = re.maxTime;
    this.sumElapsed = re.sumElapsed;
    this.sumElapsedSQ = re.sumElapsedSQ;

    this.executions = re.executions;
    this.errors = re.errors;
    this.firstExecution = re.firstExecution;
    this.lastExecution = re.lastExecution;

    this.lastException = re.lastException;
    this.lastExceptionTimestamp = re.lastExceptionTimestamp;

  }

  public void apply(final QuerySample sample) {
    apply(sample, System.currentTimeMillis());
  }

  public void apply(final QuerySample sample, final long currentTime) {
    this.lastExecution = currentTime;
    long elapsedTime = sample.getResponseTime();
    if (this.executions == 0 || elapsedTime < this.minTime) {
      this.minTime = elapsedTime;
    }
    if (this.executions == 0 || elapsedTime > this.maxTime) {
      this.maxTime = elapsedTime;
    }
    this.executions++;
    if (sample.exception != null) {
      this.errors++;
      this.lastExceptionTimestamp = this.lastExecution;
      this.lastException = sample.exception;
    }
    this.sumElapsed += elapsedTime;
    this.sumElapsedSQ += elapsedTime * elapsedTime;
  }

  // Cloning

  public RankingEntry clone() {
    return new RankingEntry(this);
  }

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

  public long getSumElapsed() {
    return sumElapsed;
  }

  public long getSumElapsedSQ() {
    return sumElapsedSQ;
  }

  public int getExecutions() {
    return executions;
  }

  public int getErrors() {
    return errors;
  }

  public long getFirstExecution() {
    return firstExecution;
  }

  public long getLastExecution() {
    return lastExecution;
  }

  public Throwable getLastException() {
    return lastException;
  }

  public long getLastExceptionTimestamp() {
    return lastExceptionTimestamp;
  }

  public long getAverageTime() {
    int successfulExecutions = this.executions - this.errors;
    return successfulExecutions > 0 ? (this.sumElapsed / successfulExecutions) : -1;
  }

  public long getTotalElapsedTime() {
    return this.sumElapsed;
  }

  public String toString() {
    String fe = this.firstExecution == 0 ? "N/A" : new Date(this.firstExecution).toString();
    String le = this.lastExecution == 0 ? "N/A" : new Date(this.lastExecution).toString();
    return this.executions + " executions" + ", " + this.errors + " errors" + ", avg " + getAverageTime()
        + " ms, \u03c3 " + Math.round(this.getTimeStandardDeviation()) + " [" + this.minTime + "-" + this.maxTime
        + " ms], TET " + this.sumElapsed + " ms, executed: " + fe + " - " + le
        + (this.lastException == null ? ", last exception: N/A"
            : ", last exception at " + new Date(this.lastExceptionTimestamp) + ": "
                + this.lastException.getClass().getName())
        + " -- " + this.compactSQL;
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
            (this.sumElapsedSQ - 1.0 * this.sumElapsed * this.sumElapsed / this.executions) //
                / //
                (this.executions - 0));
  }

}
