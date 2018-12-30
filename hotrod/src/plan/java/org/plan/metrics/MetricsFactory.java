package org.plan.metrics;

import org.plan.metrics.Metrics.ActualMetrics;
import org.plan.metrics.Metrics.EstimatedMetrics;
import org.plan.metrics.Metrics.FullMetrics;
import org.plan.metrics.Metrics.NoMetrics;

public abstract class MetricsFactory {

  protected boolean includesEstimatedMetrics;
  protected boolean includesActualMetrics;

  public static MetricsFactory instantiate(final boolean includesEstimatedMetrics,
      final boolean includesActualMetrics) {
    if (includesEstimatedMetrics) {
      return includesActualMetrics ? new FullMetricsFactory() : new EstimatedMetricsFactory();
    } else {
      return includesActualMetrics ? new ActualMetricsFactory() : new NoMetricsFactory();
    }
  }

  public abstract Metrics getMetrics(final Double estimatedCost, final Double estimatedRows, final Double actualTimeMs,
      final Long actualRows, final Long actualLoops);

  public boolean includesEstimatedMetrics() {
    return this.includesEstimatedMetrics;
  }

  public boolean includesActualMetrics() {
    return this.includesActualMetrics;
  }

  // Implementations

  public static class NoMetricsFactory extends MetricsFactory {

    private static final String NAME = "No Metrics";

    private NoMetricsFactory() {
      super.includesEstimatedMetrics = false;
      super.includesActualMetrics = false;
    }

    @Override
    public Metrics getMetrics(final Double estimatedCost, final Double estimatedRows, final Double actualTimeMs,
        final Long actualRows, final Long actualLoops) {

      if (estimatedCost != null) {
        throw new IllegalArgumentException("estimatedCost must be null for this type of metrics (" + NAME + ")");
      }
      if (estimatedRows != null) {
        throw new IllegalArgumentException("estimatedRows must be null for this type of metrics (" + NAME + ")");
      }
      if (actualTimeMs != null) {
        throw new IllegalArgumentException("actualTimeMs must be null for this type of metrics (" + NAME + ")");
      }
      if (actualRows != null) {
        throw new IllegalArgumentException("actualRows must be null for this type of metrics (" + NAME + ")");
      }
      if (actualLoops != null) {
        throw new IllegalArgumentException("actualLoops must be null for this type of metrics (" + NAME + ")");
      }

      return new NoMetrics();
    }

  }

  public static class EstimatedMetricsFactory extends MetricsFactory {

    private static final String NAME = "Estimated Metrics";

    private EstimatedMetricsFactory() {
      super.includesEstimatedMetrics = true;
      super.includesActualMetrics = false;
    }

    @Override
    public Metrics getMetrics(final Double estimatedCost, final Double estimatedRows, final Double actualTimeMs,
        final Long actualRows, final Long actualLoops) {

      if (actualTimeMs != null) {
        throw new IllegalArgumentException("actualTimeMs must be null for this type of metrics (" + NAME + ")");
      }
      if (actualRows != null) {
        throw new IllegalArgumentException("actualRows must be null for this type of metrics (" + NAME + ")");
      }
      if (actualLoops != null) {
        throw new IllegalArgumentException("actualLoops must be null for this type of metrics (" + NAME + ")");
      }

      if (estimatedCost == null) {
        throw new IllegalArgumentException("estimatedCost must be not null for this type of metrics (" + NAME + ")");
      }

      return new EstimatedMetrics(estimatedCost, estimatedRows);
    }

  }

  public static class ActualMetricsFactory extends MetricsFactory {

    private static final String NAME = "Actual Metrics";

    private ActualMetricsFactory() {
      super.includesEstimatedMetrics = false;
      super.includesActualMetrics = true;
    }

    @Override
    public Metrics getMetrics(final Double estimatedCost, final Double estimatedRows, final Double actualTimeMs,
        final Long actualRows, final Long actualLoops) {

      if (estimatedCost != null) {
        throw new IllegalArgumentException("estimatedCost must be null for this type of metrics (" + NAME + ")");
      }
      if (estimatedRows != null) {
        throw new IllegalArgumentException("estimatedRows must be null for this type of metrics (" + NAME + ")");
      }

      if (actualTimeMs == null) {
        throw new IllegalArgumentException("actualTimeMs must be not null for this type of metrics (" + NAME + ")");
      }

      return new ActualMetrics(actualTimeMs, actualRows, actualLoops);
    }

  }

  public static class FullMetricsFactory extends MetricsFactory {

    private static final String NAME = "Full Metrics";

    private FullMetricsFactory() {
      super.includesEstimatedMetrics = true;
      super.includesActualMetrics = true;
    }

    @Override
    public Metrics getMetrics(final Double estimatedCost, final Double estimatedRows, final Double actualTimeMs,
        final Long actualRows, final Long actualLoops) {

      if (estimatedCost == null) {
        throw new IllegalArgumentException("estimatedCost must be not null for this type of metrics (" + NAME + ")");
      }
      if (actualTimeMs == null) {
        throw new IllegalArgumentException("actualTimeMs must be not null for this type of metrics (" + NAME + ")");
      }

      return new FullMetrics(estimatedCost, estimatedRows, actualTimeMs, actualRows, actualLoops);
    }

  }

}
