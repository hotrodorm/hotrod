package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.object.GeneralObjectExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class ObjectWindowFunctionPartitioningStage {

  private ObjectWindowExpression function;

  public ObjectWindowFunctionPartitioningStage(final ObjectWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ObjectWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new ObjectWindowFunctionOrderingStage(this.function);
  }

  public GeneralObjectExpression end() {
    return this.function;
  }

}
