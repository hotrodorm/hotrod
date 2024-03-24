package org.hotrod.torcs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hotrod.torcs.plan.CouldNotRetrievePlanException;
import org.hotrod.torcs.plan.LOBParameterNotAllowedException;
import org.hotrod.torcs.plan.ParameterAlreadyConsumedException;
import org.hotrod.torcs.plan.PlanRetriever;
import org.hotrod.torcs.plan.PlanRetrieverFactory.UnsupportedTorcsDatabaseException;
import org.hotrod.torcs.rankings.HighestResponseTimeRanking;
import org.hotrod.torcs.setters.index.IndexSetter;
import org.hotrod.torcs.setters.name.NameSetter;
import org.springframework.stereotype.Component;

@Component
public class Torcs {

  private static final Logger log = Logger.getLogger(Torcs.class.getName());

  private static final int DEFAULT_RESET_PERIOD_IN_MINUTES = 60;
  private static final int MIN_RESET_PERIOD_IN_MINUTES = 1;
  private static final int MAX_RESET_PERIOD_IN_MINUTES = 60 * 24 * 366;

  private boolean active;

  private List<QueryExecutionObserver> observers;
  private HighestResponseTimeRanking responseTimeRanking;

  private ScheduledExecutorService scheduleService;
  private int resetPeriodInMinutes;

  private boolean lobsAllowedInPlans = false;

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
        try {
          reset();
        } catch (Throwable e) {
          log.log(Level.SEVERE, "Could not reset Torcs observers", e);
        }
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

  public void allowLOBsInPlan(final boolean allow) {
    this.lobsAllowedInPlans = allow;
  }

  public boolean isLobsAllowedInPlans() {
    return lobsAllowedInPlans;
  }

  public void setLobsAllowedInPlans(boolean lobsAllowedInPlans) {
    this.lobsAllowedInPlans = lobsAllowedInPlans;
  }

  public void reset() {
    log.info("Resetting observers.");
    for (QueryExecutionObserver o : this.observers) {
      o.reset();
    }
  }

  public HighestResponseTimeRanking getDefaultRanking() {
    return this.responseTimeRanking;
  }

  public void record(final DataSourceReference dsr, final String sql, final Map<Integer, IndexSetter> indexSetters,
      final Map<String, NameSetter> nameSetters, final int responseTime, final Throwable t) {
    if (this.active) {
      QueryExecution qe = new QueryExecution(dsr, sql, indexSetters, nameSetters, responseTime, t);
      for (QueryExecutionObserver o : this.observers) {
        if (o.isActive()) {
          o.apply(qe);
        }
      }
    }
  }

  // Generic Execution Plan

  public String getEstimatedExecutionPlan(final QueryExecution execution)
      throws SQLException, UnsupportedTorcsDatabaseException, CouldNotRetrievePlanException {
    if (execution.wasConsumableParameterDetected()) {
      throw new ParameterAlreadyConsumedException(
          "The plan could not be retrieved since this query uses a consumable parameter that was already consumed ("
              + setterName(execution.getConsummableTypeSetter()) + ")");
    }
    if (!this.lobsAllowedInPlans && execution.wasLOBParameterDetected()) {
      throw new LOBParameterNotAllowedException(
          "The plan could not be retrieved since this query uses a LOB parameter ("
              + setterName(execution.getLobTypeSetter())
              + ") and LOBs are currently disabled in Torcs. You can enable LOBs in the Torcs bean; "
              + "consider that enabling them can increase the memory consumption of Torcs.");
    }
    PlanRetriever r = execution.getDataSourceReference().getPlanRetriever();
    return r.getEstimatedExecutionPlan(execution);
  }

  public String getEstimatedExecutionPlan(final QueryExecution execution, final int variation)
      throws SQLException, UnsupportedTorcsDatabaseException, CouldNotRetrievePlanException {
    if (execution.wasConsumableParameterDetected()) {
      throw new ParameterAlreadyConsumedException(
          "The plan could not be retrieved since this query uses a consumable parameter that was already consumed ("
              + setterName(execution.getConsummableTypeSetter()) + ")");
    }
    if (!this.lobsAllowedInPlans && execution.wasLOBParameterDetected()) {
      throw new LOBParameterNotAllowedException(
          "The plan could not be retrieved since this query uses a LOB parameter ("
              + setterName(execution.getLobTypeSetter())
              + ") and LOBs are currently disabled in Torcs. You can enable LOBs in the Torcs bean; "
              + "consider that enabling them can increase the memory consumption of Torcs.");
    }
    PlanRetriever r = execution.getDataSourceReference().getPlanRetriever();
    return r.getEstimatedExecutionPlan(execution, variation);
  }

  private String setterName(final String className) {
    if (className == null) {
      return null;
    }
    String n = className.startsWith("Name") ? className.substring("Name".length()) : className;
    n = n.endsWith("Setter") ? n.substring(0, n.length() - "Setter".length()) : n;
    return "set" + n;
  }

}
