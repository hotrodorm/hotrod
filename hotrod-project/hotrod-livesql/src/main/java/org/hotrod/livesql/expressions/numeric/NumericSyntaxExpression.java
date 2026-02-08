package org.hotrod.livesql.expressions.numeric;

import org.hotrod.converter.TypeConverter;
import org.hotrod.livesql.expressions.TypedExpression;

public abstract class NumericSyntaxExpression extends NumericExpression {

  protected NumericSyntaxExpression(int precedence) {
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
