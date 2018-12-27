package org.plan.operator;

import java.util.List;

import org.plan.metrics.Metrics;
import org.plan.predicate.AccessPredicate;
import org.plan.predicate.FilterPredicate;

public abstract class Operator {

  private String id;
  private String operatorName;

  private boolean includesFetch;
  private List<AccessPredicate> accessPredicates;
  private List<FilterPredicate> filterPredicates;
  private List<Operator> children;

  private Metrics metrics;

  public Operator(final String id, final String operatorName, final boolean includesFetch,
      final List<AccessPredicate> accessPredicates, final List<FilterPredicate> filterPredicates,
      final List<Operator> children, final Metrics metrics) {
    this.id = id;
    this.operatorName = operatorName;
    this.includesFetch = includesFetch;
    this.accessPredicates = accessPredicates;
    this.filterPredicates = filterPredicates;
    this.children = children;
    this.metrics = metrics;
  }

  public String getId() {
    return id;
  }

  public String getOperatorName() {
    return operatorName;
  }

  public boolean isIncludesFetch() {
    return includesFetch;
  }

  public List<AccessPredicate> getAccessPredicates() {
    return accessPredicates;
  }

  public List<FilterPredicate> getFilterPredicates() {
    return filterPredicates;
  }

  public List<Operator> getChildren() {
    return children;
  }

  public Metrics getMetrics() {
    return metrics;
  }

}
