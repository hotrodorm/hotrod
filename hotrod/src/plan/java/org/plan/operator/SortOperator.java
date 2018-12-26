package org.plan.operator;

import java.util.List;

import org.plan.metrics.ActualMetrics;
import org.plan.metrics.EstimatedMetrics;
import org.plan.predicate.AccessPredicate;
import org.plan.predicate.FilterPredicate;

public class SortOperator extends Operator {

  public SortOperator(final String id, final String operatorName, final boolean includesFetch,
      final List<AccessPredicate> accessPredicates, final List<FilterPredicate> filterPredicates,
      final List<Operator> children, final EstimatedMetrics estimatedMetrics, final ActualMetrics actualMetrics) {
    super(id, operatorName, includesFetch, accessPredicates, filterPredicates, children, estimatedMetrics,
        actualMetrics);
    // TODO Auto-generated constructor stub

  }

}
