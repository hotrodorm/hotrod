package org.hotrod.livesql.expressions.character;

import java.util.Arrays;
import java.util.List;

import org.hotrod.livesql.queries.QueryWriter;

public class Concat extends BuiltInCharFunction {

  private List<CharExpression> strings;

  public Concat(final List<CharExpression> strings) {
    super();
    this.strings = strings;
    this.strings.forEach(e -> super.register(e));
  }

  @SafeVarargs
  public Concat(final CharExpression... a) {
    super();
    this.strings = Arrays.asList(a);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().concat(w, this.strings);
  }

}
