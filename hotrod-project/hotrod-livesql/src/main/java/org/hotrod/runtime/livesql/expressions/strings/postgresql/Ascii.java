package org.hotrod.runtime.livesql.expressions.strings.postgresql;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Ascii extends NumberExpression {

  private StringExpression string;

  public Ascii(final StringExpression string) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("ascii(");
    Helper.renderTo(this.string, w);
    w.write(")");
  }

}
