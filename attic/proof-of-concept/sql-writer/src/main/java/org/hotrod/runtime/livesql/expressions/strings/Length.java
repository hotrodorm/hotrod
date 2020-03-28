package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.QueryWriter;

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
