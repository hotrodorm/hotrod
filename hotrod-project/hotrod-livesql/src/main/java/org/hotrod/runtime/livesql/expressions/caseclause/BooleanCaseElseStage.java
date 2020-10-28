package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanCaseElseStage {

  private BooleanCaseClause clause;

  public BooleanCaseElseStage(final BooleanCaseClause clause) {
    this.clause = clause;
  }

  public Predicate end() {
    return this.clause;
  }

}
