package org.hotrod.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.character.CharSyntaxExpression;
import org.hotrod.livesql.ordering.OrderingTerm;

public class CharWindowFunctionOverStage {

  private CharWindowExpression function;

  public CharWindowFunctionOverStage(final CharWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public CharWindowFunctionPartitioningStage partitionBy(final ComparableExpression... expressions) {
    this.function.setPartitionBy(Arrays.asList(expressions));
    return new CharWindowFunctionPartitioningStage(this.function);
  }

  public CharWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new CharWindowFunctionOrderingStage(this.function);
  }

  public CharSyntaxExpression end() {
    return this.function;
  }

}
