package org.hotrod.torcs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.nocrala.tools.lang.collector.listcollector.ListCollector;
import org.springframework.stereotype.Component;

@Component
public class TorcsMetrics {

  private boolean active = true;
  private Map<String, Statement> metrics = new HashMap<String, Statement>();

  public void activate() {
    this.active = true;
  }

  public void deactivate() {
    this.active = false;
  }

  public boolean isActive() {
    return this.active;
  }

  void record(final String sql, final long elapsedTime, final Throwable t) {
    if (this.active) {
      recordExecution(sql, elapsedTime, t);
    }

  }

  private synchronized void recordExecution(final String sql, final long elapsedTime, final Throwable t) {
    Statement sm = this.metrics.get(sql);
    if (sm == null) {
      sm = new Statement(sql);
      this.metrics.put(sql, sm);
    }
    sm.record(elapsedTime, t);
  }

  // Get stats

  public List<Statement> getByHighestResponseTime() {
    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getMaxTime(), b.getMaxTime()))
        .collect(Collectors.toList());
  }

  public List<Statement> getByHighestAvgResponseTime() {
    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getAverageTime(), b.getAverageTime()))
        .collect(Collectors.toList());
  }

  public List<Statement> getByMostExecuted() {
    return this.metrics.values().stream()
        .sorted((a, b) -> -Long.compare(a.getTotalExecutions(), b.getTotalExecutions())).collect(Collectors.toList());
  }

  public List<Statement> getByMostRecentlyExecuted() {
    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getLastExecuted(), b.getLastExecuted()))
        .collect(Collectors.toList());
  }

  public List<Statement> getByMostErrors() {
    return this.metrics.values().stream()
        .sorted((a, b) -> -Long.compare(a.getExecutionErrors(), b.getExecutionErrors())).collect(Collectors.toList());
  }

  public List<Statement> getErrorsByMostRecent() {
    return this.metrics.values().stream().filter(a -> a.getExecutionErrors() > 0)
        .sorted((a, b) -> -Long.compare(a.getLastExecuted(), b.getLastExecuted())).collect(Collectors.toList());
  }

  // Get stats summary

  public String summaryByHighestResponseTime() {
    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getMaxTime(), b.getMaxTime()))
        .map(s -> "> " + s).collect(ListCollector.concat("\n"));
  }

  public String summaryByHighestAvgResponseTime() {
    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getAverageTime(), b.getAverageTime()))
        .map(s -> "> " + s).collect(ListCollector.concat("\n"));
  }

  public String summaryByMostExecuted() {
    return this.metrics.values().stream()
        .sorted((a, b) -> -Long.compare(a.getTotalExecutions(), b.getTotalExecutions())).map(s -> "> " + s)
        .collect(ListCollector.concat("\n"));
  }

  public String summaryByMostRecentlyExecuted() {
    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getLastExecuted(), b.getLastExecuted()))
        .map(s -> "> " + s).collect(ListCollector.concat("\n"));
  }

  public String summaryByMostErrors() {
    return this.metrics.values().stream()
        .sorted((a, b) -> -Long.compare(a.getExecutionErrors(), b.getExecutionErrors())).map(s -> "> " + s)
        .collect(ListCollector.concat("\n"));
  }

  public String summaryErrorsByMostRecent() {
    return this.metrics.values().stream().filter(a -> a.getExecutionErrors() > 0)
        .sorted((a, b) -> -Long.compare(a.getLastExecuted(), b.getLastExecuted())).map(s -> "> " + s)
        .collect(ListCollector.concat("\n"));
  }

}
