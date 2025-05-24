package org.hotrod.livesql.expressions.strings;

import java.util.Arrays;
import java.util.List;

import org.hotrod.livesql.queries.QueryWriter;

public class Concat extends BuiltInStringFunction {

  private List<GeneralStringExpression> strings;

  public Concat(final List<GeneralStringExpression> strings) {
    super();
    this.strings = strings;
    this.strings.forEach(e -> super.register(e));
  }

  @SafeVarargs
  public Concat(final GeneralStringExpression... a) {
    super();
    this.strings = Arrays.asList(a);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().concat(w, this.strings);
  }

}
