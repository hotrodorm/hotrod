package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class DateTimeWindowFunctionOverStage {

  private WindowExpression function;

  public DateTimeWindowFunctionOverStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public DateTimeWindowFunctionPartitioningStage partitionBy(final Expression... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new DateTimeWindowFunctionPartitioningStage(this.function);
  }

  public DateTimeWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new DateTimeWindowFunctionOrderingStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
