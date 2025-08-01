package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.CharWindowExpression;
import org.hotrod.livesql.expressions.analytics.CharWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.character.GeneralCharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;

public class CharMin extends CharFunction implements WindowableAggregationFunction {

  public CharMin(final GeneralCharExpression expression) {
    super("min(#{})", expression);
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

}
