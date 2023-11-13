package org.hotrod.torcs;

import java.util.Arrays;
import java.util.stream.Collectors;

public class QueryExecution {

  public DataSourceReference dsr;
  public String sql;
  public int responseTime;
  public Throwable exception;

  public QueryExecution(final DataSourceReference dsr, final String sql, final int responseTime,
      final Throwable exception) {
    this.dsr = dsr;
    this.sql = sql;
    this.responseTime = responseTime;
    this.exception = exception;
  }

  public DataSourceReference getDataSourceReference() {
    return dsr;
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
