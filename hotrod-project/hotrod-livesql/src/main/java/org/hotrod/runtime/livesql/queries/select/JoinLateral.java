package org.hotrod.runtime.livesql.queries.select;

public class JoinLateral extends Join {

  private TableExpression tableExpression;

  public JoinLateral(final TableExpression tableExpression) {
    super(tableExpression);
    this.tableExpression = tableExpression;
  }

  TableExpression getTableExpression() {
    return this.tableExpression;
  }

}
