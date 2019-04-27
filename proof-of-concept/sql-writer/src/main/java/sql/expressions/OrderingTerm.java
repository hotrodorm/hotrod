package sql.expressions;

import sql.QueryWriter;
import sql.exceptions.InvalidSQLStatementException;

public class OrderingTerm {

  private Expression expression;
  private boolean ascending;

  public OrderingTerm(final Expression expression, final boolean ascending) {
    if (expression == null) {
      throw new InvalidSQLStatementException(
          "Cannot use null value as column ordering. " + "Please speify a non null column in the ORDER BY clause");
    }
    this.expression = expression;
    this.ascending = ascending;
  }

  public Expression getExpression() {
    return this.expression;
  }

  public boolean isAscending() {
    return ascending;
  };

  public void renderTo(final QueryWriter w) {
    this.expression.renderTo(w);
    if (!this.ascending) {
      w.write(" desc");
    }
  }

}
