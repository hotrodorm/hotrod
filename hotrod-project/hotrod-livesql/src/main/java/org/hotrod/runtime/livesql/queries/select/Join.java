package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLClauseException;
import org.hotrod.runtime.livesql.queries.subqueries.EmergingColumn;

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

  protected List<EmergingColumn> assembleColumns() {
    return this.tableExpression.assembleColumns();
  }

}
