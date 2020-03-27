package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class DateTimeCoalesce extends DateTimeExpression {

  private List<Expression<java.util.Date>> expressions;

  public DateTimeCoalesce(final DateTimeExpression... values) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.expressions.addAll(Arrays.asList(values));
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().coalesce(w, this.expressions);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    for (Expression<java.util.Date> e : this.expressions) {
      e.validateTableReferences(tableReferences, ag);
    }
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    for (Expression<java.util.Date> e : this.expressions) {
      e.designateAliases(ag);
    }
  }

}
