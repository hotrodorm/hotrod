package org.hotrod.runtime.livesql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferences;
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

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    for (Expression<T> e : this.expressions) {
      e.validateTableReferences(tableReferences, ag);
    }
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    for (Expression<T> e : this.expressions) {
      e.designateAliases(ag);
    }
  }

}
