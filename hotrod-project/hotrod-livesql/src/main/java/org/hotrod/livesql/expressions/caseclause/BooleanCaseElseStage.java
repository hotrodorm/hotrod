package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.predicates.BooleanExpression;

public class BooleanCaseElseStage {

  private BooleanCaseClause clause;

  public BooleanCaseElseStage(final BooleanCaseClause clause) {
    this.clause = clause;
  }

  public BooleanExpression end() {
    return this.clause;
  }

}
