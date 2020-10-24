package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class ObjectWindowFunctionPartitioningStage {

  private WindowExpression function;

  public ObjectWindowFunctionPartitioningStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ObjectWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new ObjectWindowFunctionOrderingStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
