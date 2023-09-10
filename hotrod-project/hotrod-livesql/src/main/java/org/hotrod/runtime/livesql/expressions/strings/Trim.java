package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Trim extends BuiltInStringFunction {

  private StringExpression string;

  public Trim(final StringExpression string) {
    super();
    this.string = string;
    super.register(this.string);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().trim(w, this.string);
  }

}
