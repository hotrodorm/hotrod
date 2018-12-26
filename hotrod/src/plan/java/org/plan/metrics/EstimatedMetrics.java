package org.plan.metrics;

public class EstimatedMetrics {

  private Double estimatedCost;
  private Double estimatedRows;

  public EstimatedMetrics(final Double estimatedCost, final Double estimatedRows) {
    this.estimatedCost = estimatedCost;
    this.estimatedRows = estimatedRows;
  }

  public Double getEstimatedCost() {
    return estimatedCost;
  }

  public Double getEstimatedRows() {
    return estimatedRows;
  }

}
