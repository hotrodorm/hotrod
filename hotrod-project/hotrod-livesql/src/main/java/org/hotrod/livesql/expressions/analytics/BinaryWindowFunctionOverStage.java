package org.hotrod.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.binary.BinarySyntaxExpression;
import org.hotrod.livesql.ordering.OrderingTerm;

public class BinaryWindowFunctionOverStage {

  private BinaryWindowExpression function;

  public BinaryWindowFunctionOverStage(final BinaryWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BinaryWindowFunctionPartitioningStage partitionBy(final ComparableExpression... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new BinaryWindowFunctionPartitioningStage(this.function);
  }

  public BinaryWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new BinaryWindowFunctionOrderingStage(this.function);
  }

  public BinarySyntaxExpression end() {
    return this.function;
  }

}
