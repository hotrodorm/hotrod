package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.QueryWriter;

public class Upper extends StringFunction {

  private StringExpression string;

  public Upper(final StringExpression string) {
    super();
    this.string = string;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().upper(w, this.string);
  }

}
