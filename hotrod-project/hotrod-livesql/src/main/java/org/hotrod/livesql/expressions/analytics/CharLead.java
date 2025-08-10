package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class CharLead extends CharFunction implements PositionalAnalyticFunction {

  public CharLead(final CharExpression expression) {
    super("lead(#{})", expression);
  }

  public CharLead(final CharExpression expression, final NumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public CharLead(final CharExpression expression, final NumericExpression offset,
      final CharExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

}
