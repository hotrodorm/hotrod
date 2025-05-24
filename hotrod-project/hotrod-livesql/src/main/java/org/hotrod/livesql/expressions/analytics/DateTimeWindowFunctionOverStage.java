package org.hotrod.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.ordering.OrderingTerm;

public class DateTimeWindowFunctionOverStage {

  private DateTimeWindowExpression function;

  public DateTimeWindowFunctionOverStage(final DateTimeWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public DateTimeWindowFunctionPartitioningStage partitionBy(final ComparableExpression... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new DateTimeWindowFunctionPartitioningStage(this.function);
  }

  public DateTimeWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new DateTimeWindowFunctionOrderingStage(this.function);
  }

  public DateTimeExpression end() {
    return this.function;
  }

}
