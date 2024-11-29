package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.object.GeneralObjectExpression;

public class ObjectCaseElseStage {

  private ObjectCaseClause clause;

  public ObjectCaseElseStage(final ObjectCaseClause clause) {
    this.clause = clause;
  }

  public GeneralObjectExpression end() {
    return this.clause;
  }

}
