package org.hotrod.runtime.livesql.expressions.binary;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class ByteArrayValue extends ByteArrayExpression {

  private Expression<byte[]> value;

  public ByteArrayValue(final Expression<byte[]> value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.value = value;
    super.setPrecedence(value.getPrecedence());
    super.register(this.value);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.value.renderTo(w);
  }

}
