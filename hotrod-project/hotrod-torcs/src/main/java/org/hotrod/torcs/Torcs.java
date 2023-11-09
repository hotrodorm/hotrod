package org.hotrod.torcs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.hotrod.torcs.rankings.HighestResponseTimeRanking;
import org.springframework.stereotype.Component;

@Component
public class Torcs {

  private static final int DEFAULT_RESET_PERIOD_IN_MINUTES = 60;
  private static final int MIN_RESET_PERIOD_IN_MINUTES = 1;
  private static final int MAX_RESET_PERIOD_IN_MINUTES = 60 * 24 * 366;

  private boolean active;

  private List<QueryExecutionObserver> observers;
  private HighestResponseTimeRanking responseTimeRanking;

  private ScheduledExecutorService scheduleService;
  private int resetPeriodInMinutes;

  public Torcs() {

    this.active = true;
    this.observers = new ArrayList<>();
    this.responseTimeRanking = new HighestResponseTimeRanking();
    this.observers.add(this.responseTimeRanking);
    this.scheduleService = Executors.newScheduledThreadPool(1);

    this.resetPeriodInMinutes = DEFAULT_RESET_PERIOD_IN_MINUTES;
    scheduleReset();

  }

  public void register(final QueryExecutionObserver observer) {
    this.observers.add(observer);
  }

  public void setResetPeriodInMinutes(final int minutes) {
    if (minutes < MIN_RESET_PERIOD_IN_MINUTES) {
      throw new RuntimeException("The reset period (in minutes) must be greater or equal to "
          + MIN_RESET_PERIOD_IN_MINUTES + " but it's " + minutes + ".");
    }
    if (minutes > MAX_RESET_PERIOD_IN_MINUTES) {
      throw new RuntimeException("The reset period (in minutes) must be less than or equal to "
          + MAX_RESET_PERIOD_IN_MINUTES + " but it's " + minutes + ".");
    }
    this.resetPeriodInMinutes = minutes;
    scheduleReset();
  }

  private void scheduleReset() {
    this.scheduleService.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        reset();
      }
    }, this.resetPeriodInMinutes, this.resetPeriodInMinutes, TimeUnit.MINUTES);
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
    System.out.println("[TORCS Reset]");
    for (QueryExecutionObserver o : this.observers) {
      o.reset();
    }
  }

  public HighestResponseTimeRanking getDefaultRanking() {
    return this.responseTimeRanking;
  }

  void record(final String sql, final int responseTime, final Throwable t) {
    if (this.active) {
      QueryExecution s = new QueryExecution(sql, responseTime, t);
      for (QueryExecutionObserver o : this.observers) {
        if (o.isActive()) {
          o.apply(s);
        }
      }
    }
  }

}
