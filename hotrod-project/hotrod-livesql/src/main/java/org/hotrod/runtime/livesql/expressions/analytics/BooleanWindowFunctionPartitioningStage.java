package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanWindowFunctionPartitioningStage {

  private BooleanWindowExpression function;

  public BooleanWindowFunctionPartitioningStage(final BooleanWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BooleanWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new BooleanWindowFunctionOrderingStage(this.function);
  }

  public Predicate end() {
    return this.function;
  }

}
