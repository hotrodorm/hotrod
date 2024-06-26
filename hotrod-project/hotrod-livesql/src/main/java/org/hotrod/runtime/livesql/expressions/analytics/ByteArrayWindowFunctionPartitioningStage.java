package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class ByteArrayWindowFunctionPartitioningStage {

  private ByteArrayWindowExpression function;

  public ByteArrayWindowFunctionPartitioningStage(final ByteArrayWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ByteArrayWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new ByteArrayWindowFunctionOrderingStage(this.function);
  }

  public ByteArrayExpression end() {
    return this.function;
  }

}
