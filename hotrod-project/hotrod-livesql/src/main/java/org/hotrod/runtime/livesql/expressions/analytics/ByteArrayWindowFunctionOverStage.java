package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class ByteArrayWindowFunctionOverStage {

  private WindowExpression function;

  public ByteArrayWindowFunctionOverStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ByteArrayWindowFunctionPartitioningStage partitionBy(final Expression... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new ByteArrayWindowFunctionPartitioningStage(this.function);
  }

  public ByteArrayWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new ByteArrayWindowFunctionOrderingStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
