package org.hotrod.livesql.expressions.strings;

import org.hotrod.livesql.queries.QueryWriter;

public class Trim extends BuiltInStringFunction {

  private GeneralStringExpression string;

  public Trim(final GeneralStringExpression string) {
    super();
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().trim(w, this.string);
  }

}
