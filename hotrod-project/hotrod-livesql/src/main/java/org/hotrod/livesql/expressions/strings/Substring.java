package org.hotrod.livesql.expressions.strings;

import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class Substring extends BuiltInStringFunction {

  private GeneralStringExpression string;
  private GeneralNumberExpression from;
  private GeneralNumberExpression length;

  public Substring(final GeneralStringExpression string, final GeneralNumberExpression from, final GeneralNumberExpression length) {
    super();
    this.string = string;
    this.from = from;
    this.length = length;
    super.register(this.string);
    super.register(this.from);
    super.register(this.length);
  }

  public Substring(final GeneralStringExpression string, final GeneralNumberExpression from) {
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
