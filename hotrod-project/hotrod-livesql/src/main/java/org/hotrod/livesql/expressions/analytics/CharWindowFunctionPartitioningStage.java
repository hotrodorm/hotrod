package org.hotrod.livesql.expressions.analytics;

import java.util.Arrays;

import org.hotrod.livesql.expressions.character.CharSyntaxExpression;
import org.hotrod.livesql.ordering.OrderingTerm;

public class CharWindowFunctionPartitioningStage {

  private CharWindowExpression function;

  public CharWindowFunctionPartitioningStage(final CharWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public CharWindowFunctionOrderingStage orderBy(final OrderingTerm... orderingTerm) {
    this.function.setOrderBy(Arrays.asList(orderingTerm));
    return new CharWindowFunctionOrderingStage(this.function);
  }

  public CharSyntaxExpression end() {
    return this.function;
  }

}
