package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;

public class ObjectCaseElseStage {

  private ObjectCaseClause clause;

  public ObjectCaseElseStage(final ObjectCaseClause clause) {
    this.clause = clause;
  }

  public ObjectExpression end() {
    return this.clause;
  }

}
