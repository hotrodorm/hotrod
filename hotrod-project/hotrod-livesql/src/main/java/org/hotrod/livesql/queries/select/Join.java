package org.hotrod.livesql.queries.select;

import java.util.List;

import org.hotrod.livesql.exceptions.InvalidLiveSQLClauseException;
import org.hotrod.livesql.expressions.Expression;

public abstract class Join {

  private TableExpression tableExpression;

  public Join(final TableExpression tableExpression) {
    if (tableExpression == null) {
      throw new InvalidLiveSQLClauseException("The table, view, or subquery on a join cannot be null");
    }
    this.tableExpression = tableExpression;
  }

  TableExpression getTableExpression() {
    return this.tableExpression;
  }

  protected List<Expression> assembleColumns() {
    return this.tableExpression.assembleColumns();
  }

}
