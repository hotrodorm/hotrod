package org.hotrod.runtime.sql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.OrderingTerm;

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
