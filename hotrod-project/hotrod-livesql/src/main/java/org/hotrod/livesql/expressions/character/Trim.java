package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.queries.QueryWriter;

public class Trim extends BuiltInCharFunction {

  private CharExpression string;

  public Trim(final CharExpression string) {
    super();
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().trim(w, this.string);
  }

}
