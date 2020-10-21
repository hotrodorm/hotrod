package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Locate extends NumberExpression {

  private Expression<String> substring;
  private Expression<String> string;
  private Expression<Number> from;

  public Locate(final Expression<String> substring, final Expression<String> string, final Expression<Number> from) {
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
    w.getSqlDialect().getFunctionRenderer().locate(w, this.substring, this.string, this.from);
  }

}
