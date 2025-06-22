package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.exceptions.InvalidLiveSQLClauseException;

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

  protected void assembleColumns() {
    this.tableExpression.assembleColumns();
  }

}
