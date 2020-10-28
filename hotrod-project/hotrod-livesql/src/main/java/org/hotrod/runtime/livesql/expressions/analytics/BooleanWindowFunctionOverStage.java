package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class BooleanWindowFunctionOverStage {

  private BooleanWindowExpression function;

  public BooleanWindowFunctionOverStage(final BooleanWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BooleanWindowFunctionPartitioningStage partitionBy(final Expression... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new BooleanWindowFunctionPartitioningStage(this.function);
  }

  public BooleanWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new BooleanWindowFunctionOrderingStage(this.function);
  }

  public Predicate end() {
    return this.function;
  }

}
