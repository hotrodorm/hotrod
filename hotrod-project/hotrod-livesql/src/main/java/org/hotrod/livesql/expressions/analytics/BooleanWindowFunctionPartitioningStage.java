package org.hotrod.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.ordering.OrderingTerm;

public class BooleanWindowFunctionPartitioningStage {

  private BooleanWindowExpression function;

  public BooleanWindowFunctionPartitioningStage(final BooleanWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public BooleanWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new BooleanWindowFunctionOrderingStage(this.function);
  }

  public BooleanSyntaxExpression end() {
    return this.function;
  }

}
