package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLClauseException;

public class LeftJoinLateral extends Join {

  private TableExpression tableExpression;

  public LeftJoinLateral(final TableExpression tableExpression) {
    super(tableExpression);
    this.tableExpression = tableExpression;
  }

  TableExpression getTableExpression() {
    return this.tableExpression;
  }

}
