package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Substring extends BuiltInStringFunction {

  private StringExpression string;
  private NumberExpression from;
  private NumberExpression length;

  public Substring(final StringExpression string, final NumberExpression from, final NumberExpression length) {
    super();
    this.string = string;
    this.from = from;
    this.length = length;
    super.register(this.string);
    super.register(this.from);
    super.register(this.length);
  }

  public Substring(final StringExpression string, final NumberExpression from) {
    super();
    this.string = string;
    this.from = from;
    this.length = null;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().substr(w, this.string, this.from, this.length);
  }

}
