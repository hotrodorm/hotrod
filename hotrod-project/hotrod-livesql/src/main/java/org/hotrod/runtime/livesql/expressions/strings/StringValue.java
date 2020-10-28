package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

@Deprecated
public class StringValue extends StringExpression {

  private StringExpression value;

  public StringValue(final StringExpression value) {
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
