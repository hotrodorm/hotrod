package org.hotrod.livesql.queries.subqueries;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.metadata.SQLMetaExpression;

public class AllSubqueryColumns extends SQLMetaExpression {

  private Subquery subquery;

  protected AllSubqueryColumns(final Subquery subquery) {
    this.subquery = subquery;
  }

  // ResultSetColumn

  @Override
  protected List<Expression> expand() {
    return this.subquery.getResolvedColumns();
  }

}
