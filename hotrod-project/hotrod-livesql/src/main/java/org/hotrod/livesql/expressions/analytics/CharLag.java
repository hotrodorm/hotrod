package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.character.GeneralCharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;

public class CharLag extends CharFunction implements PositionalAnalyticFunction {

  public CharLag(final GeneralCharExpression expression) {
    super("lag(#{})", expression);
  }

  public CharLag(final GeneralCharExpression expression, final GeneralNumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public CharLag(final GeneralCharExpression expression, final GeneralNumericExpression offset,
      final GeneralCharExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

}
