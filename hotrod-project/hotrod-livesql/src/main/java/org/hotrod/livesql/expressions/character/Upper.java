package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.queries.QueryWriter;

public class Upper extends BuiltInCharFunction {

  private CharExpression string;

  public Upper(final CharExpression string) {
    super();
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().upper(w, this.string);
  }

}
