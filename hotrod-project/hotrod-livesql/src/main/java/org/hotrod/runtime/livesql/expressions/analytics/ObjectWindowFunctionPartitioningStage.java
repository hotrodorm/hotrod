package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;

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

  public ObjectExpression end() {
    return this.function;
  }

}
