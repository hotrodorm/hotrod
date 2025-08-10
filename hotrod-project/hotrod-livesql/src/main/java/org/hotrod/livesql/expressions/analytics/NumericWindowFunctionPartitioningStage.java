package org.hotrod.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.livesql.expressions.numeric.NumericSyntaxExpression;
import org.hotrod.livesql.ordering.OrderingTerm;

public class NumericWindowFunctionPartitioningStage {

  private NumericWindowExpression function;

  public NumericWindowFunctionPartitioningStage(final NumericWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumericWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new NumericWindowFunctionOrderingStage(this.function);
  }

  public NumericSyntaxExpression end() {
    return this.function;
  }

}
