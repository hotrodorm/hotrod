package org.hotrod.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.object.ObjectSyntaxExpression;
import org.hotrod.livesql.ordering.OrderingTerm;

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

  public ObjectSyntaxExpression end() {
    return this.function;
  }

}
