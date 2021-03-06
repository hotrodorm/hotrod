package org.hotrod.runtime.livesql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.QueryWriter;

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
    w.getSqlDialect().getFunctionRenderer().coalesce(w, this.expressions);
  }

}
