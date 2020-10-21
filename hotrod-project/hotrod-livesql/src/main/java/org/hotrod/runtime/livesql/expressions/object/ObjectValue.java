package org.hotrod.runtime.livesql.expressions.object;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class ObjectValue extends ObjectExpression {

  private Expression<Object> value;

  public ObjectValue(final Expression<Object> value) {
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
