package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class NumberWindowFunctionPartitioningStage {

  private WindowExpression function;

  public NumberWindowFunctionPartitioningStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumberWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new NumberWindowFunctionOrderingStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
