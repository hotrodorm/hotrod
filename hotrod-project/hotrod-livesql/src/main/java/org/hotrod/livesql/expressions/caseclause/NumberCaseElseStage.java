package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.numbers.NumberExpression;

public class NumberCaseElseStage {

  private NumberCaseClause clause;

  public NumberCaseElseStage(final NumberCaseClause clause) {
    this.clause = clause;
  }

  public NumberExpression end() {
    return this.clause;
  }

}
