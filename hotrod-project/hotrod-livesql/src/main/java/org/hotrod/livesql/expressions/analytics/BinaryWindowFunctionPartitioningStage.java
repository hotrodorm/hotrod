package org.hotrod.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.ordering.OrderingTerm;

public class BinaryWindowFunctionPartitioningStage {

  private BinaryWindowExpression function;

  public BinaryWindowFunctionPartitioningStage(final BinaryWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BinaryWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new BinaryWindowFunctionOrderingStage(this.function);
  }

  public BinaryExpression end() {
    return this.function;
  }

}
