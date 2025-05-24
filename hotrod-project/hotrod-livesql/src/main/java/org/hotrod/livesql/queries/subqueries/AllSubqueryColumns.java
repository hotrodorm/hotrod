package org.hotrod.livesql.queries.subqueries;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.metadata.WrappingColumn;

public class AllSubqueryColumns extends WrappingColumn {

  private Subquery subquery;

  protected AllSubqueryColumns(final Subquery subquery) {
    this.subquery = subquery;
  }

  // ResultSetColumn

  @Override
  protected Expression getExpression() {
    return null;
  }

  @Override
  protected List<Expression> unwrap() {
    return this.subquery.getExpandedColumns();
  }

}
