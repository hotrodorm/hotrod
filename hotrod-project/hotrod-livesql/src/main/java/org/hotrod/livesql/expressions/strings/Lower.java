package org.hotrod.livesql.expressions.strings;

import org.hotrod.livesql.queries.QueryWriter;

public class Lower extends BuiltInStringFunction {

  private GeneralStringExpression string;

  public Lower(final GeneralStringExpression string) {
    super();
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().lower(w, this.string);
  }

}
