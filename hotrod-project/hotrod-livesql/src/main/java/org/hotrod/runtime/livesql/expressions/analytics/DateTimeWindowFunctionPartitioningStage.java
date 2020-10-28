package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

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
