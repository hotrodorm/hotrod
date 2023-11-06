package org.hotrod.torcs.rankings;

public class QueryExecution {

  protected String sql;
  protected int responseTime;
  protected Throwable exception;

  public QueryExecution(final String sql, final int responseTime, final Throwable exception) {
    this.sql = sql;
    this.responseTime = responseTime;
    this.exception = exception;
  }

  public String getSQL() {
    return sql;
  }

  public int getResponseTime() {
    return responseTime;
  }

  public Throwable getException() {
    return exception;
  }

}
