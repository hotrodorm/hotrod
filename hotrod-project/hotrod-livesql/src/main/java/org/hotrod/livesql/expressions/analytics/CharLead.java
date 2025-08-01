package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.character.GeneralCharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;

public class CharLead extends CharFunction implements PositionalAnalyticFunction {

  public CharLead(final GeneralCharExpression expression) {
    super("lead(#{})", expression);
  }

  public CharLead(final GeneralCharExpression expression, final GeneralNumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public CharLead(final GeneralCharExpression expression, final GeneralNumericExpression offset,
      final GeneralCharExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

}
