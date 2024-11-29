package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.binary.GeneralByteArrayExpression;

public class ByteArrayCaseElseStage {

  private ByteArrayCaseClause clause;

  public ByteArrayCaseElseStage(final ByteArrayCaseClause clause) {
    this.clause = clause;
  }

  public GeneralByteArrayExpression end() {
    return this.clause;
  }

}
