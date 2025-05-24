package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.object.ObjectExpression;

public class ObjectCaseElseStage {

  private ObjectCaseClause clause;

  public ObjectCaseElseStage(final ObjectCaseClause clause) {
    this.clause = clause;
  }

  public ObjectExpression end() {
    return this.clause;
  }

}
