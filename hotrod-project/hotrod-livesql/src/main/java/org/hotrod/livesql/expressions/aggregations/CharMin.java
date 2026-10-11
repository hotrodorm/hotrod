package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.CharWindowExpression;
import org.hotrod.livesql.expressions.analytics.CharWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class CharMin extends CharUnfilteredFunction implements WindowableAggregationFunction {

  public CharMin(final CharExpression expression) {
    super("min", expression);
  }

  public CharFilteredFunction filter(final Predicate filter) {
    return new CharFilteredFunction(super.name, super.expression, filter);
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

}
