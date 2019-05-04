package org.hotrod.runtime.sql.expressions.strings;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.sql.QueryWriter;

public class Concat extends StringFunction {

  private List<StringExpression> strings;

  public Concat(final List<StringExpression> strings) {
    super();
    this.strings = strings;
  }

  public Concat(final StringExpression a, final StringExpression b) {
    super();
    this.strings = new ArrayList<StringExpression>();
    this.strings.add(a);
    this.strings.add(b);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().concat(w, this.strings);
  }

}
