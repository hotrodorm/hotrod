package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLClauseException;

public class JoinLateral extends Join {

  private TableExpression tableExpression;

  public JoinLateral(final TableExpression tableExpression) {
    super(tableExpression);
    if (tableExpression == null) {
      throw new InvalidLiveSQLClauseException("The table, view, or subquery on a join cannot be null");
    }
    this.tableExpression = tableExpression;
  }

  TableExpression getTableExpression() {
    return this.tableExpression;
  }

}
