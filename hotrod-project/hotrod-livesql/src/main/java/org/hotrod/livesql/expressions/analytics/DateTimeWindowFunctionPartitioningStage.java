package org.hotrod.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.ordering.OrderingTerm;

public class DateTimeWindowFunctionPartitioningStage {

  private DateTimeWindowExpression function;

  public DateTimeWindowFunctionPartitioningStage(final DateTimeWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public DateTimeWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new DateTimeWindowFunctionOrderingStage(this.function);
  }

  public DateTimeExpression end() {
    return this.function;
  }

}
