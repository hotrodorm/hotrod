package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Locate extends NumberExpression {

  private StringExpression substring;
  private StringExpression string;
  private NumberExpression from;

  public Locate(final StringExpression substring, final StringExpression string, final NumberExpression from) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.substring = substring;
    this.string = string;
    this.from = from;
    super.register(this.substring);
    super.register(this.string);
    super.register(this.from);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().locate(w, this.substring, this.string, this.from);
  }

}
