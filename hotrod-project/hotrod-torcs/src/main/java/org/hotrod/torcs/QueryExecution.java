package org.hotrod.torcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.hotrod.torcs.setters.index.IndexSetter;
import org.hotrod.torcs.setters.name.NameSetter;

public class QueryExecution {

  public DataSourceReference dsr;
  public String sql;
  public Map<Integer, IndexSetter> indexSetters;
  public Map<String, NameSetter> nameSetters;
  public int responseTime;
  public Throwable exception;

  public QueryExecution(final DataSourceReference dsr, final String sql, final Map<Integer, IndexSetter> indexSetters,
      final Map<String, NameSetter> nameSetters, final int responseTime, final Throwable exception) {
    this.dsr = dsr;
    this.sql = sql;
    this.indexSetters = indexSetters == null ? new HashMap<>() : indexSetters;
    this.nameSetters = nameSetters == null ? new HashMap<>() : nameSetters;
    this.responseTime = responseTime;
    this.exception = exception;
  }

  public DataSourceReference getDataSourceReference() {
    return dsr;
  }

  public String getSQL() {
    return sql;
  }

  public Collection<IndexSetter> getIndexSetters() {
    return new ArrayList<>(indexSetters.values());
  }

  public Collection<NameSetter> getNameSetters() {
    return new ArrayList<>(nameSetters.values());
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
