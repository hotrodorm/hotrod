package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.QueryWriter;

public class Lower extends StringFunction {

  private StringExpression string;

  public Lower(final StringExpression string) {
    super();
    this.string = string;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().lower(w, this.string);
  }

}
