package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class StringWindowFunctionPartitioningStage {

  private WindowExpression function;

  public StringWindowFunctionPartitioningStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public StringWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new StringWindowFunctionOrderingStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
