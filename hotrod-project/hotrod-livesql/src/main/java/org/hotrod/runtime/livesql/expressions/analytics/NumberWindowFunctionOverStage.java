package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class NumberWindowFunctionOverStage {

  private NumberWindowExpression function;

  public NumberWindowFunctionOverStage(final NumberWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumberWindowFunctionPartitioningStage partitionBy(final ComparableExpression... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new NumberWindowFunctionPartitioningStage(this.function);
  }

  public NumberWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new NumberWindowFunctionOrderingStage(this.function);
  }

  public NumberExpression end() {
    return this.function;
  }

}
