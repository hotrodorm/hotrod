package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public class NumberWindowFunctionPartitioningStage {

  private NumberWindowExpression function;

  public NumberWindowFunctionPartitioningStage(final NumberWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumberWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new NumberWindowFunctionOrderingStage(this.function);
  }

  public NumberExpression end() {
    return this.function;
  }

}
