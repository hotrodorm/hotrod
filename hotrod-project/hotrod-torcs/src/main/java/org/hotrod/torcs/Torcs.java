package org.hotrod.torcs;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.torcs.rankings.HighestResponseTimeRanking;
import org.springframework.stereotype.Component;

@Component
public class Torcs {

  private boolean active;

  private List<QuerySampleObserver> observers;
  private HighestResponseTimeRanking responseTimeRanking;

  public Torcs() {
    this.active = true;
    this.observers = new ArrayList<>();
    this.responseTimeRanking = new HighestResponseTimeRanking();
    this.observers.add(this.responseTimeRanking);
  }

  public void register(final QuerySampleObserver observer) {
    this.observers.add(observer);
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
    for (QuerySampleObserver o : this.observers) {
      o.reset();
    }
  }

  public HighestResponseTimeRanking getDefaultRanking() {
    return this.responseTimeRanking;
  }

  void record(final String sql, final int responseTime, final Throwable t) {
    if (this.active) {
      QuerySample s = new QuerySample(sql, responseTime, t);
      for (QuerySampleObserver o : this.observers) {
        if (o.isActive()) {
          o.apply(s);
        }
      }
    }
  }

}
