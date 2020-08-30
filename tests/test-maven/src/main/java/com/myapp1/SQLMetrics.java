package com.myapp1;

import java.util.HashMap;
import java.util.Map;

import org.nocrala.tools.lang.collector.listcollector.ListCollector;

public class SQLMetrics {

  private static Map<String, StatementMetrics> metrics = new HashMap<String, StatementMetrics>();

  public static void record(final String sql, final long elapsedTime, final boolean succeeded) {
    StatementMetrics sm = metrics.get(sql);
    if (sm == null) {
      sm = new StatementMetrics(sql);
      metrics.put(sql, sm);
    }
    sm.record(elapsedTime, succeeded);
//    System.out.println("[SQL] " + elapsedTime + " ms - " + (succeeded ? "OK" : "ERROR") + " - " + compact(sql));
  }

  public static String render() {
    return metrics.values().stream().sorted((a, b) -> Long.compare(a.getExecutions(), b.getExecutions()))
        .map(s -> ">> " + s).collect(ListCollector.concat("\n"));
  }

}
