package org.hotrod.livesql.expressions.character.postgresql;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.character.GeneralCharExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class Ascii extends NumericExpression {

  private GeneralCharExpression string;

  public Ascii(final GeneralCharExpression string) {
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
