package org.hotrod.livesql.expressions.character;

import org.hotrod.converter.TypeConverter;
import org.hotrod.livesql.expressions.TypedExpression;

public abstract class CharSyntaxExpression extends CharExpression {

  protected CharSyntaxExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

  public TypedExpression converter(final TypeConverter<?, ?> converter) {
    return new TypedExpression(this, converter);
  }

}
