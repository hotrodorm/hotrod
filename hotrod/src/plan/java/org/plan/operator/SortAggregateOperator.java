package org.plan.operator;

import java.util.List;

import org.plan.metrics.Metrics;
import org.plan.predicate.AccessPredicate;
import org.plan.predicate.FilterPredicate;

public class SortAggregateOperator<T extends Comparable<T>> extends Operator<T> {

  private static final String NAME = "GROUP AGGREGATE";

  public SortAggregateOperator(final T id, final String specificName, final SourceSet sourceSet,
      final List<AccessPredicate> accessPredicates, final List<FilterPredicate> filterPredicates,
      final List<Operator<T>> children, final Metrics metrics) {
    super(NAME, id, specificName, sourceSet, accessPredicates, filterPredicates, children, metrics);
    // TODO Auto-generated constructor stub

  }

}
