package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;

public class ObjectWindowFunctionOverStage {

  private ObjectWindowExpression function;

  public ObjectWindowFunctionOverStage(final ObjectWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public ObjectWindowFunctionPartitioningStage partitionBy(final ComparableExpression... expressions) {
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
