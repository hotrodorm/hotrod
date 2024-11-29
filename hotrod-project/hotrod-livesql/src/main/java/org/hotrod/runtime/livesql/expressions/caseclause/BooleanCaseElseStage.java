package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.predicates.GeneralBooleanExpression;

public class BooleanCaseElseStage {

  private BooleanCaseClause clause;

  public BooleanCaseElseStage(final BooleanCaseClause clause) {
    this.clause = clause;
  }

  public GeneralBooleanExpression end() {
    return this.clause;
  }

}
