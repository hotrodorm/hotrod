package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.queries.QueryWriter;

public class Upper extends BuiltInCharFunction {

  private GeneralCharExpression string;

  public Upper(final GeneralCharExpression string) {
    super();
    this.string = string;
    super.register(this.string);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().upper(w, this.string);
  }

}
