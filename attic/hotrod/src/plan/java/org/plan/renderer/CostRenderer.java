package org.plan.renderer;

import java.text.DecimalFormat;

import org.plan.metrics.Metrics;
import org.plan.metrics.MetricsFactory;

public abstract class CostRenderer {

  protected static final DecimalFormat PERCENT_FORMATTER = new DecimalFormat("#0.0%");

  protected static final double K = 1000.0;
  protected static final double M = 1000000.0;

  protected Double fullCost;
  protected boolean showPercentageCost;

  protected NumberFormatter costFormatter;

  protected CostRenderer(final Double fullCost, final boolean showPercentageCost) {
    this.fullCost = fullCost;
    this.showPercentageCost = showPercentageCost;
  }

  public static CostRenderer instantiate(final MetricsFactory metricsFactory, final Double fullCost,
      final boolean showPercentageCost) {
    if (metricsFactory.includesEstimatedMetrics()) {
      return metricsFactory.includesActualMetrics() ? new ActualCostRenderer(fullCost, showPercentageCost)
          : new EstimatedCostRenderer(fullCost, showPercentageCost);
    } else {
      return metricsFactory.includesActualMetrics() ? new ActualCostRenderer(fullCost, showPercentageCost)
          : new NoCostRenderer(fullCost, showPercentageCost);
    }
  }

  public abstract Scalar renderCost(final Metrics metrics);

  public abstract String renderRows(final Metrics metrics);

  // Implementations

  public static class EstimatedCostRenderer extends CostRenderer {

    // 9999M
    // 100M --
    // 99.9M
    // 10.0M --
    // 9999k
    // 100k --
    // 99.9k
    // 10.0k --
    // 9999
    // 100 --
    // 99.9
    // 10.0 --
    // 9.99
    // 1.00 --
    // 0.9999

    public EstimatedCostRenderer(final Double fullCost, final boolean showPercentageCost) {
      super(fullCost, showPercentageCost);

      System.out.println(">>> fullCost=" + fullCost);

      // cost

      if (fullCost >= 100 * M) {
        this.costFormatter = new NumberFormatter(1.0 / M, new DecimalFormat("#0'M'"));
      } else if (fullCost >= 10 * M) {
        this.costFormatter = new NumberFormatter(1.0 / M, new DecimalFormat("#0.0'M'"));
      } else if (fullCost >= 100 * K) {
        this.costFormatter = new NumberFormatter(1.0 / K, new DecimalFormat("#0'k'"));
      } else if (fullCost >= 10 * K) {
        this.costFormatter = new NumberFormatter(1.0 / K, new DecimalFormat("#0.0'k'"));
      } else if (fullCost >= 100) {
        this.costFormatter = new NumberFormatter(1.0, new DecimalFormat("#0"));
      } else if (fullCost >= 10.0) {
        this.costFormatter = new NumberFormatter(1.0, new DecimalFormat("#0.0"));
      } else if (fullCost >= 1.0) {
        this.costFormatter = new NumberFormatter(1.0, new DecimalFormat("#0.00"));
      } else {
        this.costFormatter = new NumberFormatter(1.0, new DecimalFormat("#0.0000"));
      }

      // rows

    }

    @Override
    public Scalar renderCost(final Metrics metrics) {
      if (this.showPercentageCost) {
        double ratio = metrics.getCost() / this.fullCost;
        return new Scalar(PERCENT_FORMATTER.format(ratio), null);
      } else {
        return new Scalar(this.costFormatter.format(metrics.getCost()), null);
      }
    }

    @Override
    public String renderRows(final Metrics metrics) {
      Double rows = metrics.getRows();
      if (rows == null) {
        return null;
      }
      NumberFormatter f = super.getSimpleFormatter(rows);
      return f.format(rows);
    }

  }

  public static class ActualCostRenderer extends CostRenderer {

    // 100 --
    // 99.9
    // 10.0 --
    // 9.99
    // 1.00 --
    // 0.9999
    // 0.0100 --
    // 9.99 µs
    // 1.00 µs --
    // 0.9999 µs

    private String unit;

    public ActualCostRenderer(final Double fullCost, final boolean showPercentageCost) {
      super(fullCost, showPercentageCost);
      if (fullCost >= 100) {
        this.costFormatter = new NumberFormatter(1.0, new DecimalFormat("#0"));
        this.unit = "ms";
      } else if (fullCost >= 10.0) {
        this.costFormatter = new NumberFormatter(1.0, new DecimalFormat("#0.0"));
        this.unit = "ms";
      } else if (fullCost >= 1.0) {
        this.costFormatter = new NumberFormatter(1.0, new DecimalFormat("#0.00"));
        this.unit = "ms";
      } else if (fullCost >= 0.01) {
        this.costFormatter = new NumberFormatter(1000.0, new DecimalFormat("#0"));
        this.unit = "µs";
      } else if (fullCost >= 0.001) {
        this.costFormatter = new NumberFormatter(1000.0, new DecimalFormat("#0.00"));
        this.unit = "µs";
      } else {
        this.costFormatter = new NumberFormatter(1000.0, new DecimalFormat("#0.0000"));
        this.unit = "µs";
      }
    }

    @Override
    public Scalar renderCost(final Metrics metrics) {
      if (this.showPercentageCost) {
        double ratio = metrics.getCost() / this.fullCost;
        return new Scalar(PERCENT_FORMATTER.format(ratio), null);
      } else {
        return new Scalar(this.costFormatter.format(metrics.getCost()), this.unit);
      }
    }

    @Override
    public String renderRows(final Metrics metrics) {
      Double rows = metrics.getRows();
      if (rows == null) {
        return null;
      }
      NumberFormatter f = super.getSimpleFormatter(rows);
      return f.format(rows);
    }

  }

  public static class NoCostRenderer extends CostRenderer {

    public NoCostRenderer(final Double fullCost, final boolean showPercentageCost) {
      super(fullCost, showPercentageCost);
    }

    @Override
    public Scalar renderCost(final Metrics metrics) {
      return null;
    }

    @Override
    public String renderRows(final Metrics metrics) {
      return null;
    }

  }

  protected NumberFormatter getSimpleFormatter(final Double fullCost) {
    if (fullCost >= 100 * M) {
      return new NumberFormatter(1.0 / M, new DecimalFormat("#0'M'"));
    } else if (fullCost >= 10 * M) {
      return new NumberFormatter(1.0 / M, new DecimalFormat("#0.0'M'"));
    } else if (fullCost >= 100 * K) {
      return new NumberFormatter(1.0 / K, new DecimalFormat("#0'k'"));
    } else if (fullCost >= 10 * K) {
      return new NumberFormatter(1.0 / K, new DecimalFormat("#0.0'k'"));
    } else if (fullCost >= 100) {
      return new NumberFormatter(1.0, new DecimalFormat("#0"));
    } else if (fullCost >= 10.0) {
      return new NumberFormatter(1.0, new DecimalFormat("#0.0"));
    } else if (fullCost >= 1.0) {
      return new NumberFormatter(1.0, new DecimalFormat("#0.00"));
    } else {
      return new NumberFormatter(1.0, new DecimalFormat("#0.0000"));
    }

  }

  public static class Scalar {

    private String formatterNumber;
    private String unit;

    public Scalar(final String formatterNumber, final String unit) {
      this.formatterNumber = formatterNumber;
      this.unit = unit;
    }

    public String getFormatterNumber() {
      return formatterNumber;
    }

    public String getUnit() {
      return unit;
    }

  }

}
