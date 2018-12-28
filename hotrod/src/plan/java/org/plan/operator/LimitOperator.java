package org.plan.operator;

import java.util.List;

import org.plan.metrics.Metrics;
import org.plan.predicate.AccessPredicate;
import org.plan.predicate.FilterPredicate;

public class LimitOperator<T extends Comparable<T>> extends Operator<T> {

  private static final String NAME = "LIMIT";

  public LimitOperator(final T id, final String operatorName, final SourceSet sourceSet,
      final List<AccessPredicate> accessPredicates, final List<FilterPredicate> filterPredicates,
      final List<Operator<T>> children, final Metrics metrics) {
    super(NAME, id, operatorName, sourceSet, accessPredicates, filterPredicates, children, metrics);
    // TODO Auto-generated constructor stub

  }

}
