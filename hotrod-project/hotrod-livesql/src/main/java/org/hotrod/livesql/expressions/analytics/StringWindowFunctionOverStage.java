package org.hotrod.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.strings.StringExpression;
import org.hotrod.livesql.ordering.OrderingTerm;

public class StringWindowFunctionOverStage {

  private StringWindowExpression function;

  public StringWindowFunctionOverStage(final StringWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public StringWindowFunctionPartitioningStage partitionBy(final ComparableExpression... expressions) {
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
