package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Lower extends BuiltInStringFunction {

  private StringExpression string;

  public Lower(final StringExpression string) {
    super();
    this.string = string;
    super.register(this.string);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().lower(w, this.string);
  }

}
