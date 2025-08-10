package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class Substring extends BuiltInCharFunction {

  private CharExpression string;
  private NumericExpression from;
  private NumericExpression length;

  public Substring(final CharExpression string, final NumericExpression from, final NumericExpression length) {
    super();
    this.string = string;
    this.from = from;
    this.length = length;
    super.register(this.string);
    super.register(this.from);
    super.register(this.length);
  }

  public Substring(final CharExpression string, final NumericExpression from) {
    super();
    this.string = string;
    this.from = from;
    this.length = null;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().substr(w, this.string, this.from, this.length);
  }

}
