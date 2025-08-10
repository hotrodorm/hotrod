package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.CharWindowExpression;
import org.hotrod.livesql.expressions.analytics.CharWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;

public class CharMax extends CharFunction implements WindowableAggregationFunction {

  public CharMax(final CharExpression expression) {
    super("max(#{})", expression);
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

}
