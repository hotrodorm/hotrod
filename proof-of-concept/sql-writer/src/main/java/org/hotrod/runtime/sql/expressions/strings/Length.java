package org.hotrod.runtime.sql.expressions.strings;

import org.hotrod.runtime.sql.QueryWriter;

public class Length extends StringFunction {

  private StringExpression string;

  public Length(final StringExpression string) {
    super();
    this.string = string;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().length(w, this.string);
  }

}
