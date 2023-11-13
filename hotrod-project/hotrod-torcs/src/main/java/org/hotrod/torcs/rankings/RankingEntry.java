package org.hotrod.torcs.rankings;

import java.util.Date;

import org.hotrod.torcs.DataSourceReference;
import org.hotrod.torcs.QueryExecution;

public class RankingEntry {

  private DataSourceReference dsr;
  private String sql;
  private String compactSQL;

  long minTime = 0;
  long maxTime = 0;
  long sumElapsed = 0;
  long sumElapsedSQ = 0;

  int executions = 0;
  int errors = 0;
  long firstExecutionAt = 0;
  long lastExecutionAt = 0;

  Throwable lastException = null;
  long lastExceptionTimestamp = 0;

  private QueryExecution slowestExecution;
  private QueryExecution fastestExecution;
  private QueryExecution firstExecution;
  private QueryExecution lastExecution;

  public RankingEntry(final QueryExecution execution) {
    this.dsr = execution.dsr;
    this.sql = execution.sql;
    this.compactSQL = QueryExecution.compactSQL(this.sql);
    this.firstExecutionAt = System.currentTimeMillis();
    apply(execution, this.firstExecutionAt);
  }

  private RankingEntry(final RankingEntry re) {
    this.dsr = re.dsr;
    this.sql = re.sql;
    this.compactSQL = re.compactSQL;

    this.minTime = re.minTime;
    this.maxTime = re.maxTime;
    this.sumElapsed = re.sumElapsed;
    this.sumElapsedSQ = re.sumElapsedSQ;

    this.executions = re.executions;
    this.errors = re.errors;
    this.firstExecutionAt = re.firstExecutionAt;
    this.lastExecutionAt = re.lastExecutionAt;

    this.lastException = re.lastException;
    this.lastExceptionTimestamp = re.lastExceptionTimestamp;

    this.slowestExecution = re.slowestExecution;

  }

  public void apply(final QueryExecution execution) {
    apply(execution, System.currentTimeMillis());
  }

  public void apply(final QueryExecution execution, final long currentTime) {
    this.lastExecutionAt = currentTime;
    long elapsedTime = execution.getResponseTime();
    if (this.executions == 0 || elapsedTime < this.minTime) {
      this.minTime = elapsedTime;
      this.fastestExecution = execution;
    }
    if (this.executions == 0 || elapsedTime > this.maxTime) {
      this.maxTime = elapsedTime;
      this.slowestExecution = execution;
    }
    this.executions++;
    if (this.firstExecution == null) {
      this.firstExecution = execution;
    }
    this.lastExecution = execution;
    if (execution.exception != null) {
      this.errors++;
      this.lastExceptionTimestamp = this.lastExecutionAt;
      this.lastException = execution.exception;
    }
    this.sumElapsed += elapsedTime;
    this.sumElapsedSQ += elapsedTime * elapsedTime;
  }

  // Cloning

  public RankingEntry clone() {
    return new RankingEntry(this);
  }

  // Getters

  public DataSourceReference getDataSourceReference() {
    return dsr;
  }

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

  public long getFirstExecutionAt() {
    return firstExecutionAt;
  }

  public long getLastExecutionAt() {
    return lastExecutionAt;
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
    String fe = this.firstExecutionAt == 0 ? "N/A" : new Date(this.firstExecutionAt).toString();
    String le = this.lastExecutionAt == 0 ? "N/A" : new Date(this.lastExecutionAt).toString();
    return this.executions + " executions" + ", " + this.errors + " errors" + ", avg " + getAverageTime()
        + " ms, \u03c3 " + Math.round(this.getTimeStandardDeviation()) + " [" + this.minTime + "-" + this.maxTime
        + " ms], TET " + this.sumElapsed + " ms, executed: " + fe + " - " + le
        + (this.lastException == null ? ", last exception: N/A"
            : ", last exception at " + new Date(this.lastExceptionTimestamp) + ": "
                + this.lastException.getClass().getName())
        + " -- " + (this.dsr.getId() >= 0 ? "[ds" + this.dsr.getId() + "] " : "") + this.compactSQL;
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

  // Executions

  public QueryExecution getSlowestExecution() {
    return slowestExecution;
  }

  public QueryExecution getFastestExecution() {
    return fastestExecution;
  }

  public QueryExecution getFirstExecution() {
    return firstExecution;
  }

  public QueryExecution getLastExecution() {
    return lastExecution;
  }

}
