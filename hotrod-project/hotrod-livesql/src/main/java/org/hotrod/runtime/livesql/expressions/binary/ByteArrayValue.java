package org.hotrod.runtime.livesql.expressions.binary;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

@Deprecated
public class ByteArrayValue extends ByteArrayExpression {

  private ByteArrayExpression value;

  public ByteArrayValue(final ByteArrayExpression value) {
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
