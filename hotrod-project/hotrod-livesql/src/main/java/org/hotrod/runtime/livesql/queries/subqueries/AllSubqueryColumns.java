package org.hotrod.runtime.livesql.queries.subqueries;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.metadata.WrappingColumn;

public class AllSubqueryColumns extends WrappingColumn {

  private Subquery subquery;

  protected AllSubqueryColumns(final Subquery subquery) {
    this.subquery = subquery;
  }

  @Override
  protected List<Expression> unwrap() {
    return this.subquery.getExpandedColumns();
  }

}
