package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class ByteArrayWindowFunctionPartitioningStage {

  private WindowExpression function;

  public ByteArrayWindowFunctionPartitioningStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ByteArrayWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new ByteArrayWindowFunctionOrderingStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
