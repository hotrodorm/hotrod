package org.plan.metrics;

public abstract class Metrics {

  public abstract Double getCost();

  public abstract Double getRows();

  // Implementation classes

  public static class NoMetrics extends Metrics {

    @Override
    public Double getCost() {
      return null;
    }

    @Override
    public Double getRows() {
      return null;
    }

  }

  public static class EstimatedMetrics extends Metrics {

    private Double estimatedCost;
    private Double estimatedRows;

    EstimatedMetrics(final Double estimatedCost, final Double estimatedRows) {
      this.estimatedCost = estimatedCost;
      this.estimatedRows = estimatedRows;
      if (this.estimatedCost == null) {
        throw new IllegalArgumentException("When estimated metrics are used, the estimated cost cannot be null");
      }
    }

    public Double getEstimatedCost() {
      return estimatedCost;
    }

    public Double getEstimatedRows() {
      return estimatedRows;
    }

    @Override
    public Double getCost() {
      return this.estimatedCost;
    }

    @Override
    public Double getRows() {
      return this.estimatedRows;
    }

  }

  public static class ActualMetrics extends Metrics {

    private Double actualTimeMs;
    private Double actualRows;
    private Double actualLoops;

    ActualMetrics(final Double actualTimeMs, final Double actualRows, final Double actualLoops) {
      this.actualTimeMs = actualTimeMs;
      this.actualRows = actualRows;
      this.actualLoops = actualLoops;
      if (this.actualTimeMs == null) {
        throw new IllegalArgumentException("When actual metrics are used, the actual time [ms] cannot be null");
      }
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

    @Override
    public Double getCost() {
      return this.actualTimeMs;
    }

    @Override
    public Double getRows() {
      return this.actualRows;
    }

  }

  public static class FullMetrics extends Metrics {

    private Double estimatedCost;
    private Double estimatedRows;

    private Double actualTimeMs;
    private Double actualRows;
    private Double actualLoops;

    FullMetrics(final Double estimatedCost, final Double estimatedRows, final Double actualTimeMs,
        final Double actualRows, final Double actualLoops) {
      this.estimatedCost = estimatedCost;
      this.estimatedRows = estimatedRows;
      this.actualTimeMs = actualTimeMs;
      this.actualRows = actualRows;
      this.actualLoops = actualLoops;
      if (this.estimatedCost == null) {
        throw new IllegalArgumentException("When full metrics are used, the estimated cost cannot be null");
      }
      if (this.actualTimeMs == null) {
        throw new IllegalArgumentException("When full metrics are used, the actual time [ms] cannot be null");
      }
    }

    public Double getEstimatedCost() {
      return estimatedCost;
    }

    public Double getEstimatedRows() {
      return estimatedRows;
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

    @Override
    public Double getCost() {
      return this.actualTimeMs;
    }

    @Override
    public Double getRows() {
      return this.actualRows;
    }

  }

}
