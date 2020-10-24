package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;

public class ByteArrayCaseElseStage {

  private ByteArrayCaseClause clause;

  public ByteArrayCaseElseStage(final ByteArrayCaseClause clause) {
    this.clause = clause;
  }

  public ByteArrayExpression end() {
    return this.clause;
  }

}
