package org.hotrod.livesql.expressions.object;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;

public abstract class ObjectFunction extends ObjectSyntaxExpression {

  protected ObjectFunction(final ComparableExpression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    Arrays.asList(parameters).forEach(p -> super.register(p));
  }

}
