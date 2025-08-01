package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.queries.QueryWriter;

public class Lower extends BuiltInCharFunction {

  private GeneralCharExpression string;

  public Lower(final GeneralCharExpression string) {
    super();
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().lower(w, this.string);
  }

}
