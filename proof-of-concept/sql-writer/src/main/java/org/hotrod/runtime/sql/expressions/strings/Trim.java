package org.hotrod.runtime.sql.expressions.strings;

import org.hotrod.runtime.sql.QueryWriter;

public class Trim extends StringFunction {

  private StringExpression string;

  public Trim(final StringExpression string) {
    super();
    this.string = string;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().trim(w, this.string);
  }

}
