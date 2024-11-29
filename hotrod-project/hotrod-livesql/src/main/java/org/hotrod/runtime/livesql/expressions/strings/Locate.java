package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Locate extends NumberExpression {

  private GeneralStringExpression substring;
  private GeneralStringExpression string;
  private GeneralNumberExpression from;

  public Locate(final GeneralStringExpression substring, final GeneralStringExpression string, final GeneralNumberExpression from) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.substring = substring;
    this.string = string;
    this.from = from;
    super.register(this.substring);
    super.register(this.string);
    super.register(this.from);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().locate(w, this.substring, this.string, this.from);
  }

}
