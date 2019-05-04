package org.hotrod.runtime.sql.expressions.strings;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.numbers.NumberExpression;

public class Substring extends StringFunction {

  private StringExpression string;
  private NumberExpression from;
  private NumberExpression length;

  public Substring(final StringExpression string, final NumberExpression from, final NumberExpression length) {
    super();
    this.string = string;
    this.from = from;
    this.length = length;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().substring(w, this.string, this.from, this.length);
  }

}
