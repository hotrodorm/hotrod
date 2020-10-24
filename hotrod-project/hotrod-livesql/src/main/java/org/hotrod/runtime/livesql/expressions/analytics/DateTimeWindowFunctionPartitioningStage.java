package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class DateTimeWindowFunctionPartitioningStage {

  private WindowExpression function;

  public DateTimeWindowFunctionPartitioningStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public DateTimeWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new DateTimeWindowFunctionOrderingStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
