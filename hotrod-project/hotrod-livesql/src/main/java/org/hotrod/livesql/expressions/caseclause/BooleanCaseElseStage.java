package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;

public class BooleanCaseElseStage {

  private BooleanCaseClause clause;

  public BooleanCaseElseStage(final BooleanCaseClause clause) {
    this.clause = clause;
  }

  public BooleanSyntaxExpression end() {
    return this.clause;
  }

}
