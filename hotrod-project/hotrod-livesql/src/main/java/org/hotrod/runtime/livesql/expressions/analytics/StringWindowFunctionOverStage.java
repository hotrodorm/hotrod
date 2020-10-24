package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class StringWindowFunctionOverStage {

  private WindowExpression function;

  public StringWindowFunctionOverStage(final WindowExpression function) {
    this.function = function;
  }

  // Next stages

  public StringWindowFunctionPartitioningStage partitionBy(final Expression... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new StringWindowFunctionPartitioningStage(this.function);
  }

  public StringWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new StringWindowFunctionOrderingStage(this.function);
  }

  public WindowExpression end() {
    return this.function;
  }

}
