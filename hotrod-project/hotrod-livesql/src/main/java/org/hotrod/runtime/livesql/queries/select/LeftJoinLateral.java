package org.hotrod.runtime.livesql.queries.select;

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
