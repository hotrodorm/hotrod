package org.plan.metrics;

public class ActualMetrics {

  private Double actualTimeMs;
  private Double actualRows;
  private Double actualLoops;

  public ActualMetrics(final Double actualTimeMs, final Double actualRows, final Double actualLoops) {
    this.actualTimeMs = actualTimeMs;
    this.actualRows = actualRows;
    this.actualLoops = actualLoops;
  }

  public Double getActualTimeMs() {
    return actualTimeMs;
  }

  public Double getActualRows() {
    return actualRows;
  }

  public Double getActualLoops() {
    return actualLoops;
  }

}
