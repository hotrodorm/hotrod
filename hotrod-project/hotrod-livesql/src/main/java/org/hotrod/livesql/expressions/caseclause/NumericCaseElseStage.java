package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class NumericCaseElseStage {

  private NumericCaseClause clause;

  public NumericCaseElseStage(final NumericCaseClause clause) {
    this.clause = clause;
  }

  public NumericExpression end() {
    return this.clause;
  }

}
