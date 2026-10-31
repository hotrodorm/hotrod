package org.hotrod.livesql.queries;

import org.hotrod.converter.TypeConverter;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.SQLParameterWriter.RenderedParameter;

public class ConvertedValue extends Expression {

  private Object value;
  private TypeConverter<?, Object> converter;

  public ConvertedValue(Object value, TypeConverter<?, Object> converter) {
    super(Expression.PRECEDENCE_LITERAL);
    this.value = value;
    this.converter = converter;
  }

  @Override
  protected void renderTo(QueryWriter w) {
    RenderedParameter p = w.registerParameter(this.value, this.converter);
    w.write(p.getPlaceholder());
  }

}
