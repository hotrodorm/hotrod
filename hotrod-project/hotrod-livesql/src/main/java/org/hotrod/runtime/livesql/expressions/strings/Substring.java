package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Substring extends BuiltInStringFunction {

  private Expression<String> string;
  private Expression<Number> from;
  private Expression<Number> length;

  public Substring(final Expression<String> string, final Expression<Number> from, final Expression<Number> length) {
    super();
    this.string = string;
    this.from = from;
    this.length = length;
    super.register(this.string);
    super.register(this.from);
    super.register(this.length);
  }

  public Substring(final Expression<String> string, final Expression<Number> from) {
    super();
    this.string = string;
    this.from = from;
    this.length = null;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().substr(w, this.string, this.from, this.length);
  }

}
