package org.hotrod.livesql.expressions.strings.postgresql;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.numbers.NumberExpression;
import org.hotrod.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class Ascii extends NumberExpression {

  private GeneralStringExpression string;

  public Ascii(final GeneralStringExpression string) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("ascii(");
    Shield.renderTo(this.string, w);
    w.write(")");
  }

}
