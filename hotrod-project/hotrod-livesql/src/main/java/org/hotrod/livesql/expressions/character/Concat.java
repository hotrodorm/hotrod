package org.hotrod.livesql.expressions.character;

import java.util.Arrays;
import java.util.List;

import org.hotrod.livesql.queries.QueryWriter;

public class Concat extends BuiltInCharFunction {

  private List<GeneralCharExpression> strings;

  public Concat(final List<GeneralCharExpression> strings) {
    super();
    this.strings = strings;
    this.strings.forEach(e -> super.register(e));
  }

  @SafeVarargs
  public Concat(final GeneralCharExpression... a) {
    super();
    this.strings = Arrays.asList(a);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().concat(w, this.strings);
  }

}
