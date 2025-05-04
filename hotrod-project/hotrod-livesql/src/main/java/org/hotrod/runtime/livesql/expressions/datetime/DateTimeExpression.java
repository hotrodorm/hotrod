package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.expressions.TypedExpression;

public abstract class DateTimeExpression extends GeneralDateTimeExpression {

  protected DateTimeExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
