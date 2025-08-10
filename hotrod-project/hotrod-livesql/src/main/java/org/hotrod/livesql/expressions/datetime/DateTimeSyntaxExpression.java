package org.hotrod.livesql.expressions.datetime;

import org.hotrod.livesql.expressions.TypedExpression;

public abstract class DateTimeSyntaxExpression extends DateTimeExpression {

  protected DateTimeSyntaxExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
