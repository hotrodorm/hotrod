package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class ByteArrayWindowFunctionOverStage {

  private ByteArrayWindowExpression function;

  public ByteArrayWindowFunctionOverStage(final ByteArrayWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ByteArrayWindowFunctionPartitioningStage partitionBy(final ComparableExpression... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new ByteArrayWindowFunctionPartitioningStage(this.function);
  }

  public ByteArrayWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new ByteArrayWindowFunctionOrderingStage(this.function);
  }

  public ByteArrayExpression end() {
    return this.function;
  }

}
