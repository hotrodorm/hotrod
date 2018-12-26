package org.plan.renderer.text.cost;

import java.text.DecimalFormat;

import org.plan.metrics.ActualMetrics;
import org.plan.metrics.EstimatedMetrics;

public class ActualCostRenderer extends CostRenderer {

  public ActualCostRenderer(final double actualFullCost) {
    if (actualFullCost >= 1000000000) {
      super.multiplier = 1000000.0;
      super.df = new DecimalFormat("#0");
    } else if (actualFullCost >= 1000) {
      super.multiplier = 1.0;
      super.df = new DecimalFormat("#0");
    } else if (actualFullCost >= 1000) {
      super.multiplier = 1.0;
      super.df = new DecimalFormat("#0");
    } else if (actualFullCost >= 1000) {
      super.multiplier = 1.0;
      super.df = new DecimalFormat("#0");
    } else if (actualFullCost >= 1000) {
      super.multiplier = 1.0;
      super.df = new DecimalFormat("#0");
    } else if (actualFullCost >= 1000) {
      super.multiplier = 1.0;
      super.df = new DecimalFormat("#0");
    } else if (actualFullCost >= 1000) {
      super.multiplier = 1.0;
      super.df = new DecimalFormat("#0");
    }
  }

  @Override
  public String renderCost(final EstimatedMetrics estimatedMetrics, final ActualMetrics actualMetrics) {
    // TODO Auto-generated method stub
    return null;
  }

}
