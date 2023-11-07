package org.hotrod.torcs;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.torcs.rankings.HighestResponseTimeRanking;
import org.hotrod.torcs.rankings.Ranking;
import org.hotrod.torcs.rankings.RankingEntry;
import org.springframework.stereotype.Component;

@Component
public class Torcs {

  private boolean active;

  private List<Ranking> rankings;
  private HighestResponseTimeRanking rtRanking;

  public Torcs() {
    this.active = true;
    this.rankings = new ArrayList<>();
    this.rtRanking = new HighestResponseTimeRanking();
    this.rankings.add(this.rtRanking);
  }

  public void add(final Ranking ranking) {
    this.rankings.add(ranking);
  }

  public void activate() {
    this.active = true;
  }

  public void deactivate() {
    this.active = false;
  }

  public boolean isActive() {
    return this.active;
  }

  public void reset() {
    for (Ranking r : this.rankings) {
      r.reset();
    }
  }

  void record(final String sql, final int responseTime, final Throwable t) {
    if (this.active) {
      QuerySample q = new QuerySample(sql, responseTime, t);
      for (Ranking r : this.rankings) {
        if (r.isActive()) {
          r.consume(q);
        }
      }

//      Statement sm = this.metrics.get(sql);
//      if (sm == null) {
//        sm = new Statement(sql);
//        this.metrics.put(sql, sm);
//      }
//      sm.record(elapsedTime, t);
    }
  }

  // Get stats

  public List<RankingEntry> rankingByResponseTime() {
    return this.rtRanking.getRanking();
  }

//  public List<Statement> getByHighestResponseTime() {
//    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getMaxTime(), b.getMaxTime()))
//        .collect(Collectors.toList());
//  }
//
//  public List<Statement> getByHighestAvgResponseTime() {
//    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getAverageTime(), b.getAverageTime()))
//        .collect(Collectors.toList());
//  }
//
//  public List<Statement> getByMostExecuted() {
//    return this.metrics.values().stream()
//        .sorted((a, b) -> -Long.compare(a.getTotalExecutions(), b.getTotalExecutions())).collect(Collectors.toList());
//  }
//
//  public List<Statement> getByMostRecentlyExecuted() {
//    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getLastExecuted(), b.getLastExecuted()))
//        .collect(Collectors.toList());
//  }
//
//  public List<Statement> getByMostErrors() {
//    return this.metrics.values().stream()
//        .sorted((a, b) -> -Long.compare(a.getExecutionErrors(), b.getExecutionErrors())).collect(Collectors.toList());
//  }
//
//  public List<Statement> getErrorsByMostRecent() {
//    return this.metrics.values().stream().filter(a -> a.getExecutionErrors() > 0)
//        .sorted((a, b) -> -Long.compare(a.getLastExecuted(), b.getLastExecuted())).collect(Collectors.toList());
//  }
//
//  // Get stats summary
//
//  public String summaryByHighestResponseTime() {
//    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getMaxTime(), b.getMaxTime()))
//        .map(s -> "> " + s).collect(ListCollector.concat("\n"));
//  }
//
//  public String summaryByHighestAvgResponseTime() {
//    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getAverageTime(), b.getAverageTime()))
//        .map(s -> "> " + s).collect(ListCollector.concat("\n"));
//  }
//
//  public String summaryByMostExecuted() {
//    return this.metrics.values().stream()
//        .sorted((a, b) -> -Long.compare(a.getTotalExecutions(), b.getTotalExecutions())).map(s -> "> " + s)
//        .collect(ListCollector.concat("\n"));
//  }
//
//  public String summaryByMostRecentlyExecuted() {
//    return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getLastExecuted(), b.getLastExecuted()))
//        .map(s -> "> " + s).collect(ListCollector.concat("\n"));
//  }
//
//  public String summaryByMostErrors() {
//    return this.metrics.values().stream()
//        .sorted((a, b) -> -Long.compare(a.getExecutionErrors(), b.getExecutionErrors())).map(s -> "> " + s)
//        .collect(ListCollector.concat("\n"));
//  }
//
//  public String summaryErrorsByMostRecent() {
//    return this.metrics.values().stream().filter(a -> a.getExecutionErrors() > 0)
//        .sorted((a, b) -> -Long.compare(a.getLastExecuted(), b.getLastExecuted())).map(s -> "> " + s)
//        .collect(ListCollector.concat("\n"));
//  }

}
