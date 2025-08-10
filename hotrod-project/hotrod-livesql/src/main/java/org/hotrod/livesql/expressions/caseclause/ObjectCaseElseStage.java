package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.object.ObjectSyntaxExpression;

public class ObjectCaseElseStage {

  private ObjectCaseClause clause;

  public ObjectCaseElseStage(final ObjectCaseClause clause) {
    this.clause = clause;
  }

  public ObjectSyntaxExpression end() {
    return this.clause;
  }

}
