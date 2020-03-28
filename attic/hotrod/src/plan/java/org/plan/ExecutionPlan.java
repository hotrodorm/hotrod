package org.plan;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.plan.metrics.MetricsFactory;
import org.plan.operator.Operator;

public class ExecutionPlan<T extends Comparable<T>> {

  private String queryTag;
  private Date producedAt;

  // stats?

  private String query;
  private LinkedHashMap<String, Object> parameterValues;

  private Operator<?> rootOperator;

  private MetricsFactory metricsFactory;

  public static <E extends Comparable<E>> ExecutionPlan<E> instantiate(final String queryTag, final Date producedAt, final String query,
      final LinkedHashMap<String, Object> parameterValues, final Operator<E> rootOperator,
      final boolean includesEstimatedMetrics, final boolean includesActualMetrics) {
    MetricsFactory metricsFactory = MetricsFactory.instantiate(includesEstimatedMetrics, includesActualMetrics);
    return new ExecutionPlan<E>(queryTag, producedAt, query, parameterValues, rootOperator, metricsFactory);
  }

  private ExecutionPlan(final String queryTag, final Date producedAt, final String query,
      final LinkedHashMap<String, Object> parameterValues, final Operator<T> rootOperator,
      final MetricsFactory metricsFactory) {
    this.queryTag = queryTag;
    this.producedAt = producedAt;
    this.query = query;
    this.parameterValues = parameterValues;
    this.rootOperator = rootOperator;
    this.metricsFactory = metricsFactory;
  }

  public String getQueryTag() {
    return queryTag;
  }

  public Date getProducedAt() {
    return producedAt;
  }

  public String getQuery() {
    return query;
  }

  public Map<String, Object> getParameterValues() {
    return parameterValues;
  }

  public Operator<?> getRootOperator() {
    return rootOperator;
  }

  public MetricsFactory getMetricsFactory() {
    return metricsFactory;
  }

}
