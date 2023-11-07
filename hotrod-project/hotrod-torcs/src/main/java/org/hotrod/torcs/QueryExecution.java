package org.hotrod.torcs;

public class QueryExecution {

  public String sql;
  public int responseTime;
  public Throwable exception;

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
