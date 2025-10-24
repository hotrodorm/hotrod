package org.hotrod.livesql.queries.select;

import java.util.Set;

import org.hotrod.livesql.exceptions.InvalidLiveSQLClauseException;
import org.hotrod.livesql.queries.select.sets.SelectObject;

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

  protected void renderColumns(Set<SelectObject<?>> compiling) {
    this.tableExpression.renderColumns(compiling);
  }

}
