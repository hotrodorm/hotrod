package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class StringWindowFunctionOverStage {

  private StringWindowExpression function;

  public StringWindowFunctionOverStage(final StringWindowExpression function) {
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

  public StringExpression end() {
    return this.function;
  }

}
