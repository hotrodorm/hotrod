package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.queries.QueryWriter;

public class Lower extends BuiltInCharFunction {

  private CharExpression string;

  public Lower(final CharExpression string) {
    super();
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().lower(w, this.string);
  }

}
