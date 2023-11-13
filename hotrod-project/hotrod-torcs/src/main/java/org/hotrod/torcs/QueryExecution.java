package org.hotrod.torcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.hotrod.torcs.setters.Setter;

public class QueryExecution {

  public DataSourceReference dsr;
  public String sql;
  public Map<Integer, Setter> setters;
  public int responseTime;
  public Throwable exception;

  public QueryExecution(final DataSourceReference dsr, final String sql, final Map<Integer, Setter> setters,
      final int responseTime, final Throwable exception) {
    this.dsr = dsr;
    this.sql = sql;
    this.setters = setters;
    this.responseTime = responseTime;
    this.exception = exception;
  }

  public DataSourceReference getDataSourceReference() {
    return dsr;
  }

  public String getSQL() {
    return sql;
  }

  public Collection<Setter> getSetters() {
    return new ArrayList<>(setters.values());
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
