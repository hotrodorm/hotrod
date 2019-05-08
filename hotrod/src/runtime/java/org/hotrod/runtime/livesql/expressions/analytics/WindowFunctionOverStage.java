package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class WindowFunctionOverStage<T> {

  private WindowExpression<T> function;

  public WindowFunctionOverStage(final WindowExpression<T> function) {
    this.function = function;
  }

  // Next stages

  public WindowFunctionPartitioningStage<T> partitionBy(final Expression<?>... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new WindowFunctionPartitioningStage<T>(this.function);
  }

  public WindowFunctionOrderingStage<T> orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new WindowFunctionOrderingStage<T>(this.function);
  }

  public WindowExpression<T> end() {
    return this.function;
  }

}
