package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.numeric.NumericSyntaxExpression;

public class NumericCaseElseStage {

  private NumericCaseClause clause;

  public NumericCaseElseStage(final NumericCaseClause clause) {
    this.clause = clause;
  }

  public NumericSyntaxExpression end() {
    return this.clause;
  }

}
