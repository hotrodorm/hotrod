package org.hotrod.livesql.expressions.character;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;

public abstract class CharFunction extends CharSyntaxExpression {

  protected CharFunction(final ComparableExpression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    Arrays.asList(parameters).forEach(p -> super.register(p));
  }

}
