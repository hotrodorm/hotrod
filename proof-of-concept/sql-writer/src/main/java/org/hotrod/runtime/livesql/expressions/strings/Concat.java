package org.hotrod.runtime.livesql.expressions.strings;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public class Concat extends StringFunction {

  private List<Expression<String>> strings;

  public Concat(final List<Expression<String>> strings) {
    super();
    this.strings = strings;
  }

  public Concat(final Expression<String> a, final Expression<String> b) {
    super();
    this.strings = new ArrayList<Expression<String>>();
    this.strings.add(a);
    this.strings.add(b);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().concat(w, this.strings);
  }

}
