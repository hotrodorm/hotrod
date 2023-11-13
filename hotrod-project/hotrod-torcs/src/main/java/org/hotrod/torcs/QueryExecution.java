package org.hotrod.torcs;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.sql.DataSource;

public class QueryExecution {

  public DataSource ds;
  public String sql;
  public int responseTime;
  public Throwable exception;

  public QueryExecution(final DataSource ds, final String sql, final int responseTime, final Throwable exception) {
    this.ds = ds;
    this.sql = sql;
    this.responseTime = responseTime;
    this.exception = exception;
  }

  public DataSource getDataSource() {
    return ds;
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

  public static String compactSQL(final String sql) {
    return Arrays.stream(sql.split("\n")).map(l -> l.trim()).collect(Collectors.joining(" "));
  }

}
