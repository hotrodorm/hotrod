package org.hotrod.torcs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.hotrod.torcs.plan.PlanRetriever;
import org.hotrod.torcs.plan.PlanRetrieverFactory.UnsupportedTorcsDatabaseException;
import org.hotrod.torcs.rankings.HighestResponseTimeRanking;
import org.hotrod.torcs.setters.Setter;
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
  private int restartPeriodInMinutes;

  public Torcs() {

    this.active = true;
    this.observers = new ArrayList<>();
    this.responseTimeRanking = new HighestResponseTimeRanking();
    this.observers.add(this.responseTimeRanking);
    this.scheduleService = Executors.newScheduledThreadPool(1);

    this.restartPeriodInMinutes = DEFAULT_RESET_PERIOD_IN_MINUTES;
    scheduleRestart();

  }

  public void register(final QueryExecutionObserver observer) {
    this.observers.add(observer);
  }

  public void setRestartPeriodInMinutes(final int minutes) {
    if (minutes < MIN_RESET_PERIOD_IN_MINUTES) {
      throw new RuntimeException("The restart period (in minutes) must be greater or equal to "
          + MIN_RESET_PERIOD_IN_MINUTES + " but it's " + minutes + ".");
    }
    if (minutes > MAX_RESET_PERIOD_IN_MINUTES) {
      throw new RuntimeException("The restart period (in minutes) must be less than or equal to "
          + MAX_RESET_PERIOD_IN_MINUTES + " but it's " + minutes + ".");
    }
    this.restartPeriodInMinutes = minutes;
    scheduleRestart();
  }

  private void scheduleRestart() {
    this.scheduleService.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        reset();
      }
    }, this.restartPeriodInMinutes, this.restartPeriodInMinutes, TimeUnit.MINUTES);
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
      o.restart();
    }
  }

  public HighestResponseTimeRanking getDefaultRanking() {
    return this.responseTimeRanking;
  }

  void record(final DataSourceReference dsr, final String sql, final Map<Integer, Setter> setters,
      final int responseTime, final Throwable t) {
    if (this.active) {
      QueryExecution qe = new QueryExecution(dsr, sql, setters, responseTime, t);
      for (QueryExecutionObserver o : this.observers) {
        if (o.isActive()) {
          o.apply(qe);
        }
      }
    }
  }

  // Generic Execution Plan

  public String getEstimatedExecutionPlan(final QueryExecution execution)
      throws SQLException, UnsupportedTorcsDatabaseException {
    PlanRetriever r = execution.getDataSourceReference().getPlanRetriever();
    return r.getEstimatedExecutionPlan(execution);
  }

}
