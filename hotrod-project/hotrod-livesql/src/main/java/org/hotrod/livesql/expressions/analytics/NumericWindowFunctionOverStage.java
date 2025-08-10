package org.hotrod.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.numeric.NumericSyntaxExpression;
import org.hotrod.livesql.ordering.OrderingTerm;

public class NumericWindowFunctionOverStage {

  private NumericWindowExpression function;

  public NumericWindowFunctionOverStage(final NumericWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumericWindowFunctionPartitioningStage partitionBy(final ComparableExpression... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new NumericWindowFunctionPartitioningStage(this.function);
  }

  public NumericWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new NumericWindowFunctionOrderingStage(this.function);
  }

  public NumericSyntaxExpression end() {
    return this.function;
  }

}
