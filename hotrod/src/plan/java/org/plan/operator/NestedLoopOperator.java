package org.plan.operator;

import java.util.List;

import org.plan.metrics.Metrics;
import org.plan.predicate.AccessPredicate;
import org.plan.predicate.FilterPredicate;

public class NestedLoopOperator extends Operator {

  public NestedLoopOperator(final String id, final String operatorName, final SourceSet sourceSet,
      final List<AccessPredicate> accessPredicates, final List<FilterPredicate> filterPredicates,
      final List<Operator> children, final Metrics metrics) {
    super(id, operatorName, sourceSet, accessPredicates, filterPredicates, children, metrics);
    // TODO Auto-generated constructor stub

  }

}
