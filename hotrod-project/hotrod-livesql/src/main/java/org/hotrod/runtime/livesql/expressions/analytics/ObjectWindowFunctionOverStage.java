package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class ObjectWindowFunctionOverStage {

  private ObjectWindowExpression function;

  public ObjectWindowFunctionOverStage(final ObjectWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ObjectWindowFunctionPartitioningStage partitionBy(final Expression... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new ObjectWindowFunctionPartitioningStage(this.function);
  }

  public ObjectWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new ObjectWindowFunctionOrderingStage(this.function);
  }

  public ObjectExpression end() {
    return this.function;
  }

}
