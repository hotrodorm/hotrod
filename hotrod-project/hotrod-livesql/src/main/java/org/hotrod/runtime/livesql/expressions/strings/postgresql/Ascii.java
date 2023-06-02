package org.hotrod.runtime.livesql.expressions.strings.postgresql;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Ascii extends NumberExpression {

  private StringExpression string;

  public Ascii(final StringExpression string) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.string = string;
    super.register(this.string);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("ascii(");
    this.string.renderTo(w);
    w.write(")");
  }

}
