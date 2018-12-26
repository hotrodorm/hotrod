package org.plan.renderer.text.cost;

import java.text.DecimalFormat;

import org.plan.metrics.ActualMetrics;
import org.plan.metrics.EstimatedMetrics;

public abstract class CostRenderer {

  protected double multiplier;
  protected DecimalFormat df;

  public abstract String renderCost(final EstimatedMetrics estimatedMetrics, final ActualMetrics actualMetrics);

}
