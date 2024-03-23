package org.hotrod.torcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.torcs.setters.index.IndexSetter;
import org.hotrod.torcs.setters.name.NameSetter;

public class QueryExecution {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(QueryExecution.class.getName());

  public DataSourceReference dsr;
  public String sql;
  public Map<Integer, IndexSetter> indexSetters;
  public Map<String, NameSetter> nameSetters;
  public int responseTime;
  public Throwable exception;
  private boolean wasConsumableParameterDetected;
  private String consumableTypeSetter;
  private boolean wasLOBParameterDetected;
  private String lobTypeSetter;

  public QueryExecution(final DataSourceReference dsr, final String sql, final Map<Integer, IndexSetter> indexSetters,
      final Map<String, NameSetter> nameSetters, final int responseTime, final Throwable exception) {

    this.dsr = dsr;
    this.sql = sql;
    this.indexSetters = indexSetters == null ? new HashMap<>() : indexSetters;
    this.nameSetters = nameSetters == null ? new HashMap<>() : nameSetters;
    this.responseTime = responseTime;
    this.exception = exception;

    this.wasConsumableParameterDetected = false;
    this.consumableTypeSetter = null;

    this.wasLOBParameterDetected = false;
    this.lobTypeSetter = null;

    for (IndexSetter s : indexSetters.values()) {
//      log.info(">>> setter: " + s.getClass().getSimpleName());
      if (s.isConsumableParameter()) {
        this.wasConsumableParameterDetected = true;
        this.consumableTypeSetter = s.getClass().getSimpleName();

      }
      if (s.isLOBParameter()) {
        this.wasLOBParameterDetected = true;
        this.lobTypeSetter = s.getClass().getSimpleName();
      }
    }
    for (NameSetter s : nameSetters.values()) {
//      log.info(">>> setter: " + s.getClass().getSimpleName());
      if (s.isConsumableParameter()) {
        this.wasConsumableParameterDetected = true;
        this.consumableTypeSetter = s.getClass().getSimpleName();
      }
      if (s.isLOBParameter()) {
        this.wasLOBParameterDetected = true;
        this.lobTypeSetter = s.getClass().getSimpleName();
      }
    }

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

  public boolean wasConsumableParameterDetected() {
    return wasConsumableParameterDetected;
  }

  public boolean wasLOBParameterDetected() {
    return wasLOBParameterDetected;
  }

  public String getConsummableTypeSetter() {
    return consumableTypeSetter;
  }

  public String getLobTypeSetter() {
    return lobTypeSetter;
  }

  public static String compactSQL(final String sql) {
    return Arrays.stream(sql.split("\n")).map(l -> l.trim()).collect(Collectors.joining(" "));
  }

}
