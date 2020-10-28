package org.hotrod.runtime.livesql.expressions.strings;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Concat extends BuiltInStringFunction {

  private List<StringExpression> strings;

  public Concat(final List<StringExpression> strings) {
    super();
    this.strings = strings;
    this.strings.forEach(e -> super.register(e));
  }

  @SafeVarargs
  public Concat(final StringExpression... a) {
    super();
    this.strings = Arrays.asList(a);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().concat(w, this.strings);
  }

}
