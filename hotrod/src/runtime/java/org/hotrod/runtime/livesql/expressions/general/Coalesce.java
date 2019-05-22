package org.hotrod.runtime.livesql.expressions.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class Coalesce<T> extends Expression<T> {

  private List<Expression<T>> expressions;

  public Coalesce(final Expression<T> first, final Expression<T>... rest) {
    super(Expression.PRECEDENCE_FUNCTION);
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
