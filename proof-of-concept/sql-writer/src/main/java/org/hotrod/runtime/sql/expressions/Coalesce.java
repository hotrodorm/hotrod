package org.hotrod.runtime.sql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.sql.QueryWriter;

import sql.util.Separator;

public class Coalesce<T> extends Expression<T> {

  private static final int PRECEDENCE = 1;

  private List<Expression<T>> expressions;

  @SafeVarargs
  public Coalesce(final Expression<T> first, final Expression<T>... rest) {
    super(PRECEDENCE);
    this.expressions = new ArrayList<Expression<T>>();
    this.expressions.add(first);
    this.expressions.addAll(Arrays.asList(rest));
  }

  @Override
  public void renderTo(final QueryWriter w) {
    String function = w.getSqlDialect().getFunctionTranslator().getCoalesce();
    w.write(function);
    w.write("(");
    Separator s = new Separator();
    for (Expression<T> expr : this.expressions) {
      w.write(s.render());
      super.renderInner(expr, w);
    }
    w.write(")");
  }

}
