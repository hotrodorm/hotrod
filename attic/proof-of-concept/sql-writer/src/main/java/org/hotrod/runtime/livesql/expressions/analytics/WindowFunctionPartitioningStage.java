package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class WindowFunctionPartitioningStage<T> {

  private WindowExpression<T> function;

  public WindowFunctionPartitioningStage(final WindowExpression<T> function) {
    this.function = function;
  }

  // Next stages

  public WindowFunctionOrderingStage<T> orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new WindowFunctionOrderingStage<T>(this.function);
  }

  public WindowExpression<T> end() {
    return this.function;
  }

}
