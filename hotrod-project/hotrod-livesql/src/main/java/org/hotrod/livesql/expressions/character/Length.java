package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.numeric.NumericSyntaxExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class Length extends NumericSyntaxExpression {

  private CharExpression string;

  public Length(final CharExpression string) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().length(w, this.string);
  }

}
