package com.myapp1.aopsqlmetrics;

import java.util.HashMap;
import java.util.Map;

import org.nocrala.tools.lang.collector.listcollector.ListCollector;
import org.springframework.stereotype.Component;

@Component("sqlMetrics")
public class SQLMetrics {

  private Map<String, StatementMetrics> metrics = new HashMap<String, StatementMetrics>();

  public void record(final String sql, final long elapsedTime, final boolean succeeded) {
    StatementMetrics sm = this.metrics.get(sql);
    if (sm == null) {
      sm = new StatementMetrics(sql);
      this.metrics.put(sql, sm);
    }
    sm.record(elapsedTime, succeeded);
  }

  public String render() {
    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getTimeAverage(), b.getTimeAverage()))
        .map(s -> "> " + s).collect(ListCollector.concat("\n"));
  }

}
